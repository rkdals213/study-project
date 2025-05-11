package mhk.discount

import org.springframework.stereotype.Component

@Component
class DiscountService {

    fun checkPlanNotChanged(contractId: String, currentPlan: String): Boolean {
        return true
    }

    fun checkFinancialRecordsExist(customerId: String, contractId: String, financialRecord: String): Boolean {
        return true
    }
}
