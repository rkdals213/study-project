package mhk

import mhk.config.ExposedTx
import mhk.config.JpaTx
import mhk.entity.TestEntity
import mhk.repository.TestEntityRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/teamMembers")
class TeamMemberController(
    private val testEntityRepository: TestEntityRepository
) {

    @PostMapping
    fun test01() {
        ExposedTx.writable {
            val team = TeamExposedEntity.insert {
                it[name] = "my team"
                it[createdDatetime] = LocalDateTime.now()
            }

            MemberExposedEntity.insert {
                it[name] = "kang"
                it[age] = 10
                it[teamId] = team[TeamExposedEntity.id]
            }
        }
    }

    @GetMapping
    fun test02(): List<TeamMemberDomain> {
        return ExposedTx.writable  {
            val memberResults = (TeamExposedEntity innerJoin MemberExposedEntity)
                .selectAll()
                .where { MemberExposedEntity.age lessEq 10 }

            val memberDomains = memberResults.map {
                MemberDomain(
                    memberId = it[MemberExposedEntity.id],
                    name = it[MemberExposedEntity.name],
                    age = it[MemberExposedEntity.age]
                )
            }

            TeamExposedEntity.selectAll()
                .where { TeamExposedEntity.id inList memberResults.map { it[TeamExposedEntity.id] } }
                .map {
                    TeamMemberDomain(
                        teamId = it[TeamExposedEntity.id],
                        teamName = it[TeamExposedEntity.name],
                        createdDateTime = it[TeamExposedEntity.createdDatetime],
                        members = memberDomains
                    )
                }
        }
    }

    @GetMapping("/mix")
    fun test03(): Pair<List<TeamMemberDomain>, TestEntity> {
        val teamMemberDomains = ExposedTx.readable  {
            val memberResults = (TeamExposedEntity innerJoin MemberExposedEntity)
                .selectAll()
                .where { MemberExposedEntity.age lessEq 10 }

            val memberDomains = memberResults.map {
                MemberDomain(
                    memberId = it[MemberExposedEntity.id],
                    name = it[MemberExposedEntity.name],
                    age = it[MemberExposedEntity.age]
                )
            }

            TeamExposedEntity.selectAll()
                .where { TeamExposedEntity.id inList memberResults.map { it[TeamExposedEntity.id] } }
                .map {
                    TeamMemberDomain(
                        teamId = it[TeamExposedEntity.id],
                        teamName = it[TeamExposedEntity.name],
                        createdDateTime = it[TeamExposedEntity.createdDatetime],
                        members = memberDomains
                    )
                }
        }

        val testEntity = JpaTx.writable {
            testEntityRepository.save(TestEntity(data = "testData"))
        }

        return teamMemberDomains to testEntity
    }
}
