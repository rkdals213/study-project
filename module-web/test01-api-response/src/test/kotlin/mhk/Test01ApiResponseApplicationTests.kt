package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import mhk.config.AcceptanceTest
import mhk.config.getResource
import mhk.config.givenRequestSpecification
import java.math.BigDecimal

@AcceptanceTest
class Test01ApiResponseApplicationTests : FunSpec({

    test("단일 객체가 리턴되는 응답값 검증") {
        val response = getResource(
            request = givenRequestSpecification(),
            url = "/api-response/test01"
        )

        response.statusCode() shouldBe 200

        val testData = response.jsonPath().getObject("body", TestData::class.java)

        testData.data1 shouldBe "data1"
        testData.data2 shouldBe 1
        testData.data3 shouldBe 2L
        testData.data4 shouldBe BigDecimal.valueOf(10.00)
        testData.data5 shouldBe 'A'
    }

    test("리스트가 리턴되는 응답값 검증") {
        val response = getResource(
            request = givenRequestSpecification(),
            url = "/api-response/test02"
        )

        response.statusCode() shouldBe 200

        // 배열 첫번째
        response.jsonPath().getString("body[0].data1") shouldBe "data1"
        response.jsonPath().getInt("body[0].data2") shouldBe 1
        response.jsonPath().getLong("body[0].data3") shouldBe 1
        response.jsonPath().getObject("body[0].data4", BigDecimal::class.java) shouldBe BigDecimal.valueOf(10.00)
        response.jsonPath().getChar("body[0].data5") shouldBe 'A'

        // 배열 두번째
        response.jsonPath().getString("body[1].data1") shouldBe "data2"
        response.jsonPath().getInt("body[1].data2") shouldBe 2
        response.jsonPath().getLong("body[1].data3") shouldBe 2
        response.jsonPath().getObject("body[1].data4", BigDecimal::class.java) shouldBe BigDecimal.valueOf(20.00)
        response.jsonPath().getChar("body[1].data5") shouldBe 'B'

        // 배열 마지막
        response.jsonPath().getString("body[-1].data1") shouldBe "data3"
        response.jsonPath().getInt("body[-1].data2") shouldBe 3
        response.jsonPath().getLong("body[-1].data3") shouldBe 3
        response.jsonPath().getObject("body[-1].data4", BigDecimal::class.java) shouldBe BigDecimal.valueOf(30.00)
        response.jsonPath().getChar("body[-1].data5") shouldBe 'C'
    }

    extension(SpringExtension)
})
