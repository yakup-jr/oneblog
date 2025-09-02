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

dependencies {
    implementation(project(":api"))
    implementation(project(":shared-config"))
    implementation(project(":user"))
    implementation(project(":shared-exceptions"))
    implementation(project(":validation-api"))

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.mail)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.mapstruct)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.processor)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
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
