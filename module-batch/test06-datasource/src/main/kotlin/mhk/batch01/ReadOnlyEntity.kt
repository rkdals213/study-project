package mhk.batch01

import jakarta.persistence.*

@Entity
@Table(name = "read_only_entity")
class ReadOnlyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val data: String
)
