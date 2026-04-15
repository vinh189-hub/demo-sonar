package org.example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderServiceTest {

    private val service = OrderService()

    @Test
    fun `calculateDiscount returns 10 percent when amount exceeds threshold`() {
        assertEquals(15.0, service.calculateDiscount(150.0), 0.001)
    }

    @Test
    fun `calculateDiscount returns zero when amount is below threshold`() {
        assertEquals(0.0, service.calculateDiscount(50.0), 0.001)
    }

    @Test
    fun `calculateDiscount returns 10 percent when amount equals threshold`() {
        assertEquals(10.0, service.calculateDiscount(100.0), 0.001)
    }

    @Test
    fun `processOrder applies discount when subtotal is above threshold`() {
        val items = listOf(OrderItem("Book", 60.0, 2))
        val result = service.processOrder(items)

        assertEquals(120.0, result.subtotal, 0.001)
        assertEquals(12.0, result.discount, 0.001)
        assertEquals(108.0, result.total, 0.001)
    }

    @Test
    fun `processOrder has no discount when subtotal is below threshold`() {
        val items = listOf(OrderItem("Pen", 5.0, 3))
        val result = service.processOrder(items)

        assertEquals(15.0, result.subtotal, 0.001)
        assertEquals(0.0, result.discount, 0.001)
        assertEquals(15.0, result.total, 0.001)
    }

    @Test
    fun `processOrder calculates subtotal correctly for multiple items`() {
        val items = listOf(
            OrderItem("Laptop", 500.0, 1),
            OrderItem("Mouse", 25.0, 2)
        )
        val result = service.processOrder(items)

        assertEquals(550.0, result.subtotal, 0.001)
        assertEquals(55.0, result.discount, 0.001)
        assertEquals(495.0, result.total, 0.001)
    }
}
