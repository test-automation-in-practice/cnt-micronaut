plugins {
    id("cnt-micronaut.micronaut-conventions")
    // this plugin automatically uses TestContainers to provide a postgres database for tests
    id("io.micronaut.test-resources")
    kotlin("plugin.jpa") version "1.8.22"
    kotlin("plugin.noarg") version "1.8.22"
}

dependencies {
    ksp("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    runtimeOnly("org.postgresql:postgresql")
}

application {
    mainClass.set("example.micronaut.DataApplicationKt")
}

noArg {
    annotation("example.data.jpa.model.NoArgConstructor")
}
