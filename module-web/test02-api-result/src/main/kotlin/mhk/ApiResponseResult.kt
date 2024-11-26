package mhk

sealed class ApiResponseResult<out T> {
    data class Success<out T>(val body: T) : ApiResponseResult<T>()
    data class Failure(val errorResponse: ErrorResponse) : ApiResponseResult<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    inline fun onSuccess(action: (T) -> Unit): ApiResponseResult<T> {
        if (this is Success) {
            action(body)
        }
        return this
    }

    inline fun onFailure(action: (ErrorResponse) -> Unit): ApiResponseResult<T> {
        if (this is Failure) {
            action(errorResponse)
        }
        return this
    }

    fun getOrNull(): T? = if (this is Success) body else null

    fun getOrThrow(): T {
        return when (this) {
            is Success -> body
            is Failure -> throw ApiException(errorResponse = errorResponse)
        }
    }

    inline fun <R> getOrDefault(default: R, transform: (T) -> R): R {
        return when (this) {
            is Success -> transform(body)
            is Failure -> default
        }
    }
}

class ApiException(
    val errorResponse: ErrorResponse
) : RuntimeException()


data class ErrorResponse(
    val message: String,
    val code: String,
    val status: Int
)
