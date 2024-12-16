package mhk.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName


@Profile("test")
@Configuration
class TestRedisConfiguration {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val container = GenericContainer(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(REDIS_PORT)

        container.start()

        val configuration = RedisStandaloneConfiguration(container.host, container.getMappedPort(REDIS_PORT))
        return LettuceConnectionFactory(configuration)
    }

    companion object {
        private const val REDIS_IMAGE = "redis:7.4.1-alpine"
        private const val REDIS_PORT = 6379
    }
}
