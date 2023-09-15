plugins {
    kotlin("jvm")
}

version = "0.1"
group = "example.micronaut"

val kotlinVersion = project.properties["kotlinVersion"]

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    testImplementation("io.mockk:mockk")
    testImplementation("io.kotest:kotest-assertions-core-jvm")
}
