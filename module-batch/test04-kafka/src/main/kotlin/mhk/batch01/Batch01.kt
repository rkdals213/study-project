package mhk.batch01

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.kafka.KafkaItemReader
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.ssl.SslBundles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.Duration
import java.util.*

@Configuration
class Batch01(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val kafkaProperties: KafkaProperties,
    private val sslBundles: SslBundles
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
            .chunk<String, String>(CHUNK_SIZE, transactionManager)
            .reader(batch01kafkaReader())
            .writer(batch01Writer())
            .build()
    }

    @Bean
    fun batch01kafkaReader(): KafkaItemReader<String, String> {
        val props = Properties()
        props.putAll(kafkaProperties.buildConsumerProperties(sslBundles))
        props["group.id"] = "batch01-group-id"

        return KafkaItemReaderBuilder<String, String>()
            .name("batch01reader")
            .topic("batch01-test01")
            .partitions(0)
            .consumerProperties(props)
            .pollTimeout(Duration.ofSeconds(10))
//            .partitionOffsets(hashMapOf()) // 기본값은 0, 빈 해시맵을 전달하면 카프카의 현재 offset id부터 읽어옴
            .saveState(true)
            .build()
    }

    @Bean
    fun batch01Writer(): ItemWriter<String> {
        return ItemWriter { list: Chunk<out String> ->
            println(list)
        }
    }

    companion object {
        private const val CHUNK_SIZE = 3
    }
}
