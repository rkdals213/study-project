package mhk.order

import mhk.entity.EventLog
import mhk.repository.EventLogRepository
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val eventLogRepository: EventLogRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = LoggerFactory.getLogger(OrderService::class.java)

    fun createOrder(createOrderRequest: CreateOrderRequest) {
        Order(
            id = UUID.randomUUID().toString(),
            orderInfo = createOrderRequest.orderInfo,
        ).let {
            orderRepository.registerOrder(it)
            kafkaTemplate.send("outbox-pattern-group", it.id)
            eventLogRepository.save(
                EventLog(
                    eventId = it.id,
                    status = EventLog.Status.EVENT_PUBLISHED
                )
            )
            log.info("Created order : ${it.id}")
        }
    }
}
