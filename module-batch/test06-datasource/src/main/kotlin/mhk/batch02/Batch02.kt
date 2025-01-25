package mhk.batch02

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class Batch02(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
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
        return StepBuilder("batch02Step", jobRepository)
            .chunk<ReadData, WriteData>(CHUNK_SIZE, transactionManager)
            .reader(batch02JdbcCursorReader(null))
            .processor(batch02Processor())
            .writer(batch02JdbcWriter(null))
            .build()
    }

    @Bean
    fun batch02JdbcCursorReader(
        @Qualifier("readDataSource") dataSource: DataSource?,
    ): JdbcCursorItemReader<ReadData> {
        return JdbcCursorItemReaderBuilder<ReadData>()
            .name("batch02JdbcCursorReader")
            .rowMapper(DataClassRowMapper(ReadData::class.java))
            .sql("SELECT id, data FROM read_only_entity")
            .dataSource(dataSource!!)
            .build()
    }

    @Bean
    fun batch02Processor(): ItemProcessor<ReadData, WriteData> {
        return ItemProcessor {
            WriteData(
                id = 0,
                data = it.data
            )
        }
    }

    @Bean // 3초
    fun batch02JdbcWriter(
        @Qualifier("writeDataSource") dataSource: DataSource?,
    ): JdbcBatchItemWriter<WriteData> {
        return JdbcBatchItemWriterBuilder<WriteData>()
            .dataSource(dataSource!!)
            .sql("insert into write_entity (data) values (:data)")
            .beanMapped()
            .build()
    }

    companion object {
        private const val CHUNK_SIZE = 10
    }
}
