package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import mhk.config.AcceptanceTest
import mhk.config.postResource

@AcceptanceTest
class Test12RedisJwtApplicationTests : FunSpec({
    test("Redis를 이용한 토큰 작업") {
        val token = 로그인_요청함(CORRECT_IP)

        작업_요청됨(token.accessToken, CORRECT_IP).also {
            it.message shouldBe "성공하였습니다"
        }

        작업_요청됨(WRONG_TOKEN, CORRECT_IP).also {
            it.message shouldBe "토큰이 존재하지 않습니다"
        }

        작업_요청됨(token.accessToken, WRONG_IP).also {
            it.message shouldBe "다른기기에서 로그인 하였습니다"
        }
    }

    extension(SpringExtension)
})

private const val CORRECT_IP = "127.0.0.1"
private const val WRONG_IP = "127.0.0.2"
private const val WRONG_TOKEN = "1q2w3e4r"

private fun 로그인_요청함(ip: String): Token = postResource(
    url = "/token/login",
    body = """
        {
          "memberId": "kang",
          "accessIP": "$ip"
        }
    """.trimIndent()
).`as`(Token::class.java)

private fun 작업_요청됨(accessToken: String, ip: String): DoSomethingResponse = postResource(
    url = "/token/doSomething",
    body = """
        {
          "accessToken": "$accessToken",
          "accessIP": "$ip"
        }
    """.trimIndent()
).`as`(DoSomethingResponse::class.java)

