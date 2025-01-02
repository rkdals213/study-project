package mhk.reservation

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ReservationQueueRepository(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun addLast(key: String, token: String) {
        redisTemplate.opsForList().rightPush(key, token)
    }

    fun findOrderByToken(key: String, token: String): Long {
        return redisTemplate.opsForList().indexOf(key, token) ?: throw NoSuchElementException()
    }

    fun deleteByToken(key: String, token: String) {
        redisTemplate.opsForList().remove(key, 0, token)
    }
}
