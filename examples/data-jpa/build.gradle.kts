plugins {
    id("cnt-micronaut.micronaut-jpa-conventions")
    // this plugin automatically uses TestContainers to provide a postgres database for tests
    id("io.micronaut.test-resources")
}

application {
    mainClass.set("example.micronaut.DataApplicationKt")
}

noArg {
    annotation("example.data.jpa.model.NoArgConstructor")
}
