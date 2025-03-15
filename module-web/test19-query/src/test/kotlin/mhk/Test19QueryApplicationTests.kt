package mhk

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import mhk.entity.CompositeKeyEntityPK
import mhk.entity.QCompositeKeyEntity.compositeKeyEntity
import mhk.entity.QCompositeKeyLineEntity.compositeKeyLineEntity
import mhk.entity.QTestEntity.testEntity
import mhk.repository.CompositeKeyEntityRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class Test19QueryApplicationTests(
    private val query: JPAQueryFactory,
    private val compositeKeyEntityRepository: CompositeKeyEntityRepository,
) : FunSpec({

    context("query test") {
        test("test entity") {
            query.select(testEntity)
                .from(testEntity)
                .fetch()
        }
    }

    context("composite key entity") {
        test("find all") {
            compositeKeyEntityRepository.findAll()
        }

        test("find by id") {
            shouldThrow<Exception> {
                compositeKeyEntityRepository.findById(1L)
            }
        }

        test("find by pk") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            compositeKeyEntityRepository.findByPk(pk)
        }

        test("find by pk query") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            query.select(compositeKeyEntity)
                .from(compositeKeyEntity)
                .where(compositeKeyEntity.pk.eq(pk))
                .fetch()
        }

        test("find by pk and value query") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            query.select(compositeKeyEntity)
                .from(compositeKeyEntity)
                .where(
                    compositeKeyEntity.pk.eq(pk),
                    compositeKeyEntity.value01.eq("key01")
                )
                .fetch()
        }

        test("find dto by pk and value query") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            query.select(
                Projections.constructor(
                    CompositeKeyEntityDto::class.java,
                    compositeKeyEntity.pk.key01,
                    compositeKeyEntity.pk.key02,
                    compositeKeyEntity.value01
                )
            ).from(compositeKeyEntity)
                .where(
                    compositeKeyEntity.pk.eq(pk),
                    compositeKeyEntity.value01.eq("key01")
                )
                .fetch()
        }

        test("group by query") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            query.select(
                Projections.constructor(
                    CompositeKeyEntityGroupByDto::class.java,
                    compositeKeyEntity.pk.key01,
                    compositeKeyEntity.pk.key02,
                    compositeKeyEntity.value02.sum(),
                    compositeKeyEntity.count()
                )
            )
                .from(compositeKeyEntity)
                .where(compositeKeyEntity.pk.eq(pk))
                .groupBy(compositeKeyEntity.pk.key01, compositeKeyEntity.pk.key02)
                .fetch()
        }

        test("join query") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            query.select(compositeKeyLineEntity)
                .from(compositeKeyLineEntity)
                .innerJoin(compositeKeyEntity).on(compositeKeyEntity.pk.eq(compositeKeyLineEntity.fk))
                .where(compositeKeyLineEntity.fk.eq(pk))
                .fetch()
        }

        test("join group by query") {
            val pk = CompositeKeyEntityPK("key01", "key02", "key03")

            query.select(
                Projections.constructor(
                    CompositeKeyEntityJoinGroupByDto::class.java,
                    compositeKeyEntity.pk.key01,
                    compositeKeyEntity.pk.key02,
                    compositeKeyEntity.value02.add(compositeKeyLineEntity.value02).sum(),
                    compositeKeyEntity.count()
                )
            )
                .from(compositeKeyLineEntity)
                .innerJoin(compositeKeyEntity).on(compositeKeyEntity.pk.eq(compositeKeyLineEntity.fk))
                .where(compositeKeyLineEntity.fk.eq(pk))
                .groupBy(compositeKeyEntity.pk.key01, compositeKeyEntity.pk.key02)
                .fetch()
        }
    }
})
