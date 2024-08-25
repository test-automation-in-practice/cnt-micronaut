plugins {
    id("cnt-micronaut.micronaut-conventions")
}

dependencies {
    implementation("io.micronaut:micronaut-http-client")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.1")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("example.micronaut.httpclient.ApplicationKt")
}