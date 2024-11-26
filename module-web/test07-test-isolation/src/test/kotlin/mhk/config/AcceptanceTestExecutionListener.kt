package mhk.config

import io.restassured.RestAssured
import jakarta.persistence.EntityManager
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate

class AcceptanceTestExecutionListener : AbstractTestExecutionListener() {

    override fun beforeTestClass(testContext: TestContext) {
        val serverPort = testContext.applicationContext.environment.getProperty("local.server.port", Int::class.java) ?: throw IllegalStateException("localServerPort cannot be null")
        RestAssured.port = serverPort
    }

    override fun beforeTestMethod(testContext: TestContext) {
        clearDatabaseWithJPA(testContext)
    }

    private fun clearDatabaseWithJPA(testContext: TestContext) {
        val transactionTemplate = testContext.applicationContext.getBean(TransactionTemplate::class.java)

        println("asdasdasd" + transactionTemplate.transactionManager?.javaClass)

        val entityManager = testContext.applicationContext.getBean(EntityManager::class.java)

        transactionTemplate.execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                val tableNames = getTableNames(entityManager)

                entityManager.flush()
                entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
                for (tableName in tableNames) {
                    entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
                    entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
                }
                entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
            }
        })
    }

    private fun getTableNames(entityManager: EntityManager): List<String> {
        return entityManager.createNativeQuery("SELECT CONCAT(TABLE_SCHEMA, '.', TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA IN ('KOTLIN_STUDY', 'PUBLIC')")
            .resultList
            .map { it.toString() }
    }
}
