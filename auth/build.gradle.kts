import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "net.oneblog"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val googleApiClientVersion = "2.7.2"

dependencies {
    implementation(project(":shared-exceptions"))
    implementation(project(":shared-config"))
    implementation(project(":api"))
    implementation(project(":validation-api"))
    implementation(project(":user"))
    implementation(project(":email"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.hateoas)
    implementation(libs.spring.boot.starter.mail)
    implementation("com.google.api-client:google-api-client:$googleApiClientVersion")
    implementation(libs.jjwt.api)
    implementation(libs.mapstruct)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.processor)

    testImplementation(libs.jjwt.api)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.hikari)
    testImplementation(libs.liquibase.core)
    testImplementation(libs.postgresql)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.jjwt.impl)
    testRuntimeOnly(libs.jjwt.jackson)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier = ""
}

