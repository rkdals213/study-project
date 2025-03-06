package mhk

import io.kotest.core.spec.style.FunSpec
import org.springframework.context.annotation.Import

//사용자가 직접 만든 @Component, @Service, @Repository 등의 Bean은 @Import를 사용하여 명시적으로 추가
@Import(value = [SomeBean::class])
class ComponentTest(
    private val someBean: SomeBean,
) : FunSpec({

    context("ComponentTest") {
        test("Import를 이용해 빈 주입 가능") {
            println(someBean.test01())
        }
    }
})
