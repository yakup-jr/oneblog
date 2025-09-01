import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "net.oneblog"
version = "0.0.1-SNAPSHOT"



repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-compress:1.28.0")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.hikari)
    implementation(libs.liquibase.core)
    implementation(libs.postgresql)
    implementation(libs.spring.security.test)
    implementation(libs.spring.boot.starter.test)
    implementation(libs.spring.boot.testcontainers)
    implementation(libs.testcontainers.postgresql)
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

