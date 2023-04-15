package com.todo.example

import DatabaseFactory
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import com.todo.example.plugins.*

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
}
