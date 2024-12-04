package mhk

import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class TestKafkaListener {
    private val log = LoggerFactory.getLogger(TestKafkaListener::class.java)

    @KafkaListener(
        groupId = "batch-to-web-group-batch",
        topics = ["batch-to-web"],
    )
    fun batchToWebBatchListener(datas: ConsumerRecords<String, String>, ack: Acknowledgment) {
        datas.forEach {
            log.info("batch-to-web-group-batch : ${it.key()} / ${it.value()}")
        }
        ack.acknowledge()
    }
}
