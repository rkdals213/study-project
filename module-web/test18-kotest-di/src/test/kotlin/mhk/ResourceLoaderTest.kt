package mhk

import io.kotest.core.spec.style.FunSpec
import org.springframework.core.io.ResourceLoader

//Spring에서 기본적으로 제공되는 Bean들은 ApplicationContext에서 자동으로 관리되므로 굳이 @SpringBootTest를 사용하지 않아도 주입이 가능
class ResourceLoaderTest(
    private val resourceLoader: ResourceLoader,
) : FunSpec({

    context("ResourceLoaderTest") {
        test("생성자로 바로 빈 주입 가능") {
            resourceLoader.getResource("classpath:application.properties")
                .inputStream
                .readAllBytes()
                .apply { println(this.toString(Charsets.UTF_8)) }
        }
    }
})
