package mhk.batch01

import jakarta.persistence.EntityManager
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class Batch01(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManager: EntityManager
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
            .tasklet({ _, _ ->
                val resultList = entityManager.createNativeQuery("SELECT concat(id, ', ', name, ', ', birthday, ', ', address) as data FROM batch_study_data_entity")
                    .resultList

                println(resultList)

                RepeatStatus.FINISHED
            }, transactionManager)
            .build()
    }
}
