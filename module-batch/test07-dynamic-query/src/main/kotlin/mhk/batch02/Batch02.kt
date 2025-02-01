package mhk.batch02

import jakarta.persistence.EntityManagerFactory
import mhk.QuerydslNoOffsetPagingItemReader
import mhk.entity.QReadJpaEntity.readJpaEntity
import mhk.entity.ReadJpaEntity
import mhk.expression.Expression
import mhk.options.QuerydslNoOffsetNumberOptions
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.util.function.Function

@Configuration
class Batch02(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory
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
            .reader(batch02QueryDslPagingReader())
            .writer(batch02Writer())
            .build()
    }

    @Bean
    fun batch02QueryDslPagingReader(): QuerydslNoOffsetPagingItemReader<ReadJpaEntity> {
        val birthday: LocalDate? = LocalDate.of(2010, 1, 1)

        return QuerydslNoOffsetPagingItemReaderBuilder<ReadJpaEntity> {
            enf = entityManagerFactory
            options = QuerydslNoOffsetNumberOptions(readJpaEntity.id, Expression.ASC)
            pageSize = CHUNK_SIZE
            queryFunction = Function { jpaQueryFactory ->
                jpaQueryFactory.query()
                    .select(readJpaEntity)
                    .from(readJpaEntity)
                    .where(
                        birthday?.let { readJpaEntity.birthday.gt(it) }
                    )
            }
        }.build()
    }

    @Bean
    @StepScope
    fun batch02Writer(): ItemWriter<ReadJpaEntity> {
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
