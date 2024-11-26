package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Test04KotlinDslApplicationTests: FunSpec({
    test("MessageBuilder로 message 객체 생성") {
        val message = message("Kang", "this is sample message") {
            receiver = "Park"
            read = false
        }

        message.sender shouldBe "Kang"
    }

    test("PhoneBuilder로 phone 객체 생성") {
        val phone = phone {
            number = "010-1234-5678"
            name = "kang"
            image = "img.jpg"
            shortCutNumber = "1"
        }

        phone.number shouldBe "010-1234-5678"
    }
})
