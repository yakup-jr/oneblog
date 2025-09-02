plugins {
    id("java")
}

group = "com.oneblog"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

tasks.named<Test>("test") {
    enabled = false
}