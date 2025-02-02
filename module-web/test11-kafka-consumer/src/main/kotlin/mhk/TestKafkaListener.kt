package mhk

import jakarta.persistence.EntityManager
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import kotlin.time.measureTime

@Component
class TestKafkaListener(
    private val testKafkaService: TestKafkaService
) {
    @KafkaListener(
        groupId = "batch-to-web-group-batch",
        topics = ["batch-to-web"],
    )
    fun batchToWebBatchListener(datas: ConsumerRecords<String, String>, ack: Acknowledgment) {
        default(datas, ack)
//        withVirtualThread(datas, ack)
    }

    private fun default(datas: ConsumerRecords<String, String>, ack: Acknowledgment) {
        measureTime {
            datas.forEach {
                testKafkaService.doSomething(it)
            }
        }.apply {
            println(absoluteValue)
        }

        ack.acknowledge()
    }

    private fun withVirtualThread(datas: ConsumerRecords<String, String>, ack: Acknowledgment) {
        val executor = Executors.newVirtualThreadPerTaskExecutor()

        val jobs = datas.map {
            executor.submit {
                testKafkaService.doSomething(it)
            }
        }

        measureTime {
            jobs.forEach { it.get() }
        }.apply {
            println(absoluteValue)
        }

        ack.acknowledge()
    }
}

@Service
class TestKafkaService(
    private val em: EntityManager
) {
    private val log = LoggerFactory.getLogger(TestKafkaService::class.java)

    fun doSomething(consumerRecord: ConsumerRecord<String, String>) {
        log.info("${consumerRecord.key()} : ${consumerRecord.value()} start")
        em.createNativeQuery("select 1 from dual where sleep(1) = 0").resultList
        log.info("${consumerRecord.key()} : ${consumerRecord.value()} end")
    }
}
