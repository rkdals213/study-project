package mhk

import org.apache.ibatis.annotations.Mapper

@Mapper
interface TimeConvertTestMapper {

    fun insert(timeConvertTestDto: TimeConvertTestDto): Int

    fun selectAll(): List<TimeConvertTestDto>

    fun selectAll2(): List<Map<String, Any>>

    fun selectAll3(): List<TimeConvertTestDto>
}
