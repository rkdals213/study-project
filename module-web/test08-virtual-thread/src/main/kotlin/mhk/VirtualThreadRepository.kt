package mhk

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class VirtualThreadRepository(
    private val em: EntityManager
) {
    fun getDate(): MutableList<Int> = em.createNativeQuery("select 1 from dual where sleep(1) = 0").resultList as MutableList<Int>
    suspend fun getDateCoroutine(): MutableList<Int> = em.createNativeQuery("select 1 from dual where sleep(1) = 0").resultList as MutableList<Int>
}
