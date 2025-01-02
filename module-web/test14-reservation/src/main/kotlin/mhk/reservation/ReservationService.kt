package mhk.reservation

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalDateTime

@Service
class ReservationService(
    private val reservationQueueRepository: ReservationQueueRepository,
    private val reservationSseEmitterRepository: ReservationSseEmitterRepository
) {
    private val logger = LoggerFactory.getLogger(ReservationService::class.java)

    fun generateEntranceToken(getEntranceTokenRequest: GetEntranceTokenRequest): GetEntranceTokenResponse {
        val token = "$TOKEN_PREFIX-${getEntranceTokenRequest.accountId}-$TOKEN_SUFFIX-${LocalDateTime.now()}"
        reservationQueueRepository.addLast(RESERVATION_ID_WAITING_QUEUE, token)

        return GetEntranceTokenResponse(token)
    }

    fun subscribeReservationOrder(token: String): SseEmitter {
        val sseEmitter = SseEmitter(DEFAULT_TIMEOUT)
        reservationSseEmitterRepository.save(token, sseEmitter)

        sseEmitter.onTimeout {
            sseEmitter.complete()
            logger.info("subscribe timeout")
        }
        sseEmitter.onError {
            sseEmitter.complete()
            logger.info("subscribe error")
        }
        sseEmitter.onCompletion {
            reservationSseEmitterRepository.deleteByToken(token)
            logger.info("subscribe complete")
        }

        sendNotification(token, sseEmitter, 100000)

        return sseEmitter
    }

    fun sendNotification() {
        logger.info("send notification")
        reservationSseEmitterRepository.findAll().forEach {
            val order = reservationQueueRepository.findOrderByToken(RESERVATION_ID_WAITING_QUEUE, it.first)
            sendNotification(it.first, it.second, order)
        }
    }

    private fun sendNotification(token: String, sseEmitter: SseEmitter, data: Any) {
        runCatching {
            sseEmitter.send(
                SseEmitter.event()
                    .id(token)
                    .data(data, MediaType.APPLICATION_JSON)
            )
        }.onSuccess {
            logger.info("$token subscribe started")
        }.onFailure {
            reservationSseEmitterRepository.deleteByToken(token)
            throw it
        }
    }

    companion object {
        private const val TOKEN_PREFIX = "ENTRANCE"
        private const val TOKEN_SUFFIX = "TOKEN"
        private const val RESERVATION_ID_WAITING_QUEUE = "concert01-waiting-queue"
        private const val DEFAULT_TIMEOUT = 600L * 1000 * 60
    }
}
