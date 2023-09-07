plugins {
    id("cnt-micronaut.micronaut-conventions")
}

dependencies {
    implementation("io.micronaut.cache:micronaut-cache-caffeine")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
}

application {
    mainClass.set("example.micronaut.ApplicationKt")
}
