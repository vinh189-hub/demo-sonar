package org.example

import jakarta.enterprise.context.ApplicationScoped

data class OrderItem(val name: String, val price: Double, val quantity: Int)

data class OrderResult(
    val subtotal: Double,
    val discount: Double,
    val tax: Double,
    val total: Double
)

@ApplicationScoped
class OrderService {

    companion object {
        const val TAX_RATE = 0.1
        const val DISCOUNT_THRESHOLD = 500.0
        const val DISCOUNT_RATE = 0.05
    }

    fun calculate(items: List<OrderItem>): OrderResult {
        require(items.isNotEmpty()) { "Order must have at least one item" }
        items.forEach { item ->
            require(item.price >= 0) { "Price must not be negative: ${item.name}" }
            require(item.quantity > 0) { "Quantity must be positive: ${item.name}" }
        }

        val subtotal = items.sumOf { it.price * it.quantity }
        val discount = if (subtotal >= DISCOUNT_THRESHOLD) subtotal * DISCOUNT_RATE else 0.0
        val tax = (subtotal - discount) * TAX_RATE
        val total = subtotal - discount + tax

        return OrderResult(
            subtotal = subtotal,
            discount = discount,
            tax = tax,
            total = total
        )
    }
}
