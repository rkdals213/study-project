package mhk

import jakarta.annotation.PostConstruct
import mhk.entity.Ticket
import mhk.repository.TicketRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val key = "ticket"

@RestController
@RequestMapping("/tickets")
class TicketController(
    private val ticketRepository: TicketRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    @PostMapping("/{memberId}")
    @Transactional
    fun getTicket(@PathVariable memberId: Long) {
        val ticket = redisTemplate.opsForList().leftPop(key) ?: throw RuntimeException("Ticket not found")

        Ticket(
            memberId = memberId,
            ticketId = ticket.toString()
        ).let { ticketRepository.save(it) }
    }

    @PostConstruct
    fun init() {
        repeat(10) {
            redisTemplate.opsForList().leftPush(key, "$key-$it")
        }
    }
}
