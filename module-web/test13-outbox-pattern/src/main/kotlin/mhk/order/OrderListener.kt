package mhk.order

import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OrderListener(
    private val orderRepository: OrderRepository
) {
    private val log = LoggerFactory.getLogger(OrderListener::class.java)

    @KafkaListener(
        groupId = "outbox-pattern-group-consumer",
        topics = ["outbox-pattern-group"],
    )
    fun orderListener(datas: ConsumerRecords<String, String>, ack: Acknowledgment) {
        datas.forEach {
            val order = orderRepository.getOrder(it.value())
            log.info("Received order : ${order.id}")
        }
        ack.acknowledge()
    }
}
