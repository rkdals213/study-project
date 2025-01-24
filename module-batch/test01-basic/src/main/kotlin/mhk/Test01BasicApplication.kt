package mhk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Test01BasicApplication

fun main(args: Array<String>) {
    runApplication<Test01BasicApplication>(*args)
}
