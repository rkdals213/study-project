package mhk.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
class DataSourceConfig {

    @Primary
    @Bean(name = ["writeDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.write")
    fun writeDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean(name = ["readDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.read")
    fun readDataSource(): DataSource = DataSourceBuilder.create().build()
}
