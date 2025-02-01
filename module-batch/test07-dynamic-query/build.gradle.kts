plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")
}
