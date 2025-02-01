package mhk.batch01

import jakarta.persistence.EntityManagerFactory
import mhk.entity.ReadJpaEntity
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate

@Configuration
class Batch01(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory
) {
    @Bean
    fun batch01Job(): Job {
        return JobBuilder("batch01Job", jobRepository)
            .start(batch01Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch01Step(): Step {
        return StepBuilder("batch01Step", jobRepository)
            .chunk<ReadJpaEntity, ReadJpaEntity>(CHUNK_SIZE, transactionManager)
            .reader(batch01JpaPagingReader())
            .writer(batch01Writer())
            .build()
    }

    @Bean
    fun batch01JpaPagingReader(): JpaPagingItemReader<ReadJpaEntity> {
        val birthday: LocalDate? = LocalDate.of(2010, 1, 1)

        var sql = "SELECT b FROM batch_study_data_entity b WHERE 1=1 "
        val parameters = mutableMapOf<String, Any>()

        birthday?.let {
            parameters["birthday"] = birthday
            sql += "AND b.birthday > :birthday "
        }

        return JpaPagingItemReaderBuilder<ReadJpaEntity>()
            .name("batch01JpaPagingReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .queryString(sql)
            .parameterValues(parameters)
            .build()
    }

    @Bean
    @StepScope
    fun batch01Writer(): ItemWriter<ReadJpaEntity> {
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
