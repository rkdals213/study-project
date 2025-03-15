package mhk.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(catalog = "kotlin_study", name = "composite_key_line_entity")
class CompositeKeyLineEntity(
    @EmbeddedId
    val pk: CompositeKeyLineEntityPK,

    val value01: String,
    val value02: BigDecimal,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "key01", column = Column(name = "key01_fk")),
        AttributeOverride(name = "key02", column = Column(name = "key02_fk")),
        AttributeOverride(name = "key03", column = Column(name = "key03_fk")),
    )
    val fk: CompositeKeyEntityPK
)

@Embeddable
class CompositeKeyLineEntityPK(
    val key01: String,
    val key02: String,
    val key03: String
)
