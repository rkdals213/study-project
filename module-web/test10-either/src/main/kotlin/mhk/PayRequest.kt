package mhk

import java.math.BigDecimal

data class PayRequest(
    val orderId: Long,
    val amount: BigDecimal,
    val paymentMethod: String
)
