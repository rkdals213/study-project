package mhk

import java.math.BigDecimal

data class CompositeKeyEntityDto(
    val key01: String,
    val key02: String,
    val value01: String
)

data class CompositeKeyEntityGroupByDto(
    val key01: String,
    val key02: String,
    val sumValue02: BigDecimal,
    val count: Long
)

data class CompositeKeyEntityJoinGroupByDto(
    val key01: String,
    val key02: String,
    val sumValue02: BigDecimal,
    val count: Long
)
