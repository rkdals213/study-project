package mhk.discount

interface DiscountValidator {

    fun validate(contractServiceComponentsLine: ContractServiceComponentsLine): String

    val discountCode: DiscountCode
}


data class ContractServiceComponentsLine(
    val customerId: String,
    val contractId: String,
    val plan: String
)
