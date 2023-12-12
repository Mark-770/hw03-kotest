import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



val allureVersion = "2.24.0"
// Define the version of AspectJ
val aspectJVersion = "1.9.20.1"

// Define configuration for AspectJ agent
val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
    // Import allure-bom to ensure correct versions of all the dependencies are used
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    // Add necessary Allure dependencies to dependencies section
    testImplementation("io.qameta.allure:allure-junit5")
    // Add aspectjweaver dependency
    agent("org.aspectj:aspectjweaver:${aspectJVersion}")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    //Ko-test
    testImplementation ("io.kotest:kotest-runner-junit5:5.6.0")
    testImplementation ("io.kotest:kotest-assertions-core:5.6.0")
    testImplementation("io.kotest:kotest-property:5.6.0")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "-javaagent:${agent.singleFile}"
    )
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}