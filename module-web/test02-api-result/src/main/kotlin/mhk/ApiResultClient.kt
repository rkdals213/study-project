package mhk

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "api-result-client",
    url = "localhost:8080/api-result"
)
interface ApiResultClient {
    @GetMapping("/test01")
    fun test01(): ResponseEntity<String>
}

inline fun <reified T> ResponseEntity<String>.responseResult(): ApiResponseResult<T> {
    return when (this.statusCode.is2xxSuccessful) {
        true -> {
            val response = try {
                defaultObjectMapper.readValue<T>(body!!)
            } catch (e: Exception) {
                val responseBody = this.body.toString()
                return failResponse(responseBody)
            }

            return ApiResponseResult.Success(response)
        }

        else -> {
            val responseBody = this.body.toString()
            failResponse(responseBody)
        }
    }
}

fun failResponse(responseBody: String) = ApiResponseResult.Failure(
    when {
        isErrorResponseDeserializeAble(responseBody) -> defaultObjectMapper.readValue(responseBody, ErrorResponse::class.java)
        else -> defaultError()
    }
)

private fun isErrorResponseDeserializeAble(responseBody: String): Boolean {
    return when (val rootNode = defaultObjectMapper.readTree(responseBody)) {
        null -> false
        else -> rootNode.path("message").isTextual && rootNode.path("status").isNumber && rootNode.path("code").isTextual
    }
}

fun defaultError() = ErrorResponse(
    message = "Unknown Error",
    code = "ERROR",
    status = 500
)

val defaultObjectMapper: ObjectMapper = jsonMapper { addModule(kotlinModule()) }
