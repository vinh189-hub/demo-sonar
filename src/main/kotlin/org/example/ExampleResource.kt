package org.example

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource {

    @Inject
    lateinit var orderService: OrderService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello from Quarkus REST"

    @GET
    @Path("/discount/{amount}")
    @Produces(MediaType.TEXT_PLAIN)
    fun discount(@PathParam("amount") amount: Double): String {
        val discount = orderService.calculateDiscount(amount)
        return "Amount: $amount, Discount: $discount, Total: ${amount - discount}"
    }
}