package mhk

import mhk.config.DefaultTransaction
import org.springframework.stereotype.Service

@Service
class MultiDataSourceService(
    private val multiDataSourceEntityRepository: MultiDataSourceEntityRepository,
    private val multiDataSourceEntityMapper: MultiDataSourceEntityMapper
) {

    fun test01() = DefaultTransaction.writable {
        MultiDataSourceEntity(id = "id01", value = "jpa")
            .let {
                multiDataSourceEntityRepository.save(it)
            }

        multiDataSourceEntityRepository.findById("id01")
            .apply { println(this) }
    }

    fun test02() = DefaultTransaction.writable {
        MultiDataSourceEntity(id = "id01", value = "mybatis")
            .let {
                multiDataSourceEntityMapper.save(it)
            }

        multiDataSourceEntityMapper.findById("id01")
            .apply { println(this) }
    }

    fun test03() = DefaultTransaction.writable {
        MultiDataSourceEntity(id = "id01", value = "jpa")
            .let {
                multiDataSourceEntityRepository.save(it)
            }

        multiDataSourceEntityMapper.findById("id01")
            .apply { println(this) }
    }
}
