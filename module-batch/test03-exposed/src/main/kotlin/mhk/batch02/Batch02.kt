package mhk.batch02

import jakarta.persistence.EntityManagerFactory
import mhk.entity.ReadJpaEntity
import mhk.entity.WriteJpaEntity
import mhk.common.exposed.WriteExposedEntity
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.batchInsert
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.DataClassRowMapper
import javax.sql.DataSource

@Configuration
class Batch02(
    private val jobRepository: JobRepository,
//    private val transactionManager: PlatformTransactionManager,
    private val transactionManager: SpringTransactionManager,
//    private val transactionManager: JpaTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dataSource: DataSource
) {

    @Bean
    fun batch02Job(): Job {
        return JobBuilder("batch02Job", jobRepository)
            .start(batch02Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch02Step(): Step {
        println("transactionManager java class : " + transactionManager.javaClass)

        return StepBuilder("batch02Step", jobRepository)
            .chunk<ReadJpaEntity, ReadJpaEntity>(CHUNK_SIZE, transactionManager)
//            .chunk<ReadJpaEntity, WriteJpaEntity>(CHUNK_SIZE, transactionManager)
            .reader(batch02JpaPagingReader())
//            .reader(batch02JdbcCursorReader())
//            .processor(batch02Processor())
//            .writer(batch02JpaWriter())
//            .writer(batch02JdbcWriter())
            .writer(batch02ExposedWriter())
            .build()
    }

    @Bean
    fun batch02JdbcCursorReader(): JdbcCursorItemReader<ReadJpaEntity> {
        return JdbcCursorItemReaderBuilder<ReadJpaEntity>()
            .name("batch02Reader")
            .rowMapper(DataClassRowMapper(ReadJpaEntity::class.java))
            .sql("SELECT id, name, birthday, address FROM batch_study_data_entity b WHERE id <= 10")
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun batch02JpaPagingReader(): JpaPagingItemReader<ReadJpaEntity> {
        return JpaPagingItemReaderBuilder<ReadJpaEntity>()
            .name("batch02JpaPagingReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM batch_study_data_entity b WHERE id <= 10")
            .build()
    }

    @Bean
    fun batch02Processor(): ItemProcessor<ReadJpaEntity, WriteJpaEntity> {
        return ItemProcessor {
            WriteJpaEntity(
                id = 0,
                name = it.name,
                birthday = it.birthday,
                address = it.address
            )
        }
    }

    @Bean // 30초
    fun batch02JpaWriter(): JpaItemWriter<WriteJpaEntity> {
        return JpaItemWriterBuilder<WriteJpaEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }

    @Bean // 3초
    fun batch02JdbcWriter(): JdbcBatchItemWriter<WriteJpaEntity> {
        return JdbcBatchItemWriterBuilder<WriteJpaEntity>()
            .dataSource(dataSource)
            .sql("insert into batch_study_data_entity2 (name, birthday, address) values (:name, :birthday, :address)")
            .beanMapped()
            .build()
    }

    @Bean // 3초
    fun batch02ExposedWriter(): ItemWriter<ReadJpaEntity> {
        return ItemWriter { list ->
            WriteExposedEntity.batchInsert(list) {
                this[WriteExposedEntity.name] = it.name
                this[WriteExposedEntity.birthday] = it.birthday
                this[WriteExposedEntity.address] = it.address
            }
        }
    }

    companion object {
        private const val CHUNK_SIZE = 10000
    }
}

