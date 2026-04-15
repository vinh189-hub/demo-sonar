package org.example

import jakarta.enterprise.context.ApplicationScoped

data class OrderItem(val name: String, val price: Double, val quantity: Int)

data class OrderResult(val subtotal: Double, val discount: Double, val total: Double)

@ApplicationScoped
class OrderService {

    companion object {
        const val DISCOUNT_THRESHOLD = 100.0
        const val DISCOUNT_RATE = 0.10
    }

    fun calculateDiscount(subtotal: Double): Double {
        return if (subtotal >= DISCOUNT_THRESHOLD) subtotal * DISCOUNT_RATE else 0.0
    }

    fun processOrder(items: List<OrderItem>): OrderResult {
        val subtotal = items.sumOf { it.price * it.quantity }
        val discount = calculateDiscount(subtotal)
        return OrderResult(subtotal, discount, subtotal - discount)
    }
}
