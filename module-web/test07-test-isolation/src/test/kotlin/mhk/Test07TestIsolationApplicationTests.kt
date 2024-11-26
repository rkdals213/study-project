package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import mhk.config.AcceptanceTest
import mhk.config.getResource
import mhk.config.givenRequestSpecification
import mhk.config.postResource

@AcceptanceTest
class Test07TestIsolationApplicationTests: FunSpec({

    context("kotest와 restassured를 사용한 테스트") {
        test("api 호출에 성공한다") {
            val body = """
                {
                    "data" : "this is kotest + restassured"
                }
            """.trimIndent()

            val response = postResource(
                request = givenRequestSpecification(),
                url = "/test-isolation",
                body = body
            )

            response.statusCode() shouldBe 200
        }
    }

    context("데이터 1") {
        val body = """
                {
                    "data" : "data 1"
                }
            """.trimIndent()

        test("1만 리턴된다") {
            postResource(
                request = givenRequestSpecification(),
                url = "/test-isolation/insert",
                body = body
            )

            val response = getResource(
                request = givenRequestSpecification(),
                url = "/test-isolation/get",
            )

            response.statusCode() shouldBe 200
            response.jsonPath().getInt("body[0].id") shouldBe 1
            response.jsonPath().getString("body[0].data") shouldBe "data 1"
        }

    }

    context("데이터 2") {
        val body = """
                {
                    "data" : "data 2"
                }
            """.trimIndent()

        test("2만 리턴된다") {
            postResource(
                request = givenRequestSpecification(),
                url = "/test-isolation/insert",
                body = body
            )

            val response = getResource(
                request = givenRequestSpecification(),
                url = "/test-isolation/get",
            )

            response.jsonPath().getInt("body[0].id") shouldBe 1
            response.jsonPath().getString("body[0].data") shouldBe "data 2"

        }
    }

    extension(SpringExtension)
})
