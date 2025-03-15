package mhk.repository

import mhk.entity.CompositeKeyEntity
import mhk.entity.CompositeKeyEntityPK
import org.springframework.data.jpa.repository.JpaRepository

interface CompositeKeyEntityRepository : JpaRepository<CompositeKeyEntity, Long> {
    fun findByPk(pk: CompositeKeyEntityPK): CompositeKeyEntity?
}
