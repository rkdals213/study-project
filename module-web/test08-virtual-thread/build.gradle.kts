plugins {
    id("org.springframework.boot")
}

dependencies {
    api(project(":module-database:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.projectreactor:reactor-test")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.1")
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.117.Final:osx-aarch_64")
}
