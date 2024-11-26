package mhk.batch02

import jakarta.persistence.EntityManagerFactory
import mhk.entity.QReadJpaEntity.readJpaEntity
import mhk.entity.ReadJpaEntity
import mhk.QuerydslNoOffsetPagingItemReader
import mhk.common.jpa.QuerydslNoOffsetPagingItemReaderBuilder
import mhk.expression.Expression
import mhk.options.QuerydslNoOffsetNumberOptions
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.transaction.PlatformTransactionManager
import java.util.function.Function
import javax.sql.DataSource

@Configuration
class Batch02(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val dataSource: DataSource,
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
            .chunk<ReadJpaEntity, ReadJpaEntity>(CHUNK_SIZE, transactionManager)
//            .reader(batch02JpaPagingReader())
//            .reader(batch02JdbcCursorReader())
            .reader(batch02QueryDslPagingReader())
            .writer(batch02Writer())
            .build()
    }

    @Bean
    fun batch02JpaPagingReader(): JpaPagingItemReader<ReadJpaEntity> {
        return JpaPagingItemReaderBuilder<ReadJpaEntity>()
            .name("batch02JpaPagingReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT b FROM batch_study_data_entity b")
            .build()
    }

    @Bean
    fun batch02JdbcCursorReader(): JdbcCursorItemReader<ReadJpaEntity> {
        return JdbcCursorItemReaderBuilder<ReadJpaEntity>()
            .name("batch02JdbcCursorReader")
            .rowMapper(DataClassRowMapper(ReadJpaEntity::class.java))
            .sql("SELECT id, name, birthday, address FROM batch_study_data_entity b order by id desc")
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun batch02QueryDslPagingReader(): QuerydslNoOffsetPagingItemReader<ReadJpaEntity> {
        return QuerydslNoOffsetPagingItemReaderBuilder<ReadJpaEntity> {
            enf = entityManagerFactory
            options = QuerydslNoOffsetNumberOptions(readJpaEntity.id, Expression.DESC)
            pageSize = CHUNK_SIZE
            queryFunction = Function {
                it.query()
                    .select(readJpaEntity)
                    .from(readJpaEntity)
            }
        }.build()
    }

    @Bean
    fun batch02Writer(): FlatFileItemWriter<ReadJpaEntity> {
        val extractor = BeanWrapperFieldExtractor<ReadJpaEntity>()
        extractor.setNames(arrayOf("id", "name", "birthday", "address"))

        val aggregator = DelimitedLineAggregator<ReadJpaEntity>()
        aggregator.setDelimiter(",")
        aggregator.setFieldExtractor(extractor)

        return FlatFileItemWriterBuilder<ReadJpaEntity>()
            .name("batch02Writer")
            .encoding("UTF-8")
            .resource(FileSystemResource("output/test.csv"))
            .lineAggregator(aggregator)
            .build()
    }

    companion object {
        private const val CHUNK_SIZE = 100000
    }
}


