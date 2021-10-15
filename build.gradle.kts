import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    application
    idea
}

group = "me.veelkoov"
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
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("io.pebbletemplates:pebble:3.1.5")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.4")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.31")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("vlkv.HoldMyAB")
}
