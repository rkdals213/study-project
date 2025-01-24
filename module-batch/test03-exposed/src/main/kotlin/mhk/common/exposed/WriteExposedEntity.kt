package mhk.common.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object WriteExposedEntity : Table("batch_study_data_entity2") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    val birthday = date("birthday")
    val address = varchar("address", 255)

    override val primaryKey = PrimaryKey(id)
}
