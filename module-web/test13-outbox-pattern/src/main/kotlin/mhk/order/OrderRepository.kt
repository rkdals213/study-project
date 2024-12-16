package mhk.order

import org.springframework.stereotype.Repository

@Repository
class OrderRepository {
    private val orders: MutableList<Order> = mutableListOf()

    fun getOrder(orderId: String) = orders.first { it.id == orderId }

    fun registerOrder(order: Order) = orders.add(order)
}
