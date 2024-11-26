package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.math.BigDecimal

fun handleFailure(throwable: Throwable): String {
    return throwable.message ?: ""
}

fun handleSuccess(payStatus: PayStatus): PayStatus.PayAccountingTreatment {
    return payStatus as PayStatus.PayAccountingTreatment
}

class Test10EitherApplicationTests : FunSpec({
    context("결제 테스트") {
        val payService = PayService()

        test("결제를 성공하면 회계처리까지 완료된다") {
            val payRequest = PayRequest(orderId = 1L, amount = BigDecimal.valueOf(10000), paymentMethod = "CARD")

            val payResponse = payService.validate(payRequest)
                .next<PayStatus.PayValidated, PayStatus.PayDeposited> {
                    payService.deposit(it)
                }.next<PayStatus.PayDeposited, PayStatus.PayReceipted> {
                    payService.receipt(it)
                }.next<PayStatus.PayReceipted, PayStatus.PayAccountingTreatment> {
                    payService.accountingTreatment(it)
                }.fold(
                    ::handleFailure, ::handleSuccess
                )

            payResponse.javaClass shouldBeSameInstanceAs PayStatus.PayAccountingTreatment::class.java
        }

        test("결제를 실패하면 실패위치의 메세지가 나온다") {
            val payRequest = PayRequest(orderId = 1L, amount = BigDecimal.valueOf(-1), paymentMethod = "CARD")

            val payResponse = payService.validate(payRequest)
                .next<PayStatus.PayValidated, PayStatus.PayDeposited> {
                    payService.deposit(it)
                }.next<PayStatus.PayDeposited, PayStatus.PayReceipted> {
                    payService.receipt(it)
                }.next<PayStatus.PayReceipted, PayStatus.PayAccountingTreatment> {
                    payService.accountingTreatment(it)
                }.fold(
                    ::handleFailure, ::handleSuccess
                )

            payResponse shouldBe "입금 금액은 0보다 커야함"
        }
    }
})
