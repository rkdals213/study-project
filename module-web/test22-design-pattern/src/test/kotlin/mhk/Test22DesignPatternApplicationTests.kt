package mhk

import io.kotest.core.spec.style.FunSpec
import mhk.discount.ContractServiceComponentsLine
import mhk.discount.DiscountCode
import mhk.discount.DiscountValidatorLoader
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Test22DesignPatternApplicationTests(
    private val discountValidatorLoader: DiscountValidatorLoader
) : FunSpec({

    test("할인을 검증") {
        val discountValidator = discountValidatorLoader.getDiscountValidator(DiscountCode.DIS01)

        val result = discountValidator?.validate(
            ContractServiceComponentsLine(
                customerId = "100000000100000",
                contractId = "C000000001",
                plan = "PLAN001"
            )
        ) ?: "FAIL"

        println(result)
    }
})
