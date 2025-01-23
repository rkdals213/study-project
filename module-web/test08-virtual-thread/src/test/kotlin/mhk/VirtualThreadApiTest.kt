package mhk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.StructuredTaskScope
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

    context("가상 쓰레드 API 조회 테스트") {
        test("StructuredTaskScope.ShutdownOnFailure() : 중간에 예외가 발생하면 진행중인 쓰레드가 모두 취소된다") {
            shouldThrow<ExecutionException> {
                StructuredTaskScope.ShutdownOnFailure().use { scope ->
                    scope.fork {
                        feignClient.test01().apply { println(this.body) }.body
                    }

                    scope.fork {
                        feignClient.test02().apply { println(this.body) }.body
                    }

                    scope.fork {
                        feignClient.test03().apply { println(this.body) }.body
                    }

                    scope.fork {
                        feignClient.test04().apply { println(this.body) }.body
                    }

                    scope.join()
                    println(scope.isShutdown)
                    scope.throwIfFailed()
                }
            }
        }

        test("Executors.newVirtualThreadPerTaskExecutor() : 중간에 예외가 발생하면 진행중인 쓰레드가 모두 취소된다") {
            shouldThrow<ExecutionException> {
                val executor = Executors.newVirtualThreadPerTaskExecutor()
                val jobs = listOf(
                    executor.submit {
                        feignClient.test01().apply { println(this.body) }.body
                    },
                    executor.submit {
                        feignClient.test02().apply { println(this.body) }.body
                    },
                    executor.submit {
                        feignClient.test03().apply { println(this.body) }.body
                    },
                    executor.submit {
                        feignClient.test04().apply { println(this.body) }.body
                    }
                )

                try {
                    jobs.forEach { it.get() }
                } catch (e: ExecutionException) {
                    throw e
                } finally {
                    executor.shutdown()
                    println(executor.isShutdown)
                }
            }
        }
    }
})
