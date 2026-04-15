package org.example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class OrderServiceTest {

    private lateinit var service: OrderService

    @BeforeEach
    fun setUp() {
        service = OrderService()
    }

    // ── Happy paths ──────────────────────────────────────────────────────────

    @Test
    fun `calculate single item without discount`() {
        val items = listOf(OrderItem("Book", price = 100.0, quantity = 2))

        val result = service.calculate(items)

        // subtotal = 200, discount = 0 (< 500), tax = 20, total = 220
        assertAll(
            { assertEquals(200.0, result.subtotal) },
            { assertEquals(0.0, result.discount) },
            { assertEquals(20.0, result.tax) },
            { assertEquals(220.0, result.total) }
        )
    }

    @Test
    fun `calculate multiple items with discount applied when subtotal meets threshold`() {
        val items = listOf(
            OrderItem("Laptop", price = 400.0, quantity = 1),
            OrderItem("Mouse", price = 100.0, quantity = 2)  // subtotal = 600
        )

        val result = service.calculate(items)

        // subtotal = 600, discount = 30 (5%), tax = 57, total = 627
        assertAll(
            { assertEquals(600.0, result.subtotal) },
            { assertEquals(30.0, result.discount) },
            { assertEquals(57.0, result.tax, 0.001) },
            { assertEquals(627.0, result.total, 0.001) }
        )
    }

    @Test
    fun `no discount when subtotal is exactly at threshold`() {
        // 500.0 exactly should trigger discount
        val items = listOf(OrderItem("Bag", price = 250.0, quantity = 2))

        val result = service.calculate(items)

        assertEquals(25.0, result.discount)
    }

    @Test
    fun `no discount when subtotal is just below threshold`() {
        val items = listOf(OrderItem("Pen", price = 49.9, quantity = 10)) // subtotal = 499

        val result = service.calculate(items)

        assertEquals(0.0, result.discount)
    }

    @Test
    fun `calculate with quantity greater than one`() {
        val items = listOf(OrderItem("Notebook", price = 10.0, quantity = 5))

        val result = service.calculate(items)

        assertEquals(50.0, result.subtotal)
    }

    // ── Validation errors ────────────────────────────────────────────────────

    @Test
    fun `throw when item list is empty`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            service.calculate(emptyList())
        }
        assertEquals("Order must have at least one item", ex.message)
    }

    @Test
    fun `throw when item price is negative`() {
        val items = listOf(OrderItem("Broken", price = -1.0, quantity = 1))

        val ex = assertThrows(IllegalArgumentException::class.java) {
            service.calculate(items)
        }
        assertEquals("Price must not be negative: Broken", ex.message)
    }

    @Test
    fun `throw when item quantity is zero`() {
        val items = listOf(OrderItem("Ghost", price = 10.0, quantity = 0))

        val ex = assertThrows(IllegalArgumentException::class.java) {
            service.calculate(items)
        }
        assertEquals("Quantity must be positive: Ghost", ex.message)
    }

    @Test
    fun `throw when item quantity is negative`() {
        val items = listOf(OrderItem("Ghost", price = 10.0, quantity = -3))

        val ex = assertThrows(IllegalArgumentException::class.java) {
            service.calculate(items)
        }
        assertEquals("Quantity must be positive: Ghost", ex.message)
    }

    @Test
    fun `allow zero price item`() {
        val items = listOf(OrderItem("Free sample", price = 0.0, quantity = 1))

        val result = service.calculate(items)

        assertEquals(0.0, result.total)
    }
}
