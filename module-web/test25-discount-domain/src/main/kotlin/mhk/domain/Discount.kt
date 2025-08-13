package mhk.domain

class Discount(
    val discountCode: String,
    val discountType: DiscountType,
)

class DiscountRule(
    val id: Long,
    val discountCode: String,
    val discountGrade: String,
)

class DiscountEligibleProduct(
    val id: Long,
    val discountCode: String,
    val productCode: String
)

enum class DiscountType {
    FIXED, RATE
}
