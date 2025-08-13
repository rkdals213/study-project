package mhk

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class TimeConvertTestDto(
    val id: Long? = null,
    val stringToYearMonth: YearMonth? = null,
    val stringToLocalDate: LocalDate? = null,
    val dateToLocalDate: LocalDate? = null,
    val stringToLocalDateTime: LocalDateTime? = null,
    val timestampToLocalDateTime: LocalDateTime? = null,
    val dateTimeToLocalDateTime: LocalDateTime? = null
)
