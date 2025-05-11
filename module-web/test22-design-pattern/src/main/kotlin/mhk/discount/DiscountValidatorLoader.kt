package mhk.discount

import org.springframework.stereotype.Component

@Component
class DiscountValidatorLoader(
    private val discountValidators: List<DiscountValidator>
) {
    private val discountValidatorMap = discountValidators.associateBy { it.discountCode }

    fun getDiscountValidator(discountCode: DiscountCode): DiscountValidator? {
        return discountValidatorMap[discountCode]
    }

}
