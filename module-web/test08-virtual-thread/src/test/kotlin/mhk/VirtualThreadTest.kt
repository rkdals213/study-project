package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.StructuredTaskScope
import kotlin.time.measureTime

class VirtualThreadTest : FunSpec({

    context("가상 쓰레드 기본 테스트") {
        test("100개의 숫자를 리스트에 더하면 sleep한 시간만큼 시간이 소요된다") {
            val executor = Executors.newVirtualThreadPerTaskExecutor()
            val results = mutableListOf<Int>()

            val jobs = List(100) {
                executor.submit {
                    sleep(1000)
                    synchronized(results) {
                        results.add(it)
                    }
                    sleep(1000)
                    println("Adding $it")
                }
            }

            measureTime {
                jobs.forEach { it.get() }
            }.apply {
                println(absoluteValue)
            }

            executor.shutdown()
            results.size shouldBe 100
        }
    }

    context("가상 쓰레드 구조화 테스트") {
        test("100개의 숫자를 리스트에 더하다 오류가 발생하면 멈추고 예외를 던진다") {
            val results = mutableListOf<Int>()
            try {
                StructuredTaskScope.ShutdownOnFailure().use { scope ->
                    List(100) {
                        scope.fork {
                            sleep(1000L)
                            if (it % 3 == 0) {
                                throw ClassNotFoundException()
                            }
                            synchronized(results) {
                                results.add(it)
                            }
                            println("Adding $it")
                        }
                    }
                    scope.join()
                    scope.throwIfFailed()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            println("Results size: ${results.size}")
        }
    }

    context("가상 쓰레드 대량 생성 테스트") {
        test("대량의 쓰레드를 생성하여 처리하여도 sleep한 시간만큼 소요된다") {
            val threads = (1..10000).map {
                Thread.ofVirtual().start {
                    try {
                        sleep(1000)
                        println("Thread $it finished")
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }

            measureTime {
                threads.forEach { it.join() }
            }.apply {
                println(absoluteValue)
            }
        }
    }
})
