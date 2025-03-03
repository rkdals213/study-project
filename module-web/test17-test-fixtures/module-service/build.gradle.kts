plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    testApi(testFixtures(project(":module-web:test17-test-fixtures:test17-module-domain")))
}
