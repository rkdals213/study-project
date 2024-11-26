package mhk

import mhk.TeamExposedEntity
import org.jetbrains.exposed.sql.Table

object MemberExposedEntity : Table("member_entity") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    val age = integer("age")
    val teamId = long("team_id") references TeamExposedEntity.id

    override val primaryKey = PrimaryKey(id)
}
