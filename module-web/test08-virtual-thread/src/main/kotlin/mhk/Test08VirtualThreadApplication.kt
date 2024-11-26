package mhk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class Test08VirtualThreadApplication

fun main(args: Array<String>) {
    runApplication<Test08VirtualThreadApplication>(*args)
}
