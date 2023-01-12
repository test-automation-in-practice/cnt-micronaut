plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.6.2"
}

version = "0.1"
group = "example.micronaut"

val kotlinVersion= project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut.cache:micronaut-cache-caffeine")

    testImplementation("io.mockk:mockk")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
}

application {
    mainClass.set("example.micronaut.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

allOpen {
    annotation("jakarta.inject.Singleton")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("example.micronaut.*")
    }
}
