package mhk

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(val amount: BigDecimal) {

    constructor(amount: Long) : this(BigDecimal.valueOf(amount))
    constructor(amount: String) : this(BigDecimal(amount))


    operator fun plus(other: Money): Money = Money(this.amount + other.amount)

    operator fun minus(other: Money): Money = Money(this.amount - other.amount)

    operator fun times(multiplier: Long): Money = Money(this.amount.multiply(BigDecimal.valueOf(multiplier)))
    operator fun times(multiplier: BigDecimal): Money = Money(this.amount.multiply(multiplier))

    operator fun div(divisor: Long): Money =
        Money(this.amount.divide(BigDecimal.valueOf(divisor), 0, RoundingMode.DOWN))

    operator fun div(divisor: BigDecimal): Money = Money(this.amount.divide(divisor, 0, RoundingMode.DOWN))

    operator fun compareTo(other: Money): Int = this.amount.compareTo(other.amount)

    fun isPositive(): Boolean = amount > BigDecimal.ZERO
    fun isNegative(): Boolean = amount < BigDecimal.ZERO
    fun isZero(): Boolean = amount.compareTo(BigDecimal.ZERO) == 0

    companion object {
        val ZERO: Money = Money(BigDecimal.ZERO)
    }

    override fun toString(): String = "₩${amount.setScale(0, RoundingMode.DOWN)}"
}


inline fun <T> Iterable<T>.sumOf(selector: (T) -> Money): Money {
    return this.fold(Money.ZERO) { acc, element ->
        acc + selector(element)
    }
}

fun Iterable<Money>.sum(): Money {
    return this.fold(Money.ZERO) { acc, m -> acc + m }
}