import java.util.Properties

plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main'
    // that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // for various plugins
    gradlePluginPortal()
}

// We need to manually get access to ../gradle.properties. Out of reasons unknown to the author
// buildSrc is completely separated from the rest of the build. But I don't want to keep to
// locations in sync. At least as long as the current approach does not cause disadvantages.
val gradleProperties = Properties()
gradleProperties.load(projectDir.resolve("../gradle.properties").inputStream())
val kotlinVersion = gradleProperties["kotlinVersion"]

dependencies {
    // to find these
    // 1. open https://plugins.gradle.org
    // 2. search for your plugin via its id
    // You will find the necessary implementation dependency in the block called
    // _Using legacy plugin application_ in the dependencies block.
    // You need everything inside _classpath(...)_
    // If your plugin is not part of the gradle plugin portal, you need to find the correct values
    // somehow.
    implementation ("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation ("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
    implementation ("org.jetbrains.kotlin:kotlin-noarg:${kotlinVersion}")
    implementation ("com.github.johnrengelman:shadow:8.1.1")
    implementation ("io.micronaut.gradle:micronaut-gradle-plugin:4.0.3")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:${kotlinVersion}-1.0.19")
}
