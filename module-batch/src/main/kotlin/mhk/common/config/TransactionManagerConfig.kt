package mhk.common.config

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.orm.jpa.JpaTransactionManager
import javax.sql.DataSource

@Configuration
class TransactionManagerConfig {

    @Bean
    @Primary
    fun jpaTransactionManager(dataSource: DataSource): JpaTransactionManager {
        return JpaTransactionManager().apply {
            this.dataSource = dataSource
        }
    }

    @Bean
    fun exposedTransactionManager(dataSource: DataSource): SpringTransactionManager {
        return SpringTransactionManager(dataSource)
    }

    @Bean
    fun transactionManager(dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

}
