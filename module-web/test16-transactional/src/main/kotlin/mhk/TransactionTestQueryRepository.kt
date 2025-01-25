package mhk

import com.querydsl.jpa.impl.JPAQueryFactory
import mhk.entity.QTransactionTestEntity.transactionTestEntity
import mhk.entity.TransactionTestEntity
import org.springframework.stereotype.Repository

@Repository
class TransactionTestQueryRepository(
    private val query: JPAQueryFactory
) {

    fun findById(id: Long): TransactionTestEntity? = query.select(transactionTestEntity)
        .from(transactionTestEntity)
        .where(transactionTestEntity.id.eq(id))
        .fetchOne()
}
