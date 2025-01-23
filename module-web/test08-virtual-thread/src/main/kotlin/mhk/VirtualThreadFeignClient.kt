package mhk

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "virtual-thread-server",
    url = "localhost:8080/virtual-thread"
)
interface VirtualThreadFeignClient {
    @GetMapping("/test01")
    fun test01(): ResponseEntity<String>

    @GetMapping("/test02")
    fun test02(): ResponseEntity<String>

    @GetMapping("/test03")
    fun test03(): ResponseEntity<String>

    @GetMapping("/test04")
    fun test04(): ResponseEntity<String>
}
