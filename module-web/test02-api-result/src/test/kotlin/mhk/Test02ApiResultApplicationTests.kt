package mhk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.ResponseEntity

private val client: ApiResultClient = mockk()

class Test02ApiResultApplicationTests : FunSpec({

    context("API의 200상태의 정상 응답") {
        every { client.test01() } returns ResponseEntity.ok("""{"data1": "test data1", "data2": 1000}""")

        test("정삭적으로 데이터 읽어온다") {
            val result = client.test01().responseResult<ApiResultResponse>()
                .onSuccess {
                    println("this is success data : $it")
                }
                .onFailure {
                    println("this is failure data : $it")
                }
                .getOrThrow()

            result.data1 shouldBe "test data1"
            result.data2 shouldBe 1000
        }
    }

    context("API의 200상태의 포맷과 다른 응답") {
        every { client.test01() } returns ResponseEntity.ok("""{"data3": "test data1", "data4": 1000}""")

        test("응답값이 설정한 타입과 다르면 기본값으로 응답받는다") {
            val result = client.test01().responseResult<ApiResultResponse>()
                .onSuccess {
                    println("this is success data : $it")
                }
                .onFailure {
                    println("this is failure data : $it")
                }
                .getOrDefault(
                    default = ApiResultResponse("default response", 23000),
                    transform = { it })

            result.data1 shouldBe "default response"
            result.data2 shouldBe 23000
        }
    }

    context("API의 200상태의 오류 응답") {
        every { client.test01() } returns ResponseEntity.ok("""{"message": "알수없는 오류가 발생했습니다", "code": "ERROR CODE", "status": 500}""")

        test("응답받은 오류를 기반으로 예외를 발생시킨다") {
            val result = shouldThrow<ApiException> {
                client.test01().responseResult<ApiResultResponse>()
                    .onSuccess {
                        println("this is success data : $it")
                    }
                    .onFailure {
                        println("this is failure data : $it")
                    }
                    .getOrThrow()
            }

            result.errorResponse.message shouldBe "알수없는 오류가 발생했습니다"
            result.errorResponse.code shouldBe "ERROR CODE"
            result.errorResponse.status shouldBe 500
        }
    }

    context("API의 400상태의 실패 응답") {
        every { client.test01() } returns ResponseEntity.badRequest().build()

        test("예외를 발생시킨다") {
            val result = shouldThrow<ApiException> {
                client.test01().responseResult<ApiResultResponse>()
                    .onSuccess {
                        println("this is success data : $it")
                    }
                    .onFailure {
                        println("this is failure data : $it")
                    }
                    .getOrThrow()
            }

            result.errorResponse.message shouldBe "Unknown Error"
            result.errorResponse.code shouldBe "ERROR"
            result.errorResponse.status shouldBe 500
        }
    }
})
