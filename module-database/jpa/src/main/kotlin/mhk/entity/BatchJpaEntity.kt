package mhk.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "batch_study_data_entity")
class ReadJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val birthday: LocalDate,
    val address: String,
) {
    override fun toString(): String {
        return "BatchStudyDataEntity(id=$id, name='$name', birthday=$birthday, address='$address')"
    }
}

@Entity(name = "batch_study_data_entity2")
class WriteJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val birthday: LocalDate,
    val address: String,
) {
    override fun toString(): String {
        return "BatchStudyDataEntity(id=$id, name='$name', birthday=$birthday, address='$address')"
    }
}
