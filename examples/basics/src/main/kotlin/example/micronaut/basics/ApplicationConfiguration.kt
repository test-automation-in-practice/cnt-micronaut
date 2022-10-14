package example.micronaut.basics

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.time.Clock

@Factory
class ApplicationConfiguration {

    @Singleton
    fun clock(): Clock = Clock.systemUTC()

    @Singleton
    fun idGenerator() = IdGenerator()
}
