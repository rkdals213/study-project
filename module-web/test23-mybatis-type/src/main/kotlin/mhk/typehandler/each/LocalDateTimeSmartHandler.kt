package mhk.typehandler.each

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeSmartHandler : BaseTypeHandler<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: LocalDateTime?, jdbcType: JdbcType?) {
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): LocalDateTime? {
        return convert(rs.getObject(columnName))
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LocalDateTime? {
        return convert(rs.getObject(columnIndex))
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LocalDateTime? {
        return convert(cs.getObject(columnIndex))
    }

    private fun convert(value: Any?): LocalDateTime? = when (value) {
        is String -> LocalDateTime.parse(value, formatter)
        is Timestamp -> value.toLocalDateTime()
        is LocalDateTime -> value
        else -> null
    }
}
