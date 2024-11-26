package mhk.config

import io.restassured.RestAssured
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class AcceptanceTestExecutionListener : AbstractTestExecutionListener() {

    override fun beforeTestClass(testContext: TestContext) {
        val serverPort = testContext.applicationContext.environment.getProperty("local.server.port", Int::class.java) ?: throw IllegalStateException("localServerPort cannot be null")
        RestAssured.port = serverPort
    }
}
