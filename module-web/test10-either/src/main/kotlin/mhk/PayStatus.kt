package mhk

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import java.math.BigDecimal

sealed class PayStatus {
    data class PayValidated(
        val payRequest: PayRequest
    ) : PayStatus()

    data class PayDeposited(
        val txId: String
    ) : PayStatus()

    data class PayReceipted(
        val txId: String
    ) : PayStatus()

    data class PayAccountingTreatment(
        val txId: String,
        val orderId: Long,
        val amount: BigDecimal,
        val payMethod: String
    ) : PayStatus()
}

inline fun <reified T : PayStatus, R : PayStatus> Either<Throwable, PayStatus>.next(
    action: (T) -> Either<Throwable, R>
): Either<Throwable, PayStatus> {
    return flatMap {
        when (it) {
            is T -> action(it)
            else -> it.right()
        }
    }
}
