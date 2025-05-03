package mhk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class KotestSample : FunSpec({
    context("mock") {
        val someBean = mockk<SomeBean>()

        test("함수의 동작을 정의하지 않으면 예외가 발생한다") {
            shouldThrow<MockKException> {
                someBean.test01()
            }
        }

        test("함수의 동작을 정의하면 동작대로 실행된다") {
            every { someBean.test01() } returns "정의된 동작"

            someBean.test01() shouldBe "정의된 동작"
        }
    }

    context("spy") {
        val someBean = spyk<SomeBean>()

        test("함수의 동작을 정의하지 않으면 기존 함수가 실행된다") {
            someBean.test01() shouldBe "test bean"
        }

        test("함수의 동작을 정의하면 동작대로 실행된다") {
            every { someBean.test01() } returns "정의된 동작"

            someBean.test01() shouldBe "정의된 동작"
        }
    }

    context("slot") {
        val slot = slot<String>()
        val someBean = spyk<SomeBean>()

        test("함수의 입력값을 캡쳐하면 캡쳐된 값은 함수의 입력값이다") {
            every { someBean.test02(capture(slot)) } returns "정의된 동작"

            someBean.test02("test")
            slot.captured shouldBe "test"
        }
    }

    context("relaxed = true 옵션") {
        val someBean = mockk<SomeBean>(relaxed = true)

        test("함수의 동작을 정의하지 않으면 기본값이 리턴된다") {
            someBean.test01() shouldBe ""
        }
    }

    context("clearMockk") {
        val someBean = mockk<SomeBean>()

        test("clearMocks하면 정의된 함수의 동작들이 모두 제거된다") {
            every { someBean.test01() } returns "정의된 동작"
            someBean.test01() shouldBe "정의된 동작"

            clearMocks(someBean)

            shouldThrow<MockKException> {
                someBean.test01()
            }
        }
    }

    context("verify / confirmVerified") {
        val someBean = mockk<SomeBean>()

        test("함수가 실행되었는지 검증한다") {
            every { someBean.test01() } returns "정의된 동작1"
            every { someBean.test02(any()) } returns "정의된 동작2"

            someBean.test01() shouldBe "정의된 동작1"
            someBean.test02("") shouldBe "정의된 동작2"

            verify { someBean.test01() }
            verify { someBean.test02("") }

            confirmVerified(someBean)
        }
    }
})
