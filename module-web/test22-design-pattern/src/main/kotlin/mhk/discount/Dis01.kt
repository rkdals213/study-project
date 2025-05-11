package mhk.discount

import org.springframework.stereotype.Component

@Component
class Dis01(
    private val discountService: DiscountService
) : DiscountValidator {

    override fun validate(contractServiceComponentsLine: ContractServiceComponentsLine): String {
        val checkPlanChanged = discountService.checkPlanNotChanged(
            contractId = contractServiceComponentsLine.contractId,
            currentPlan = contractServiceComponentsLine.plan
        )

        val checkFinancialRecordsExist = discountService.checkFinancialRecordsExist(
            customerId = contractServiceComponentsLine.customerId,
            contractId = contractServiceComponentsLine.contractId,
            financialRecord = "LDZ0000001"
        )

        return if (checkPlanChanged && checkFinancialRecordsExist) "GRADE01" else "E000001"
    }

    override val discountCode: DiscountCode
        get() = DiscountCode.DIS01
}
