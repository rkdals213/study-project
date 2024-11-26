package mhk

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe


class Test06NullCheckApplicationTests: BehaviorSpec({

    Given("Member 레포지토리에서") {
        val memberRepository = MemberRepository()
        When("값을 불러왔을때 null check 하면") {
            val member: String? = memberRepository.findByIdOrNull(1L)
            requireNotNull(member) { "value is null" }

            Then("해당값은 null이 아니며 이후 not null 값으로 사용할 수 있다") {
                val notNullMember: String = member
                notNullMember shouldNotBe null
            }
        }
    }
})

