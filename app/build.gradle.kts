plugins {
    java
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

application {
    mainClass.set("net.oneblog.app.AppApplication")
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
    implementation(project(":auth"))
    implementation(project(":article"))
    implementation(project(":user"))
    implementation(project(":email"))
    implementation(project(":shared-config"))
    implementation(project(":shared-exceptions"))

    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.openapi.starter.webmvc.ui)
    implementation(libs.spring.boot.starter.actuator)
}

tasks.bootJar {
    archiveFileName = "oneblog.jar"
    mainClass = "net.oneblog.app.AppApplication"
}
