plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"

    application
    idea
}

kotlin {
    jvmToolchain(17)
}

group = "vlkv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadJavadoc = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    implementation("io.pebbletemplates:pebble:3.1.6")
    implementation("com.github.ajalt.clikt:clikt:4.2.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("vlkv.HoldMyABKt")
}
