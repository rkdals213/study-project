package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import mhk.config.AcceptanceTest
import mhk.config.getResource
import mhk.config.givenRequestSpecification
import mhk.config.postResource

@AcceptanceTest
class ExposedAcceptanceFlowTest : FunSpec({

    context("exposed를 이용한 flow 테스트") {
        test("엔티티 등록 및 조회에") {
            val response = postResource(
                request = givenRequestSpecification(),
                url = "/teamMembers",
                body = ""
            )

            response.statusCode() shouldBe 200

            val response2 = getResource(
                request = givenRequestSpecification(),
                url = "/teamMembers",
            )

            response2.statusCode() shouldBe 200

            val response3 = getResource(
                request = givenRequestSpecification(),
                url = "/teamMembers/mix",
            )

            response3.statusCode() shouldBe 200
        }
    }

    extension(SpringExtension)
})
