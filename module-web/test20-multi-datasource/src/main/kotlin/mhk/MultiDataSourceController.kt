package mhk

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MultiDataSourceController(
    private val multiDataSourceService: MultiDataSourceService
) {

    @PostMapping("/test01")
    fun test01(): MultiDataSourceEntity? {
        return multiDataSourceService.test01()
    }

    @PostMapping("/test02")
    fun test02(): MultiDataSourceEntity? {
        return multiDataSourceService.test02()
    }

    @PostMapping("/test03")
    fun test03(): MultiDataSourceEntity? {
        return multiDataSourceService.test03()
    }
}
