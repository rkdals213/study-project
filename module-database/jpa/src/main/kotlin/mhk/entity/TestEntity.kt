package mhk.entity

import jakarta.persistence.*

@Entity
@Table(catalog = "kotlin_study", name = "test_entity")
class TestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val data: String
) {
    override fun toString(): String {
        return "TestEntity(id=$id, data='$data')"
    }
}
