plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.batch:spring-batch-test")
}

allOpen {
    annotation("com.example.common.JobParameter")
}
