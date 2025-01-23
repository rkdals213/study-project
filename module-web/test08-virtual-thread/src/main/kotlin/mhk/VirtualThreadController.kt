package mhk

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep

@RestController
@RequestMapping("/virtual-thread")
class VirtualThreadController {

    @GetMapping("/test01")
    fun test01(): Pair<String, String> {
        sleep(1000)
        return "message" to "this is virtual thread 1"
    }

    @GetMapping("/test02")
    fun test02(): Pair<String, String> {
        sleep(2000)
        return "message" to "this is virtual thread 2"
    }

    @GetMapping("/test03")
    fun test03(): Pair<String, String> {
        sleep(3000)
        throw RuntimeException("api 호출 실패")
    }

    @GetMapping("/test04")
    fun test04(): Pair<String, String> {
        sleep(4000)
        return "message" to "this is virtual thread 4"
    }
}
