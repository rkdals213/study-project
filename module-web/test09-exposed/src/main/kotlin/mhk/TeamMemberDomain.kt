package mhk

import java.time.LocalDateTime

data class TeamMemberDomain(
    val teamId: Long,
    val teamName: String,
    val createdDateTime: LocalDateTime,
    val members: List<MemberDomain>
)

data class MemberDomain(
    val memberId: Long,
    val name: String,
    val age: Int
)
