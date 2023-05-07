package com.todo.example

import DatabaseFactory
import com.fasterxml.jackson.databind.*
import com.todo.example.service.*
import com.todo.example.web.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import io.ktor.util.*
import org.slf4j.event.*

fun main() {
    embeddedServer(Tomcat, 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
            log.error(cause)
        }
    }

    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        header(HttpHeaders.ContentType)
        host("localhost:3000") // 本番環境では、許可するホストを明示的に指定してください
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }

    install(CallLogging) {
        level = Level.INFO
    }

    DatabaseFactory.init()

    val todoService = TodoService()

    routing {
        get("/") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        todos(todoService)
    }
}
