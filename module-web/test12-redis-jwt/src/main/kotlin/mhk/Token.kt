package mhk

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import org.springframework.data.repository.CrudRepository

@RedisHash("Token")
class Token(
    @Id
    val id: String,

    val memberId: String,
    val accessIP: String,

    @Indexed
    val accessToken: String
) {
    override fun toString(): String {
        return "Token(id='$id', memberId='$memberId', accessIP='$accessIP', accessToken='$accessToken')"
    }
}

interface TokenRepository: CrudRepository<Token, String> {
    fun findByAccessToken(accessToken: String): Token?
}
