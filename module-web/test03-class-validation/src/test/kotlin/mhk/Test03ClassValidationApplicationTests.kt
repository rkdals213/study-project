package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import mhk.config.AcceptanceTest
import mhk.config.getResource
import mhk.config.givenRequestSpecification
import mhk.config.postResource

private const val CORRECT_PHONE_NUMBER = "010-1234-5678"
private const val INCORRECT_PHONE_NUMBER = "010-1234-56789"

@AcceptanceTest
class Test03ClassValidationApplicationTests : FunSpec({

    context("GET 요청") {
        test("정확한 전화번호를 보내면 성공한다") {
            val response = getResource(
                request = givenRequestSpecification(),
                url = "/class-validation",
                params = mapOf("phoneNumber" to CORRECT_PHONE_NUMBER)
            )

            response.statusCode() shouldBe 200
        }

        test("부정확한 전화번호를 보내면 500에러 발생한다") {
            val response = getResource(
                request = givenRequestSpecification(),
                url = "/class-validation",
                params = mapOf("phoneNumber" to INCORRECT_PHONE_NUMBER)
            )

            response.statusCode() shouldBe 500
        }
    }

    context("POST 요청") {
        test("정확한 전화번호를 보내면 성공한다") {
            val body = """{
                    "phoneNumber" : "$CORRECT_PHONE_NUMBER"
                }              
            """.trimIndent()

            val response = postResource(
                request = givenRequestSpecification(),
                url = "/class-validation",
                body = body
            )

            response.statusCode() shouldBe 200
        }

        test("부정확한 전화번호를 보내면 400에러 발생한다") {
            val body = """{
                "phoneNumber" : "$INCORRECT_PHONE_NUMBER"
                }
            """.trimIndent()

            val response = postResource(
                request = givenRequestSpecification(),
                url = "/class-validation",
                body = body
            )

            response.statusCode() shouldBe 400
        }
    }

    extension(SpringExtension)
})
