package mhk.batch03

import mhk.entity.ReadJpaEntity
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import javax.sql.DataSource

@Configuration
class Batch03(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val dataSource: DataSource,
) {
    @Bean
    fun batch03Job(): Job {
        return JobBuilder("batch03Job", jobRepository)
            .start(batch03Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch03Step(): Step {
        return StepBuilder("batch03Step", jobRepository)
            .chunk<ReadJpaEntity, ReadJpaEntity>(CHUNK_SIZE, transactionManager)
            .reader(batch03JdbcPagingReader())
            .writer(batch03Writer())
            .build()
    }

    @Bean
    fun batch03JdbcPagingReader(): JdbcPagingItemReader<ReadJpaEntity> {
        val birthday: LocalDate? = LocalDate.of(2010, 1, 1)

        val factory = SqlPagingQueryProviderFactoryBean()
        factory.setDataSource(dataSource)
        factory.setSelectClause("SELECT id, name, birthday, address")
        factory.setFromClause("FROM batch_study_data_entity")

        var whereSql = "WHERE 1=1 "
        val parameters = mutableMapOf<String, Any>()

        birthday?.let {
            whereSql += "AND birthday > :birthday "
            parameters["birthday"] = birthday
        }

        factory.setWhereClause(whereSql)
        factory.setSortKey("id")

        return JdbcPagingItemReaderBuilder<ReadJpaEntity>()
            .name("batch03JdbcPagingReader")
            .rowMapper(DataClassRowMapper(ReadJpaEntity::class.java))
            .queryProvider(factory.getObject())
            .parameterValues(parameters)
            .dataSource(dataSource)
            .pageSize(CHUNK_SIZE)
            .fetchSize(CHUNK_SIZE)
            .build()
    }

    @Bean
    @StepScope
    fun batch03Writer(): ItemWriter<ReadJpaEntity> {
        return ItemWriter {
            for (readJpaEntity in it) {
                println(readJpaEntity)
            }
        }
    }

    companion object {
        private const val CHUNK_SIZE = 10000
    }
}
