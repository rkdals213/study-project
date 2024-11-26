package mhk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@SpringBootApplication
class Test01ApiResponseApplication

fun main(args: Array<String>) {
    runApplication<Test01ApiResponseApplication>(*args)
}

@RestController
@RequestMapping("/api-response")
class ApiResponseController {

    @GetMapping("/test01")
    fun test01(): ResponseEntity<ApiResponse<TestData>> {
        return ResponseEntity.ok(
            ApiResponse(
                status = "200",
                message = "OK",
                body = TestData(
                    data1 = "data1",
                    data2 = 1,
                    data3 = 2L,
                    data4 = BigDecimal.valueOf(10.00),
                    data5 = 'A'
                )
            )
        )
    }

    @GetMapping("/test02")
    fun test02(): ResponseEntity<ApiResponse<List<TestData>>> {
        return ResponseEntity.ok(
            ApiResponse(
                status = "200",
                message = "OK",
                body = listOf(
                    TestData(
                        data1 = "data1",
                        data2 = 1,
                        data3 = 1L,
                        data4 = BigDecimal.valueOf(10.00),
                        data5 = 'A'
                    ),
                    TestData(
                        data1 = "data2",
                        data2 = 2,
                        data3 = 2L,
                        data4 = BigDecimal.valueOf(20.00),
                        data5 = 'B'
                    ),
                    TestData(
                        data1 = "data3",
                        data2 = 3,
                        data3 = 3L,
                        data4 = BigDecimal.valueOf(30.00),
                        data5 = 'C'
                    )
                )
            )
        )
    }
}

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val body: T
)

data class TestData(
    val data1: String,
    val data2: Int,
    val data3: Long,
    val data4: BigDecimal,
    val data5: Char
)
