package mhk.ticket

data class Ticket(
    val id: Long,
    var status: Status
) {
    enum class Status {
        ON_SALE, SOLD_OUT
    }
}
