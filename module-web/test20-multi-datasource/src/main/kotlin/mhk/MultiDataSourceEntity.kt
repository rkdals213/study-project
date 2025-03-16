package mhk

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(catalog = "kotlin_study", name = "multi_datasource_entity")
class MultiDataSourceEntity(
    @Id
    val id: String,

    val value: String,
)
