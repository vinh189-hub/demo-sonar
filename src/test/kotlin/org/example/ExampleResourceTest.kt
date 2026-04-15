package org.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test

@QuarkusTest
class ExampleResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
            .`when`().get("/hello")
            .then()
            .statusCode(200)
            .body(`is`("Hello from Quarkus REST"))
    }

    @Test
    fun `discount endpoint applies 10 percent discount when amount is above threshold`() {
        given()
            .`when`().get("/hello/discount/150.0")
            .then()
            .statusCode(200)
            .body(containsString("Amount: 150.0"))
            .body(containsString("Discount: 15.0"))
            .body(containsString("Total: 135.0"))
    }

    @Test
    fun `discount endpoint returns no discount when amount is below threshold`() {
        given()
            .`when`().get("/hello/discount/50.0")
            .then()
            .statusCode(200)
            .body(containsString("Amount: 50.0"))
            .body(containsString("Discount: 0.0"))
            .body(containsString("Total: 50.0"))
    }

}