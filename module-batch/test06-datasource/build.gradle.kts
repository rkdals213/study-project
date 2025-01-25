plugins {
	id("org.springframework.boot")
	kotlin("plugin.jpa") version "1.9.25"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-batch")
	api("org.springframework.boot:spring-boot-starter-data-jpa")
	testImplementation("org.springframework.batch:spring-batch-test")
	runtimeOnly("com.mysql:mysql-connector-j")
}

noArg {
	annotation("jakarta.persistence.Entity")
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
	annotation("com.example.common.JobParameter")
}
