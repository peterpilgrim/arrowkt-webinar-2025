plugins {
    kotlin("jvm") version "2.2.20"
    id("io.kotest") version "6.0.4"
    application
}

group = "uk.co.xenonique.javatechcoach"
version = "1.0.0"

repositories {
    mavenCentral()
}

val arrowVersion = "2.1.2"

dependencies {
    // Arrow Core
    implementation("io.arrow-kt:arrow-core:${arrowVersion}")

    // For more advanced FP features (optional, but good to have)
    implementation("io.arrow-kt:arrow-fx-coroutines:${arrowVersion}")
    
    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testImplementation("io.kotest:kotest-framework-engine:6.0.4")
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.0.4")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:6.0.4")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}


application {
    mainClass.set("uk.co.xenonique.javatechcoach.arrow.MainKt")
}
