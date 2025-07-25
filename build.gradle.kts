import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    application
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

application {
    mainClass = "net.oneblog.app.AppApplication"
}

tasks.named<BootRun>("bootRun") {
    dependsOn(":app:bootRun")
}

tasks.named<BootJar>("bootJar") {
    dependsOn(":app:bootJar")
    doLast {
        copy {
            from("app/build/libs/oneblog.jar")
            into("build/libs/")
        }
    }
}