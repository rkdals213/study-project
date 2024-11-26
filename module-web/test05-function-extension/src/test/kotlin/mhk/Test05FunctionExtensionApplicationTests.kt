package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import mhk.StringUtils.maskingPhoneNumber

class Test05FunctionExtensionApplicationTests : FunSpec({
    test("전화번호를 마스킹 처리하면 가운데 숫자가 마스킹된다") {
        val phoneNumber = "010-1234-5678"
        val maskingPhoneNumber = phoneNumber.maskingPhoneNumber()
        maskingPhoneNumber shouldBe "010-****-5678"
    }
})
