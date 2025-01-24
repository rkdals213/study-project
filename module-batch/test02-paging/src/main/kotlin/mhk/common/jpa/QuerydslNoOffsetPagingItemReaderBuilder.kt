package mhk.common.jpa

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManagerFactory
import mhk.QuerydslNoOffsetPagingItemReader
import mhk.options.QuerydslNoOffsetOptions
import java.util.function.Function

class QuerydslNoOffsetPagingItemReaderBuilder<T> {
    lateinit var enf: EntityManagerFactory
    lateinit var options: QuerydslNoOffsetOptions<T>
    var pageSize: Int = 0
    lateinit var queryFunction: Function<JPAQueryFactory, JPAQuery<T>>

    fun build(): QuerydslNoOffsetPagingItemReader<T> {
        return QuerydslNoOffsetPagingItemReader(
            enf,
            pageSize,
            options,
            queryFunction
        )
    }
}

fun <T> QuerydslNoOffsetPagingItemReaderBuilder(block: QuerydslNoOffsetPagingItemReaderBuilder<T>. () -> Unit): QuerydslNoOffsetPagingItemReaderBuilder<T> {
    val builder = QuerydslNoOffsetPagingItemReaderBuilder<T>()
    builder.block()
    return builder
}
