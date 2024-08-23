plugins {
    id("cnt-micronaut.micronaut-conventions")
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("io.micronaut.kafka:micronaut-kafka")
}

application {
    mainClass.set("example.micronaut.ApplicationKt")
}
