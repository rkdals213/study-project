package mhk

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ExposedAutoConfiguration::class])
class ModuleBatchApplication

fun main(args: Array<String>) {
    runApplication<ModuleBatchApplication>(*args)
}
