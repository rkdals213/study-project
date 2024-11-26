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
        return "message" to "this is virtual thread"
    }
}
