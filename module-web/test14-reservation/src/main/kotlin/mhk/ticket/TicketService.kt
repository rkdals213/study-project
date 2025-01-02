package mhk.ticket

import mhk.reservation.ReservationQueueRepository
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TicketService(
    private val tickets: MutableList<Ticket> = mutableListOf(Ticket(1, Ticket.Status.ON_SALE), Ticket(2, Ticket.Status.ON_SALE)),
    private val reservationQueueRepository: ReservationQueueRepository,
    private val redissonClient: RedissonClient
) {

    fun getTickets(): List<Ticket> {
        return tickets
    }

    fun checkTicketAndLock(id: Long) {
        tickets.find { it.id == id }?.let {
            check(it.status == Ticket.Status.ON_SALE)
        }

        runCatching {
            val lock = redissonClient.getLock("$RESERVATION_ID-$id")

            if (lock.tryLock(0, 60, TimeUnit.SECONDS)) {
                println("Lock acquired successfully")
            } else {
                throw RuntimeException()
            }
        }.onFailure {
            println("Lock acquire failed")
            throw it
        }
    }

    fun purchaseTicket(id: Long, token: String) {
        tickets.find { it.id == id }?.let {
            it.status = Ticket.Status.SOLD_OUT
        }

        reservationQueueRepository.deleteByToken(RESERVATION_ID_WAITING_QUEUE, token)
    }

    companion object {
        private const val RESERVATION_ID = "concert01"
        private const val RESERVATION_ID_WAITING_QUEUE = "concert01-waiting-queue"
    }
}
