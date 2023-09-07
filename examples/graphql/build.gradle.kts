plugins {
    id("cnt-micronaut.micronaut-conventions")
}

dependencies {
    implementation("io.micronaut.graphql:micronaut-graphql")

    testImplementation("org.skyscreamer:jsonassert:1.5.1")
    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
}

application {
    mainClass.set("example.micronaut.ApplicationKt")
}
