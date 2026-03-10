plugins {
    kotlin("jvm") version "2.3.10"
    application
}

group = "railway"
version = "1.0.0"

application {
    mainClass.set("railway.cli.AppKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:5.0.3")

    testImplementation("io.kotest:kotest-runner-junit5:6.1.6")
    testImplementation("io.kotest:kotest-assertions-core:6.1.6")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
