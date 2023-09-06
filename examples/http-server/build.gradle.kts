plugins {
    id("cnt-micronaut.micronaut-conventions")
}

dependencies {
    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
}

application {
    mainClass.set("example.micronaut.ApplicationKt")
}
