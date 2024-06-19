package com.lucasalfare.flbase

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureStatusPages() {
  install(StatusPages) {
    exception<AppError> { call, cause ->
      when (cause) {
        is UnavailableDatabaseService -> call.respond(
          cause.status,
          cause.customMessage ?: "UnavailableDatabaseService"
        )

        is BadRequest -> call.respond(cause.status, cause.customMessage ?: "BadRequest")
        is SerializationError -> call.respond(cause.status, cause.customMessage ?: "SerializationError")
        is ValidationError -> call.respond(cause.status, cause.customMessage ?: "ValidationError")
        else -> call.respond(cause.status, cause.customMessage ?: "InternalServerError")
      }
    }
  }
}

fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowMethod(HttpMethod.Delete)
  }
}

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

fun Application.configureStaticHtml(
  vararg pathAndIndex: Pair<String, String> = emptyArray()
) {
  if (pathAndIndex.isNotEmpty()) {
    routing {
      pathAndIndex.forEach {
        staticResources(remotePath = it.first, basePackage = "assets", index = it.second)
      }
    }
  }
}

//fun Application.configureRouting(
//  vararg routesCallbacks: Routing.() -> Unit = emptyArray()
//) {
//  if (routesCallbacks.isNotEmpty()) {
//    routing {
//      routesCallbacks.forEach { it() }
//    }
//  }
//}

fun Application.configureRouting(
  routingCallback: Routing.() -> Unit = {}
) {
  routing {
    routingCallback()
  }
}