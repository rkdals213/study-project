package mhk.service

import TestFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.core.io.ResourceLoader

class ModuleServiceApplicationTests(
    private val resourceLoader: ResourceLoader
) : FunSpec({

    context("도메인 모듈에 있는 test-fixtures를 참조") {
        beforeTest {
            resourceLoader.getResource("classpath:test.sql")
                .inputStream
                .readAllBytes()
                .apply { println(this.toString(Charsets.UTF_8)) }
        }


        test("클래스를 선언할 수 있다") {
            TestFixture(1).id shouldBe 1
        }

        test("파일을 읽어올 수 있다") {
            resourceLoader.getResource("classpath:test-fixture.json")
                .inputStream
                .readAllBytes()
                .apply { println(this.toString(Charsets.UTF_8)) }
        }
    }
})
