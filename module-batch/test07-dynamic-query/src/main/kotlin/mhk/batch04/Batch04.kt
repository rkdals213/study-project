package mhk.batch04

import mhk.entity.ReadJpaEntity
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.annotation.MapperScan
import org.mybatis.spring.batch.MyBatisPagingItemReader
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder
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

@Configuration
@MapperScan(basePackages = ["mhk.batch04"])
class Batch04(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val sqlSessionFactory: SqlSessionFactory
) {
    @Bean
    fun batch04Job(): Job {
        return JobBuilder("batch04Job", jobRepository)
            .start(batch04Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch04Step(): Step {
        return StepBuilder("batch04Step", jobRepository)
            .chunk<ReadJpaEntity, ReadJpaEntity>(CHUNK_SIZE, transactionManager)
            .reader(batch04MybatisPagingReader())
            .writer(batch04Writer())
            .build()
    }

    @Bean
    fun batch04MybatisPagingReader(): MyBatisPagingItemReader<ReadJpaEntity> {
        val birthday: LocalDate? = LocalDate.of(2010, 1, 1)

        val parameters = mutableMapOf<String, Any>()

        birthday?.let {
            parameters["birthday"] = birthday
        }

        return MyBatisPagingItemReaderBuilder<ReadJpaEntity>()
            .sqlSessionFactory(sqlSessionFactory)
            .pageSize(CHUNK_SIZE)
            .queryId("mhk.batch04.ReadJpaEntityMapper.selectPagedData")
            .parameterValues(parameters)
            .build()
    }

    @Bean
    @StepScope
    fun batch04Writer(): ItemWriter<ReadJpaEntity> {
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
