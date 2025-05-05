package mhk

fun adjustVatToMatchTotal(details: List<ChargeLine>) {
    // 대상 항목 필터링
    val vatApplicableItems = details.filter { it.vatType == "1" }

    val totalVat = vatApplicableItems.sumOf { it.amount } / 10

    if (vatApplicableItems.isEmpty()) return

    val currentVatSum = vatApplicableItems.sumOf { it.vat }
    var correction = totalVat - currentVatSum

    if (correction.isZero()) return

    val unit = if (correction.isPositive()) Money(1) else Money(-1)

    var index = 0
    while (!correction.isZero()) {
        vatApplicableItems[index].addVatCorrection(unit)
        correction -= unit
        index = (index + 1) % vatApplicableItems.size
    }
}