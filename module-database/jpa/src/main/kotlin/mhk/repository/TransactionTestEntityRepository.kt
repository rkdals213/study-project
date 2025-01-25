package mhk.repository

import mhk.entity.TransactionTestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionTestEntityRepository : JpaRepository<TransactionTestEntity, Long> {
    fun findByTransactionId(transactionId: Long): TransactionTestEntity?
}
