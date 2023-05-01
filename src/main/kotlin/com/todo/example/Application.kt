package com.todo.example

import DatabaseFactory
import com.fasterxml.jackson.databind.*
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import com.todo.example.plugins.*
import io.ktor.features.*
import io.ktor.jackson.*
import org.flywaydb.core.Flyway.*

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    DatabaseFactory.init()
    configureRouting()
}
