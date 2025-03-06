package mhk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class Test17KotestDiApplication

fun main(args: Array<String>) {
    runApplication<Test17KotestDiApplication>(*args)
}

@Component
class SomeBean {

    fun test01(): String {
        return "test bean"
    }
}
