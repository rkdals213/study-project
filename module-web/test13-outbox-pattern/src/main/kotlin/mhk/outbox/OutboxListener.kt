package mhk.outbox

import jakarta.transaction.Transactional
import mhk.entity.EventLog
import mhk.repository.EventLogRepository
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OutboxListener(
    private val eventLogRepository: EventLogRepository
) {

    private val log = LoggerFactory.getLogger(OutboxListener::class.java)

    @KafkaListener(
        groupId = "outbox-pattern-group-outbox-consumer",
        topics = ["outbox-pattern-group"],
    )
    @Transactional
    fun orderListener(datas: ConsumerRecords<String, String>, ack: Acknowledgment) {
        datas.forEach {
            log.info("message consumed : ${it.value()}")

            val eventLog = eventLogRepository.findByEventId(it.value()) ?: throw RuntimeException("event not found")
            eventLog.status = EventLog.Status.EVENT_CONSUMED
            eventLog.updatedAt = LocalDateTime.now()
        }
        ack.acknowledge()
    }
}
