plugins {
    id("org.springframework.boot")
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.epages.restdocs-api-spec") version "0.18.2"
}

val asciidoctorExt = configurations.create("asciidoctorExt") {
    extendsFrom(configurations["testImplementation"])
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured:3.0.1")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.1")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.1")

    testImplementation("com.epages:restdocs-api-spec-restassured:0.19.4")
}

tasks {
    val snippetsDir = layout.buildDirectory.dir("generated-snippets")

    test {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        dependsOn(test)

        sourceDir("src/docs/asciidoc")
        sources {
            include("index.adoc")
        }

        inputs.dir(snippetsDir)
        configurations(asciidoctorExt.name)
        baseDirFollowsSourceFile()

        attributes(
            mapOf(
                "snippets" to snippetsDir.get().asFile.absolutePath
            )
        )
    }

    val generateDocumentation by registering(Copy::class) {
        dependsOn(asciidoctor)

        from(asciidoctor.map { it.outputDir }) {
            include("index.html")
        }

        into(layout.projectDirectory.dir("src/main/resources/static/docs"))

        doFirst {
            delete(layout.projectDirectory.dir("src/main/resources/static/docs"))
        }
    }
}

postman {
    title = "My API"
    version = "0.1.0"
    baseUrl = "http://localhost:8080"
    snippetsDirectory = "build/generated-snippets"
}

openapi3 {
    this.setServer("http://localhost:8080")
    title = "My API"
    description = "My API description"
    version = "0.1.0"
    format = "json" // or json
}