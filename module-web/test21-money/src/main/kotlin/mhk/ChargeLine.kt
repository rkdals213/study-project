package mhk

class ChargeLine(
    val rateCode: String,
    val amount: Money,
    var vat: Money,
    val vatType: String
) {
    fun addVatCorrection(unit: Money) {
        vat += unit
    }

    override fun toString(): String {
        return "ChargeLine(rateCode='$rateCode', amount=$amount, vat=$vat, vatType='$vatType')"
    }
}
