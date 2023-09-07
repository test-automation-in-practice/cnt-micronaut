plugins {
    id("cnt-micronaut.micronaut-conventions")
    id("io.micronaut.test-resources")
}

dependencies {
    implementation("io.micronaut.rabbitmq:micronaut-rabbitmq")
}

application {
    mainClass.set("example.micronaut.ApplicationKt")
}
