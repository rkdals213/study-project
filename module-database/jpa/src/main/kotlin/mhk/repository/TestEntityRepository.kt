package mhk.repository

import mhk.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TestEntityRepository : JpaRepository<TestEntity, Long> {}
