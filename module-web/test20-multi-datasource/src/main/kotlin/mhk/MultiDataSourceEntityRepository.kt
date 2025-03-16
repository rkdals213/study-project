package mhk

import org.springframework.data.jpa.repository.JpaRepository

interface MultiDataSourceEntityRepository: JpaRepository<MultiDataSourceEntity, Long> {

    fun findById(id: String): MultiDataSourceEntity?
}
