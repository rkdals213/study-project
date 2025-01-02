package mhk

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.Executors
import kotlin.time.measureTime

@SpringBootTest
class Test15RedisListApplicationTests : FunSpec({


    test("webClient로 호출") {
        val executor = Executors.newVirtualThreadPerTaskExecutor()

        val jobs = List(1000) {
            executor.submit {
                val response = WebClient.builder()
                    .baseUrl("http://localhost:8080")
                    .build()
                    .post()
                    .uri("/tickets/$it")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .block()

                println(response)
            }
        }

        measureTime {
            jobs.forEach {
                runCatching {
                    it.get()
                }.onFailure {
                    println(it)
                }.onSuccess {
                    println(it)
                }
            }
        }.apply {
            println(absoluteValue)
        }

        executor.shutdown()
    }
})
