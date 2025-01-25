package mhk.batch01

import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class Batch01(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {

    @Bean
    fun batch01Job(
        batch01Step: Step
    ): Job {
        return JobBuilder("batch01Job", jobRepository)
            .start(batch01Step)
            .build()
    }

    @Bean
    @JobScope
    fun batch01Step(
        batch01Reader: JpaPagingItemReader<ReadOnlyEntity>,
        batch01Processor: ItemProcessor<ReadOnlyEntity, WriteEntity>,
        batch01Writer: JpaItemWriter<WriteEntity>,
    ): Step {
        return StepBuilder("batch01Step", jobRepository)
            .chunk<ReadOnlyEntity, WriteEntity>(CHUNK_SIZE, transactionManager)
            .reader(batch01Reader)
            .processor(batch01Processor)
            .writer(batch01Writer)
            .build()
    }

    @Bean
    @StepScope
    fun batch01Reader(
        @Qualifier("readerEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): JpaPagingItemReader<ReadOnlyEntity> {
        return JpaPagingItemReaderBuilder<ReadOnlyEntity>()
            .name("batch01JpaPagingReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT a FROM ReadOnlyEntity a")
            .build()
    }

    @Bean
    @StepScope
    fun batch01Processor(): ItemProcessor<ReadOnlyEntity, WriteEntity> {
        return ItemProcessor {
            WriteEntity(
                id = 0,
                data = it.data
            )
        }
    }

    @Bean
    @StepScope
    fun batch01Writer(
        @Qualifier("writerEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): JpaItemWriter<WriteEntity> {
        return JpaItemWriterBuilder<WriteEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }

    companion object {
        private const val CHUNK_SIZE = 10
    }
}
