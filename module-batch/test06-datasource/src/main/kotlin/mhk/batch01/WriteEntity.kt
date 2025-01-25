package mhk.batch01

import jakarta.persistence.*

@Entity
@Table(name = "write_entity")
class WriteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val data: String
)
