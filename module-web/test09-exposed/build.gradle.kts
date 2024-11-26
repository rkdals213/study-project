plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
