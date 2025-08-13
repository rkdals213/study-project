package mhk.typehandler.each

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.sql.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

val yyyyMMddHHmmssFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
val yyyyMMddFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
val yyyyMMformatter = DateTimeFormatter.ofPattern("yyyyMM")

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, yyyyMMddHHmmssFormatter)
fun String.toLocalDate(): LocalDate = LocalDate.parse(this, yyyyMMddFormatter)
fun String.toYearMonth(): YearMonth = YearMonth.parse(this, yyyyMMformatter)

fun LocalDateTime.yyyyMMddHHmmss(): String = this.format(yyyyMMddHHmmssFormatter)
fun LocalDate.yyyyMMdd(): String = this.format(yyyyMMddFormatter)
fun YearMonth.yyyyMM(): String = this.format(yyyyMMformatter)

// 1) YearMonth <-> String
class StringToYearMonthTypeHandler : BaseTypeHandler<YearMonth>() {
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: YearMonth, jdbcType: JdbcType?) {
        ps.setString(i, parameter.yyyyMM())
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): YearMonth? {
        val str = rs.getString(columnName) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toYearMonth()
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): YearMonth? {
        val str = rs.getString(columnIndex) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toYearMonth()
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): YearMonth? {
        val str = cs.getString(columnIndex) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toYearMonth()
    }
}

// 2) LocalDate <-> String
class StringToLocalDateTypeHandler : BaseTypeHandler<LocalDate>() {
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: LocalDate, jdbcType: JdbcType?) {
        ps.setString(i, parameter.yyyyMMdd())
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): LocalDate? {
        val str = rs.getString(columnName) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toLocalDate()
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LocalDate? {
        val str = rs.getString(columnIndex) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toLocalDate()
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LocalDate? {
        val str = cs.getString(columnIndex) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toLocalDate()
    }
}

class DateToLocalDateTypeHandler : BaseTypeHandler<LocalDate>() {
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: LocalDate, jdbcType: JdbcType?) {
        ps.setDate(i, Date.valueOf(parameter))
    }
    override fun getNullableResult(rs: ResultSet, columnName: String): LocalDate? {
        val date = rs.getDate(columnName)
        println("date : $date, ${date::class.qualifiedName}}")
        return date?.toLocalDate()
    }
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LocalDate? {
        val date = rs.getDate(columnIndex)
        println("date : $date, ${date::class.qualifiedName}}")
        return date?.toLocalDate()
    }
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LocalDate? {
        val date = cs.getDate(columnIndex)
        println("date : $date, ${date::class.qualifiedName}}")
        return date?.toLocalDate()
    }
}

// 3) LocalDateTime <-> String
class StringToLocalDateTimeTypeHandler : BaseTypeHandler<LocalDateTime>() {
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: LocalDateTime, jdbcType: JdbcType?) {
        ps.setString(i, parameter.yyyyMMddHHmmss())
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): LocalDateTime? {
        val str = rs.getString(columnName) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toLocalDateTime()
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LocalDateTime? {
        val str = rs.getString(columnIndex) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toLocalDateTime()
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LocalDateTime? {
        val str = cs.getString(columnIndex) ?: return null
        println("str : $str, ${str::class.qualifiedName}}")
        return str.toLocalDateTime()
    }
}

class TimestampToLocalDateTimeTypeHandler : BaseTypeHandler<LocalDateTime>() {
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: LocalDateTime, jdbcType: JdbcType?) {
        ps.setTimestamp(i, Timestamp.valueOf(parameter))
    }
    override fun getNullableResult(rs: ResultSet, columnName: String): LocalDateTime? {
        val timestamp = rs.getTimestamp(columnName)
        println("timestamp : $timestamp, ${timestamp::class.qualifiedName}}")
        return timestamp?.toLocalDateTime()
    }
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LocalDateTime? {
        val timestamp = rs.getTimestamp(columnIndex)
        println("timestamp : $timestamp, ${timestamp::class.qualifiedName}}")
        return timestamp?.toLocalDateTime()
    }
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LocalDateTime? {
        val timestamp = cs.getTimestamp(columnIndex)
        println("timestamp : $timestamp, ${timestamp::class.qualifiedName}}")
        return timestamp?.toLocalDateTime()
    }
}

