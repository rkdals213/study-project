package mhk

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.Executors
import kotlin.time.measureTime

@SpringBootTest
class VirtualThreadApiTest(
    private val feignClient: VirtualThreadFeignClient
) : FunSpec({

    context("가상 쓰레드 api 호출") {
        test("feignClient로 호출") {
            val executor = Executors.newVirtualThreadPerTaskExecutor()

            val jobs = List(10) {
                executor.submit {
                    feignClient.test01().apply { println(this.body) }
                }
            }

            measureTime {
                jobs.forEach { it.get() }
            }.apply {
                println(absoluteValue)
            }

            executor.shutdown()
        }

        test("webClient로 호출") {
            val executor = Executors.newVirtualThreadPerTaskExecutor()

            val jobs = List(10) {
                executor.submit {
                    val response = WebClient.builder()
                        .baseUrl("http://localhost:8080")
                        .build()
                        .get()
                        .uri("/virtual-thread/test01")
                        .retrieve()
                        .bodyToMono(String::class.java)
                        .block()

                    println(response)
                }
            }

            measureTime {
                jobs.forEach { it.get() }
            }.apply {
                println(absoluteValue)
            }

            executor.shutdown()
        }
    }
})
