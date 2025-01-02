plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database:jpa"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
