package mhk.batch01

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManagerFactory
import mhk.entity.QWriteJpaEntity.writeJpaEntity
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
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.DataClassRowMapper
import javax.sql.DataSource

@Configuration
class Batch01(
    private val jobRepository: JobRepository,
//    private val transactionManager: PlatformTransactionManager,
    private val transactionManager: SpringTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dataSource: DataSource,
    private val queryFactory: JPAQueryFactory
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
            .chunk<ReadJpaEntity, WriteJpaEntity>(CHUNK_SIZE, transactionManager)
            .reader(batch01JdbcCursorReader())
            .processor(batch01Processor())
//            .writer(batch01JpaWriter())
//            .writer(batch01JdbcWriter())
//            .writer(batch01QuerydslWriter())
            .writer(batch01ExposedWriter())
            .build()
    }

    @Bean
    fun batch01JdbcCursorReader(): JdbcCursorItemReader<ReadJpaEntity> {
        return JdbcCursorItemReaderBuilder<ReadJpaEntity>()
            .name("batch01Reader")
            .rowMapper(DataClassRowMapper(ReadJpaEntity::class.java))
            .sql("SELECT id, name, birthday, address FROM batch_study_data_entity b WHERE id <= 100000")
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun batch01Processor(): ItemProcessor<ReadJpaEntity, WriteJpaEntity> {
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
    fun batch01JpaWriter(): JpaItemWriter<WriteJpaEntity> {
        return JpaItemWriterBuilder<WriteJpaEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }

    @Bean // 30초
    fun batch01QuerydslWriter(): ItemWriter<WriteJpaEntity> {
        return ItemWriter { list ->
            for (entity in list) {
                queryFactory.insert(writeJpaEntity)
                    .columns(writeJpaEntity.name, writeJpaEntity.birthday, writeJpaEntity.address)
                    .values(entity.name, entity.birthday, entity.address)
                    .execute()
            }
        }
    }

    @Bean // 3초
    fun batch01JdbcWriter(): JdbcBatchItemWriter<WriteJpaEntity> {
        return JdbcBatchItemWriterBuilder<WriteJpaEntity>()
            .dataSource(dataSource)
            .sql("insert into batch_study_data_entity2 (name, birthday, address) values (:name, :birthday, :address)")
            .beanMapped()
            .build()
    }

    @Bean // 3초
    fun batch01ExposedWriter(): ItemWriter<WriteJpaEntity> {
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
