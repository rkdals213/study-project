package mhk

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/token")
class TokenController(
    private val tokenRepository: TokenRepository
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Token {
        return Token(
            id = UUID.randomUUID().toString(),
            memberId = loginRequest.memberId,
            accessIP = loginRequest.accessIP,
            accessToken = UUID.randomUUID().toString()
        ).let {
            tokenRepository.save(it)
        }
    }

    @PostMapping("/doSomething")
    fun doSomething(@RequestBody doSomething: DoSomething): DoSomethingResponse {
        val token = tokenRepository.findByAccessToken(doSomething.accessToken) ?: return DoSomethingResponse(message = "토큰이 존재하지 않습니다")
        check(token.accessIP == doSomething.accessIP) { return DoSomethingResponse(message = "다른기기에서 로그인 하였습니다") }

        return DoSomethingResponse(message = "성공하였습니다")
    }
}

data class LoginRequest(
    val memberId: String,
    val accessIP: String
)

data class DoSomething(
    val accessToken: String,
    val accessIP: String
)

data class DoSomethingResponse(
    val message: String
)
