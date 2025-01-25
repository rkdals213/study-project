package mhk.common

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "writerEntityManagerFactory"
)
class JpaEntityManagerConfig {

    @Bean(name = ["writerEntityManagerFactory"])
    fun writerEntityManagerFactory(
        @Qualifier("writeDataSource") dataSource: DataSource,
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages("mhk.batch01")
            .persistenceUnit("writer")
            .build()
    }

    @Primary
    @Bean(name = ["writerTransactionManager"])
    fun writerTransactionManager(
        @Qualifier("writerEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }


    @Bean(name = ["readerEntityManagerFactory"])
    fun readerEntityManagerFactory(
        @Qualifier("readDataSource") dataSource: DataSource,
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages("mhk.batch01")
            .persistenceUnit("reader")
            .build()
    }

    @Bean(name = ["readerTransactionManager"])
    fun readerTransactionManager(
        @Qualifier("readerEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
