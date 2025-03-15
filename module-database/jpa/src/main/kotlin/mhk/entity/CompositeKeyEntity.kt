package mhk.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(catalog = "kotlin_study", name = "composite_key_entity")
class CompositeKeyEntity(
    @EmbeddedId
    val pk: CompositeKeyEntityPK,

    val value01: String,
    val value02: BigDecimal,
)

@Embeddable
class CompositeKeyEntityPK(
    val key01: String,
    val key02: String,
    val key03: String
)
