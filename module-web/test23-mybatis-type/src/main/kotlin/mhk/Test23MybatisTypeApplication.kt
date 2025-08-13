package mhk

import mhk.typehandler.each.toLocalDate
import mhk.typehandler.each.toLocalDateTime
import mhk.typehandler.each.toYearMonth
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth


@SpringBootApplication
class Application(
    private val mapper: TimeConvertTestMapper
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val dto = TimeConvertTestDto(
            stringToYearMonth = YearMonth.of(2025, 7),
            stringToLocalDate = LocalDate.of(2025, 7, 5),
            dateToLocalDate = LocalDate.of(2025, 7, 5),
            stringToLocalDateTime = LocalDateTime.of(2025, 7, 5, 0, 0, 0),
            timestampToLocalDateTime = LocalDateTime.of(2025, 7, 5, 0, 0, 0),
            dateTimeToLocalDateTime = LocalDateTime.of(2025, 7, 5, 0, 0, 0),
        )
//        mapper.insert(dto)


        println("=== SELECT RESULT ===")

//
//        val results2 = mapper.selectAll2()
//        results2.map {
//            TimeConvertTestDto(
//                id = it["id"] as Long?,
//                stringToYearMonth = (it["string_to_year_month"] as String?)?.toYearMonth(),
//                stringToLocalDate = (it["string_to_local_date"] as String?)?.toLocalDate(),
//                dateToLocalDate = (it["date_to_local_date"] as Date?)?.toLocalDate(),
//                stringToLocalDateTime = (it["string_to_local_date_time"] as String?)?.toLocalDateTime(),
//                timestampToLocalDateTime = (it["timestamp_to_local_date_time"] as Timestamp?)?.toLocalDateTime(),
//                dateTimeToLocalDateTime = (it["date_time_to_local_date_time"] as LocalDateTime?)
//            )
//        }.forEach { println(it) }


        repeat(100) {
            val results = mapper.selectAll()
            results.forEach { println(it) }
            val results3 = mapper.selectAll3()
            results3.forEach { println(it) }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
