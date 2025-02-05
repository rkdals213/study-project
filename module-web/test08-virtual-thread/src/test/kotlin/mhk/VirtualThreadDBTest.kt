package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Executors
import kotlin.time.DurationUnit
import kotlin.time.measureTime

@SpringBootTest
class VirtualThreadDBTest(
    private val virtualThreadRepository: VirtualThreadRepository,
) : FunSpec({

    context("기본 DB 호출") {
        test("1초 걸리는 쿼리를 5번 호출하면 5초가 걸린다") {
            measureTime {
                repeat(5) {
                    virtualThreadRepository.getDate()
                }
            }.apply {
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 5
            }
        }
    }

    context("가상 쓰레드 DB 호출") {
        test("1초 걸리는 쿼리를 5번 호출하면 1초가 걸린다") {
            val executor = Executors.newVirtualThreadPerTaskExecutor()

            val jobs = List(5) {
                executor.submit {
                    virtualThreadRepository.getDate()
                }
            }

            measureTime {
                jobs.forEach { it.get() }
            }.apply {
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 1
            }

            executor.shutdown()
        }
    }

    context("코루틴 DB 호출") {
        test("1초 걸리는 쿼리를 5번 호출하면 5초가 걸린다") {
            measureTime {
                coroutineScope {
                    (1..5).map {
                        async { virtualThreadRepository.getDateCoroutine() } // blocking
                    }.awaitAll()
                }
            }.apply {
                println(absoluteValue)
                absoluteValue.toInt(DurationUnit.SECONDS) shouldBe 5
            }
        }
    }
})
