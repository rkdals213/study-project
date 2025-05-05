package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Test21MoneyApplicationTests : FunSpec({

    context("Money") {
        test("청구금액의 합") {
            val money = listOf(
                ChargeLine("RI001", Money(1000), Money(100), "1"),
                ChargeLine("RI002", Money(2000), Money(200), "2"),
                ChargeLine("RI003", Money(3000), Money(300), "1"),
                ChargeLine("RI004", Money(4000), Money(400), "2"),
                ChargeLine("RI005", Money(5000), Money(500), "1"),
            )

            val sumOf = money.sumOf { it.amount }
            val sum = money.map { it.amount }.sum()

            sumOf shouldBe Money(15000)
            sum shouldBe Money(15000)
        }

        test("부가세 계산") {
            val money = listOf(
                ChargeLine("RI001", Money(254), Money(25), "1"),
                ChargeLine("RI002", Money(2000), Money(200), "2"),
                ChargeLine("RI003", Money(334), Money(33), "1"),
                ChargeLine("RI004", Money(4000), Money(400), "2"),
                ChargeLine("RI005", Money(444), Money(44), "1"),
            )

            adjustVatToMatchTotal(money)

            val totalVat = money.filter { it.vatType == "1" }.sumOf { it.amount } / 10
            val sumOfVat = money.filter { it.vatType == "1" }.sumOf { it.vat }

            sumOfVat shouldBe totalVat
        }
    }
})