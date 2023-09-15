plugins {
    id("cnt-micronaut.micronaut-conventions")
}

dependencies {
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.security:micronaut-security-jwt")
    // We use this to have implementations of org.reactivestreams.Publisher
    // which we will use while developing some security related things
    implementation("io.micronaut.reactor:micronaut-reactor")

    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
}

application {
    mainClass.set("example.micronaut.security.SecurityApplicationKt")
}
