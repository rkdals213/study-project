package mhk

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class Test20MultiDatasourceApplicationTests(
    private val multiDataSourceService: MultiDataSourceService
) : FunSpec({

    context("Application") {
        test("Should load entity") {
            multiDataSourceService.test01()
        }
    }

    extensions(SpringExtension)
})
