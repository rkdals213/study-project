package mhk

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import mhk.config.JpaTx
import mhk.entity.QTestEntity.testEntity
import org.hibernate.Session
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Test19QueryApplication

fun main(args: Array<String>) {
    runApplication<Test19QueryApplication>(*args)
}

@RestController
class TestController(
    private val query: JPAQueryFactory,
    private val entityManager: EntityManager
) {

    @Transactional
    @PostMapping("/test01")
    fun test01() {
        val fetch = query.select(testEntity)
            .from(testEntity)
            .fetch()

        println(fetch)
        fetch.forEach {
            println(entityManager.contains(it))
        }

    }

    @PostMapping("/test02")
    fun test02() {
        JpaTx.writable {
            val fetch = query.select(testEntity)
                .from(testEntity)
                .fetch()
            println("Hibernate Session is Open: ${entityManager.unwrap(Session::class.java).isOpen}")

            println(fetch)
            fetch.forEach {
                println(entityManager.contains(it))
            }
        }
    }

}

