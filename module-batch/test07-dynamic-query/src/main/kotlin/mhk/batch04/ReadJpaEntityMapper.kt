package mhk.batch04

import mhk.entity.ReadJpaEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ReadJpaEntityMapper {
    fun selectPagedData(): List<ReadJpaEntity>
}
