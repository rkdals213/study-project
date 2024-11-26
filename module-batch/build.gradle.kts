plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    testImplementation("org.springframework.batch:spring-batch-test")
    implementation("org.springframework.kafka:spring-kafka")
}

allOpen {
    annotation("com.example.common.JobParameter")
}
