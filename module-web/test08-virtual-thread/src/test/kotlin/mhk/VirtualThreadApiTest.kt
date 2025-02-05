package mhk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.StructuredTaskScope
import kotlin.time.DurationUnit
import kotlin.time.measureTime

@SpringBootTest
class VirtualThreadApiTest(
    private val feignClient: VirtualThreadFeignClient,
) : FunSpec({

    context("가상 쓰레드 API 호출") {
        test("1초 걸리는 api를 feignClient로 5번 호출하면 1초가 걸린다") {
            val executor = Executors.newVirtualThreadPerTaskExecutor()

            val jobs = List(5) {
                executor.submit {
                    feignClient.test01().apply { println(this.body) }
                }
            }

            measureTime {
                jobs.forEach { it.get() }
            }.apply {
                println(absoluteValue)
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 1
            }

            executor.shutdown()
        }

        test("1초 걸리는 api를 webClient로 5번 호출하면 1초가 걸린다") {
            val executor = Executors.newVirtualThreadPerTaskExecutor()

            val jobs = List(5) {
                executor.submit {
                    WebClient.builder()
                        .baseUrl("http://localhost:8080")
                        .build()
                        .get()
                        .uri("/virtual-thread/test01")
                        .retrieve()
                        .bodyToMono(String::class.java)
                        .block()
                        .apply { println(this) }
                }
            }

            measureTime {
                jobs.forEach { it.get() }
            }.apply {
                println(absoluteValue)
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 1
            }

            executor.shutdown()
        }
    }

    context("코루틴 API 호출") {
        test("1초 걸리는 api를 feignClient로 5번 호출하면 5초가 걸린다") {
            measureTime {
                coroutineScope {
                    (1..5).map {
                        async { feignClient.test01().apply { println(this.body) } } // feign client는 reactive를 지원하지 않는다 : https://docs.spring.io/spring-cloud-openfeign/reference/spring-cloud-openfeign.html#reactive-support
                    }.awaitAll()
                }
            }.apply {
                println(absoluteValue)
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 5
            }
        }

        test("1초 걸리는 api를 webClient로 5번 호출하면 1초가 걸린다") {
            measureTime {
                coroutineScope {
                    (1..5).map {
                        async {
                            WebClient.builder()
                                .baseUrl("http://localhost:8080")
                                .build()
                                .get()
                                .uri("/virtual-thread/test01")
                                .retrieve()
                                .awaitBody<String>()
                                .apply { println(this) }
                        }
                    }.awaitAll()
                }
            }.apply {
                println(absoluteValue)
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 1
            }
        }
    }


    context("가상 쓰레드 예외 전파 테스트") {
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
