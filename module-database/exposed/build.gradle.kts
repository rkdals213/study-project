dependencies {
    api("org.jetbrains.exposed:exposed-spring-boot-starter:0.56.0")
    api("org.jetbrains.exposed:exposed-java-time:0.56.0")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
}
