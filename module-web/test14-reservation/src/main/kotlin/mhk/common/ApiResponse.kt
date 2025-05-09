package mhk.common

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

@NokDslMarker
data class ApiResponse<T>(
    var status: Int = 200,
    var message: String? = "",
    var data: T? = null
)

fun <T> responseEntity(block: ResponseEntityBuilder<T>.() -> Unit): ResponseEntity<T> {
    val responseEntityBuilder = ResponseEntityBuilder<T>()
    responseEntityBuilder.block()
    return responseEntityBuilder.build()
}

fun <T> apiResponse(block: ApiResponse<T>.() -> Unit): ApiResponse<T> {
    val apiResponse = ApiResponse<T>()
    apiResponse.block()
    return apiResponse
}

@NokDslMarker
data class ResponseEntityBuilder<T>(
    var status: HttpStatus = HttpStatus.OK,
    var contentType: MediaType = MediaType.APPLICATION_JSON,
    var body: T? = null
) {
    fun build() = ResponseEntity.status(status)
        .contentType(contentType)
        .body(body)
}

object EmptyBody

@DslMarker
annotation class NokDslMarker
