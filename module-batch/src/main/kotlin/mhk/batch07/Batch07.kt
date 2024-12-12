package mhk.batch07

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.orm.jpa.JpaTransactionManager
import javax.sql.DataSource

@Configuration
class Batch07(
    private val jobRepository: JobRepository,
    private val transactionManager: JpaTransactionManager,
    private val dataSource: DataSource,
    private val redisTemplate: RedisTemplate<String, String>
) {
    @Bean
    fun batch07Job(): Job {
        return JobBuilder("batch07Job", jobRepository)
            .start(batch07Step())
            .build()
    }

    @Bean
    @JobScope
    fun batch07Step(): Step {
        return StepBuilder("batch07Step", jobRepository)
            .chunk<RowSalary, RowSalary>(CHUNK_SIZE, transactionManager)
            .reader(batch07RowDataReader())
            .writer(batch07RowDataWriter())
//            .chunk<TotalSalary, TotalSalary>(CHUNK_SIZE, transactionManager)
//            .reader(batch07GroupByReader())
//            .writer(batch07GroupByWriter())
            .build()
    }

    @Bean
    fun batch07GroupByReader(): JdbcCursorItemReader<TotalSalary> {
        return JdbcCursorItemReaderBuilder<TotalSalary>()
            .name("batch07Reader")
            .rowMapper(DataClassRowMapper(TotalSalary::class.java))
            .sql(GROUP_BY_SQL)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun batch07RowDataReader(): JdbcCursorItemReader<RowSalary> {
        return JdbcCursorItemReaderBuilder<RowSalary>()
            .name("batch07Reader")
            .rowMapper(DataClassRowMapper(RowSalary::class.java))
            .sql(ROW_DATA_SQL)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun batch07GroupByWriter(): ItemWriter<TotalSalary> {
        return ItemWriter { list: Chunk<out TotalSalary> ->
            for (totalSalary in list) {
                println(totalSalary)
            }
        }
    }

    @Bean
    fun batch07RowDataWriter(): ItemWriter<RowSalary> {
        return ItemWriter { list: Chunk<out RowSalary> ->
            redisTemplate.executePipelined {
                val command = it.hashCommands()
                for (totalSalary in list) {
                    command.hIncrBy("test".toByteArray(), "${totalSalary.deptNo}_salary".toByteArray(), totalSalary.salary)
                    command.hIncrBy("test".toByteArray(), "${totalSalary.deptNo}_count".toByteArray(), 1)
                }
                it.multi()
                it.exec()
            }
        }
    }

    companion object {
        private const val CHUNK_SIZE = 1000000
        private const val GROUP_BY_SQL = """
            SELECT d.dept_no, d.dept_name, SUM(b.salary) as total_salary, COUNT(a.emp_no) as employee_count
FROM employees a
         INNER JOIN salaries b
                    ON a.emp_no = b.emp_no
         INNER JOIN dept_emp c
                    ON a.emp_no = c.emp_no
         INNER JOIN departments d
                    ON c.dept_no = d.dept_no
WHERE '2024-12-12' BETWEEN b.from_date AND b.to_date
GROUP BY d.dept_no, d.dept_name
        """
        private const val ROW_DATA_SQL = """
        SELECT d.dept_no, d.dept_name, b.salary, a.emp_no
FROM employees a
         INNER JOIN salaries b
                    ON a.emp_no = b.emp_no
         INNER JOIN dept_emp c
                    ON a.emp_no = c.emp_no
         INNER JOIN departments d
                    ON c.dept_no = d.dept_no
WHERE '2024-12-12' BETWEEN b.from_date AND b.to_date
        """
    }
}

data class TotalSalary(
    val deptNo: String,
    val deptName: String,
    val totalSalary: Long,
    val employeeCount: Long
)

data class RowSalary(
    val deptNo: String,
    val deptName: String,
    val salary: Long,
    val empNo: Long
)
