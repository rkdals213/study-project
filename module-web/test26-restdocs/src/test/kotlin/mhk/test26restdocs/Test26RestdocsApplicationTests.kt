package mhk.test26restdocs

import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.Filter
import io.restassured.internal.http.Status
import io.restassured.specification.RequestSpecification
import mhk.test26restdocs.acceptance.AcceptanceTest
import mhk.test26restdocs.acceptance.getResource
import mhk.test26restdocs.acceptance.givenRequestSpecification
import mhk.test26restdocs.acceptance.postResource
import mhk.test26restdocs.document.*
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration


fun documentFilter01(): Filter {
    return RestAssuredRestDocumentationWrapper.document(
        identifier = "get-api",
        requestPreprocessor = preprocessRequest(prettyPrint()),
        responsePreprocessor = preprocessResponse(prettyPrint()),
        queryParameters(
            "teamName" means "team name" isOptional true
        ),
        pathParameters(
            "teamId" means "the team id" isOptional false
        ),
        responseFields(
            "status" type NUMBER means "status",
            "message" type STRING means "message",
            "data" type OBJECT means "data",
            "data.name" type STRING means "team name",
            "data.status" type STRING means "team status - ${Status.entries.map { it.name }}",
            "data.registeredDateTime" type DATETIME means "team registered date time",
            "data.members" type ARRAY means "team members",
            "data.members[].name" type STRING means "team member name",
            "data.members[].registeredDateTime" type DATETIME means "team member registered date time",
            "data.members[].age" type NUMBER means "team member age",
            "data.members[].isMarried" type BOOLEAN means "team member isMarried",
        )
    )
}


fun documentFilter02(): Filter {
    return RestAssuredRestDocumentationWrapper.document(
        identifier = "post-api",
        requestPreprocessor = preprocessRequest(prettyPrint()),
        responsePreprocessor = preprocessResponse(prettyPrint()),
        requestFields(
            "teamId" type STRING means "team id",
            "teamName" type STRING means "team name"
        ),
        responseFields(
            "status" type NUMBER means "status",
            "message" type STRING means "message",
            "data" type OBJECT means "data",
            "data.name" type STRING means "team name",
            "data.status" type STRING means "team status - ${Status.entries.map { it.name }}",
            "data.registeredDateTime" type DATETIME means "team registered date time",
            "data.members" type ARRAY means "team members",
            "data.members[].name" type STRING means "team member name",
            "data.members[].registeredDateTime" type DATETIME means "team member registered date time",
            "data.members[].age" type NUMBER means "team member age",
            "data.members[].isMarried" type BOOLEAN means "team member isMarried",
        )
    )
}

@AcceptanceTest
class Test26RestdocsApplicationTests : FunSpec({

    lateinit var restDocumentation: ManualRestDocumentation

    lateinit var restDocumentSpec: RequestSpecification

    beforeTest { testCase ->
        restDocumentation = ManualRestDocumentation()

        restDocumentation.beforeTest(
            testCase.spec::class.java,
            testCase.name.testName
        )

        restDocumentSpec = RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .build()
    }

    afterTest {
        restDocumentation.afterTest()
    }

    context("api 문서 생성 테스트") {
        test("get mapping") {
            getResource(
                request = givenRequestSpecification(restDocumentSpec, documentFilter01()),
                url = "/api/{teamId}",
                queryParams = mapOf("teamName" to "My Team"),
                pathParams = mapOf("teamId" to "1")
            )
        }

        test("post mapping") {
            postResource(
                request = givenRequestSpecification(restDocumentSpec, documentFilter02()),
                url = "/api",
                body = mapOf(
                    "teamId" to "1",
                    "teamName" to "My Team"
                )
            )
        }
    }

    extensions(SpringExtension)
})
