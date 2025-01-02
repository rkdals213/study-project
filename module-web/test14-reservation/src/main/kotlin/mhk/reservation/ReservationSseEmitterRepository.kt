package mhk.reservation

import org.springframework.stereotype.Repository
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Repository
class ReservationSseEmitterRepository(
    private val emitters: ConcurrentHashMap<String, SseEmitter> = ConcurrentHashMap()
) {

    fun save(token: String, sseEmitter: SseEmitter): SseEmitter {
        emitters[token] = sseEmitter
        return sseEmitter
    }

    fun findAll(): List<Pair<String, SseEmitter>> {
        return emitters.entries.map {
            Pair(it.key, it.value)
        }
    }

    fun deleteByToken(token: String) {
        emitters.remove(token)
    }
}
