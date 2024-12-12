package mhk.common.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@Configuration
class RedisConfiguration {

    @Bean
    fun connectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory()
    }
}
