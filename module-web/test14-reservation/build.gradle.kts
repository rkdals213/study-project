plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database:jpa"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.redisson:redisson-spring-boot-starter:3.41.0")
}
