package mhk.config

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.http.MediaType

fun givenRequestSpecification(): RequestSpecification = RestAssured.given()
    .contentType(ContentType.JSON)
    .log().all()

fun getResource(
    request: RequestSpecification = givenRequestSpecification(),
    url: String,
    params: Map<String, String> = emptyMap()
): ExtractableResponse<Response> {
    return request.with()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .params(params)
        .get(url)
        .then()
        .log().all()
        .extract()
}

fun postResource(
    request: RequestSpecification = givenRequestSpecification(),
    url: String,
    body: String
): ExtractableResponse<Response> {
    return request.with()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(body)
        .post(url)
        .then()
        .log().all()
        .extract()
}
