import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.8.22"
	jacoco
}

group = "br.com.stock"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.1")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Firebase
	implementation("com.google.firebase:firebase-admin:9.3.0")

	// Doc e swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

	// Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.auth0:java-jwt:4.2.1")

	// Observability
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
	implementation("io.github.openfeign:feign-micrometer:13.2.1")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")

	//POI
	implementation("org.apache.poi:poi:5.2.3")
	implementation("org.apache.poi:poi-ooxml:5.2.5")

	// Database
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.flywaydb:flyway-core")
	runtimeOnly("org.postgresql:postgresql")

	// Resilience
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.1")
	implementation("io.github.resilience4j:resilience4j-kotlin:2.2.0")
	implementation("io.github.resilience4j:resilience4j-feign:2.2.0")

	// Mail
	implementation("org.springframework.boot:spring-boot-starter-mail")

	// AWS S3
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.732")

	// AWS Messaging
	implementation("org.springframework.cloud:spring-cloud-starter-aws-messaging:2.2.6.RELEASE")
	implementation("org.springframework:spring-messaging:5.3.24")
	implementation("com.amazonaws:aws-java-sdk-sqs:1.12.533")
	implementation("com.amazonaws:aws-java-sdk-sns:1.12.533")

	// ULID
	implementation("de.huxhorn.sulky:de.huxhorn.sulky.ulid:8.3.0")

	//JOB
	implementation("net.javacrumbs.shedlock:shedlock-spring:5.6.0")
	implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:5.6.0")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.0")
	testImplementation("com.ninja-squad:springmockk:4.0.0")

	// Test Database
	testImplementation("io.zonky.test:embedded-postgres:2.0.2")
	testImplementation("io.zonky.test:embedded-database-spring-test:2.2.0")
	testImplementation(platform("io.zonky.test.postgres:embedded-postgres-binaries-bom:14.5.0"))
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

springBoot {
	buildInfo()
}

jacoco {
	toolVersion = "0.8.9"
}

val excludePackages = listOf(
	"**/br/com/stock/server/ServerApplication*",
	"**/br/com/stock/server/config/AwsConfig*",
	"**/br/com/stock/server/config/FirebaseConfig*",
	"**/br/com/stock/server/service/partner/restaurant/chat/dto/**",
	"**/br/com/stock/server/service/notification/EmailNotificationSenderService*",
	"**/br/com/stock/server/service/notification/PushNotificationService*",
	"**/br/com/stock/server/repository/firebase/**",
	"**/br/com/stock/server/domain/notification/**",
	"**/br/com/stock/server/repository/image/impl/**"
)

extra["excludePackeges"] = excludePackages

tasks.test {
	configure<JacocoTaskExtension> {
		excludes = excludePackages
	}
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(excludePackages)
		}
	)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.91".toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(excludePackages)
		}
	)
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestCoverageVerification {
	dependsOn("jacocoTestReport")
}

tasks.build {
	dependsOn("jacocoTestCoverageVerification")
}

