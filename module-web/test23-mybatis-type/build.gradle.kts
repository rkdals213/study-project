plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")
    runtimeOnly("com.mysql:mysql-connector-j")
}

