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
}
