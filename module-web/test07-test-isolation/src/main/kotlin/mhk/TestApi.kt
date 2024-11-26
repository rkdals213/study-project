package mhk

import mhk.config.JpaTx
import mhk.entity.TestEntity
import mhk.repository.TestEntityRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/test-isolation")
class TestApi(
    private val testEntityRepository: TestEntityRepository
) {

    @PostMapping
    fun test01(@RequestBody testData: TestData): TestData {
        return testData
    }

    @PostMapping("/insert")
    fun test02(@RequestBody testData: TestData) {
        JpaTx.writable {
            testEntityRepository.save(TestEntity(data = testData.data))
        }
    }

    @GetMapping("/get")
    fun test03(): ResponseEntity<ApiResponse<MutableList<TestEntity>>> {
        return ResponseEntity.ok(
            ApiResponse(
                status = "200",
                message = "OK",
                body = testEntityRepository.findAll()
            )
        )
    }
}

data class TestData(
    val data: String
)

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val body: T
)
