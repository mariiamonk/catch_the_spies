plugins {
    id("application")
    kotlin("jvm") version ("2.2.20")
}

group = "ds"
version = "1.0-SNAPSHOT"

repositories {

    mavenCentral()

}

kotlin {
    jvmToolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.postgresql:postgresql:42.7.8")
    implementation("org.jetbrains.kotlinx:dataframe:1.0.0-Beta3")
    implementation("org.liquibase:liquibase-core:4.24.0")
    implementation("org.jetbrains.kotlinx:dataframe:1.0.0-Beta3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1")
    implementation("com.fasterxml.woodstox:woodstox-core:6.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
}

tasks.test {
    useJUnitPlatform()
}