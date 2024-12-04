package mhk

import arrow.core.Either
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import mhk.flow.FlowFailureStatus
import mhk.flow.FlowService
import mhk.flow.FlowSuccessStatus
import mhk.flow.nextFlow
import mhk.pay.PayRequest
import mhk.pay.PayService
import mhk.pay.PayStatus
import mhk.pay.next
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

    context("flow 테스트") {
        val flowService = FlowService()

        test("flow 진행") {
            flowService.flow01(3)
                .nextFlow<FlowSuccessStatus.Flow01Success, FlowSuccessStatus.Flow02Success> {
                    flowService.flow02(it) as Either<FlowSuccessStatus.Flow01Success, FlowSuccessStatus.Flow02Success>
                }.nextFlow<FlowSuccessStatus.Flow02Success, FlowSuccessStatus.Flow03Success> {
                    flowService.flow03(it) as Either<FlowSuccessStatus.Flow02Success, FlowSuccessStatus.Flow03Success>
                }.fold(
                    ifLeft = {
                        println("LEFT : $it")
                    },
                    ifRight = {
                        println("RIGHT : $it")
                    })
        }
    }
})
