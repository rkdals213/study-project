package mhk

import arrow.core.Either
import java.math.BigDecimal
import java.util.*

class PayService {
    private var uuid: UUID = UUID.randomUUID()
    private var repository: MutableMap<String, PayRequest> = mutableMapOf()

    fun validate(payRequest: PayRequest): Either<Throwable, PayStatus.PayValidated> {
        return if (payRequest.amount < BigDecimal.ZERO) {
            Either.Left(IllegalArgumentException("입금 금액은 0보다 커야함"))
        } else {
            println("검증 완료 : orderId: ${payRequest.orderId}, amount: ${payRequest.amount}")
            Either.Right(PayStatus.PayValidated(payRequest))
        }
    }

    fun deposit(payValidated: PayStatus.PayValidated): Either<Throwable, PayStatus.PayDeposited> {
        repository[uuid.toString()] = payValidated.payRequest

        println("입금 완료 : orderId: ${payValidated.payRequest.orderId}, amount: ${payValidated.payRequest.amount}")

        return Either.Right(PayStatus.PayDeposited(uuid.toString()))
    }

    fun receipt(payDeposited: PayStatus.PayDeposited): Either<Throwable, PayStatus.PayReceipted> {
        val payRequest = repository[payDeposited.txId]
            ?: return Either.Left(NoSuchElementException("txId ${payDeposited.txId} not found"))

        println("수납 완료 : orderId: ${payRequest.orderId}, amount: ${payRequest.amount}")

        return Either.Right(PayStatus.PayReceipted(payDeposited.txId))
    }

    fun accountingTreatment(payReceipted: PayStatus.PayReceipted): Either<Throwable, PayStatus.PayAccountingTreatment> {
        val payRequest = repository[payReceipted.txId]
            ?: return Either.Left(NoSuchElementException("txId ${payReceipted.txId} not found"))

        println("회계 처리 완료 : orderId: ${payRequest.orderId}, amount: ${payRequest.amount}")

        return Either.Right(
            PayStatus.PayAccountingTreatment(
                txId = payReceipted.txId,
                orderId = payRequest.orderId,
                amount = payRequest.amount,
                payMethod = payRequest.paymentMethod
            )
        )
    }
}
