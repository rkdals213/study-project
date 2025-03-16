package mhk

import org.apache.ibatis.annotations.Mapper

@Mapper
interface MultiDataSourceEntityMapper {

    fun findById(id: String): MultiDataSourceEntity?

    fun save(entity: MultiDataSourceEntity): Int
}
