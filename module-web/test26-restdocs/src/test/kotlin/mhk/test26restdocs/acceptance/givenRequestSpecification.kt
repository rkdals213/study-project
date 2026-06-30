package mhk.test26restdocs.acceptance

import io.restassured.RestAssured
import io.restassured.filter.Filter
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.http.MediaType

fun givenRequestSpecification(): RequestSpecification = RestAssured.given()
    .contentType(ContentType.JSON)
    .log().all()

fun givenRequestSpecification(spec: RequestSpecification, documentFilter: Filter): RequestSpecification = RestAssured.given(spec)
    .contentType(ContentType.JSON)
    .log().all()
    .filter(documentFilter)

fun getResource(
    request: RequestSpecification = givenRequestSpecification(),
    url: String,
    queryParams: Map<String, String> = emptyMap(),
    pathParams: Map<String, String> = emptyMap(),
): ExtractableResponse<Response> {
    return request.with()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .queryParams(queryParams)
        .pathParams(pathParams)
        .get(url)
        .then()
        .log().all()
        .extract()
}

fun postResource(
    request: RequestSpecification = givenRequestSpecification(),
    url: String,
    body: Any? = null,
): ExtractableResponse<Response> {
    return request.with()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(body)
        .post(url)
        .then()
        .log().all()
        .extract()
}