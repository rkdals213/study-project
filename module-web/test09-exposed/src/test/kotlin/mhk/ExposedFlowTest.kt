package mhk

import io.kotest.core.spec.style.FunSpec
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class ExposedFlowTest : FunSpec({

    context("DB 세팅이 완료되고") {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")

        test("DB에 인서트 요청하면") {
            transaction {
                addLogger(StdOutSqlLogger)
                SchemaUtils.create(TeamExposedEntity, MemberExposedEntity)

                val insertTeamResult: InsertStatement<Number> = TeamExposedEntity.insert {
                    it[name] = "My Team"
                    it[createdDatetime] = LocalDateTime.now()
                }

                MemberExposedEntity.insert {
                    it[name] = "My Member 1"
                    it[age] = 10
                    it[teamId] = insertTeamResult[TeamExposedEntity.id]
                }

                MemberExposedEntity.insert {
                    it[name] = "My Member 2"
                    it[age] = 20
                    it[teamId] = insertTeamResult[TeamExposedEntity.id]
                }

                TeamExposedEntity.selectAll()
                    .where { TeamExposedEntity.id eq 1 }
                    .forEach { println(it[TeamExposedEntity.name]) }

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

                val teamMemberDomains = TeamExposedEntity.selectAll()
                    .where { TeamExposedEntity.id inList memberResults.map { it[TeamExposedEntity.id] } }
                    .map {
                        TeamMemberDomain(
                            teamId = it[TeamExposedEntity.id],
                            teamName = it[TeamExposedEntity.name],
                            createdDateTime = it[TeamExposedEntity.createdDatetime],
                            members = memberDomains
                        )
                    }

                println(teamMemberDomains)
            }
        }
    }
})
