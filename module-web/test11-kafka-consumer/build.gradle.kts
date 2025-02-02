plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database:jpa"))

    implementation("org.springframework.kafka:spring-kafka:3.3.0")
}
