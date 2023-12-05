plugins {
    kotlin("jvm") version "1.9.0"
}

group = "me.stepbystep"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}