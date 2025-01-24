package mhk.batch02

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.persistence.EntityManagerFactory
import mhk.QuerydslNoOffsetPagingItemReader
import mhk.common.jpa.QuerydslNoOffsetPagingItemReaderBuilder
import mhk.entity.QReadJpaEntity.readJpaEntity
import mhk.entity.ReadJpaEntity
import mhk.expression.Expression
import mhk.options.QuerydslNoOffsetNumberOptions
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.orm.jpa.JpaTransactionManager
import java.text.SimpleDateFormat
import java.util.function.Function


@Configuration
class Batch02(
    private val jobRepository: JobRepository,
    private val transactionManager: JpaTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
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
        return QuerydslNoOffsetPagingItemReaderBuilder<ReadJpaEntity> {
            enf = entityManagerFactory
            options = QuerydslNoOffsetNumberOptions(readJpaEntity.id, Expression.ASC)
            pageSize = CHUNK_SIZE
            queryFunction = Function {
                it.query()
                    .select(readJpaEntity)
                    .from(readJpaEntity)
                    .where(readJpaEntity.id.lt(100))
            }
        }.build()
    }

    @Bean
    fun batch02Writer(): ItemWriter<ReadJpaEntity> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ProducerConfig.RETRIES_CONFIG] = 0
        props[ProducerConfig.BATCH_SIZE_CONFIG] = 16384
        props[ProducerConfig.LINGER_MS_CONFIG] = 1
        props[ProducerConfig.BUFFER_MEMORY_CONFIG] = 33554432
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        val kafkaProducerFactory = DefaultKafkaProducerFactory<String, String>(props)
        val kafkaTemplate = KafkaTemplate(kafkaProducerFactory)
        kafkaTemplate.defaultTopic = "batch-to-web"

        return ItemWriter { list: Chunk<out ReadJpaEntity> ->
            for (data in list) {
                kafkaTemplate.send("batch-to-web", data.id.toString(), objectMapper.writeValueAsString(data))
            }
        }
    }

    companion object {
        private const val CHUNK_SIZE = 100000
        private val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    }
}
