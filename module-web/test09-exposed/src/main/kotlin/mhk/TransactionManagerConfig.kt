package mhk
//
//import org.jetbrains.exposed.spring.SpringTransactionManager
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.jdbc.datasource.DataSourceTransactionManager
//import org.springframework.orm.jpa.JpaTransactionManager
//import org.springframework.transaction.PlatformTransactionManager
//import javax.sql.DataSource
//
//@Configuration
//class TransactionManagerConfig {
//
//    @Primary
//    @Bean(name = ["jpaTransactionManager"])
//    fun jpaTransactionManager(dataSource: DataSource): JpaTransactionManager {
//        return JpaTransactionManager().apply {
//            this.dataSource = dataSource
//        }
//    }
//
//    @Bean(name = ["exposedTransactionManager"])
//    fun exposedTransactionManager(dataSource: DataSource): SpringTransactionManager {
//        return SpringTransactionManager(dataSource)
//    }
//
//    @Bean(name = ["transactionManager"])
//    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
//        return DataSourceTransactionManager(dataSource)
//    }
//
//}
