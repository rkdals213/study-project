package mhk

import io.kotest.core.spec.style.FunSpec
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest

//EntityManager는 **JPA 영속성 컨텍스트(Persistence Context)**를 관리하는 객체
//Spring에서는 JPA 관련 Bean을 EntityManagerFactory를 통해 관리하며, 이를 주입받으려면 JPA 관련 설정이 포함된 컨텍스트가 필요
@DataJpaTest
//@SpringBootTest
class EntityManagerTest(
    private val entityManager: EntityManager,
) : FunSpec({

    context("EntityManagerTest") {
        test("DataJpaTest, SpringBootTest를 이용해 빈 주입 가능") {
            entityManager.createNativeQuery("select 1 from dual").resultList
        }
    }
})
