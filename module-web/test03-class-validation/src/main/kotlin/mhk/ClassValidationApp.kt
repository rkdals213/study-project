package mhk

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/class-validation")
class ClassValidationApp {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping
    fun get(@RequestParam phoneNumber: PhoneNumber) {
        logger.info("{}", phoneNumber)
    }

    @PostMapping
    fun post(@RequestBody request: Request) {
        logger.info("{}", request)
    }

    data class Request(val phoneNumber: PhoneNumber)

}
