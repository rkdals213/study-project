package mhk

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ExposedAutoConfiguration::class])
class Test09ExposedApplication

fun main(args: Array<String>) {
    runApplication<Test09ExposedApplication>(*args)
}
