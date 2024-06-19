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

/**
 * Configures status pages for handling exceptions in the application.
 *
 * This function installs the `StatusPages` feature and defines exception handlers
 * for various types of `AppError` subclasses, responding with appropriate HTTP status codes
 * and custom messages.
 */
fun Application.configureStatusPages() {
  val t = this
  install(StatusPages) {
    exception<AppError> { call, cause ->
      when (cause) {
        is UnavailableDatabaseService -> {
          t.log.error("Database service unavailable: ${cause.customMessage}")
          call.respond(
            cause.status,
            cause.customMessage ?: "UnavailableDatabaseService"
          )
        }

        is BadRequest -> {
          t.log.error("Bad request: ${cause.customMessage}")
          call.respond(cause.status, cause.customMessage ?: "BadRequest")
        }

        is SerializationError -> {
          t.log.error("Serialization error: ${cause.customMessage}")
          call.respond(cause.status, cause.customMessage ?: "SerializationError")
        }

        is ValidationError -> {
          t.log.error("Validation error: ${cause.customMessage}")
          call.respond(cause.status, cause.customMessage ?: "ValidationError")
        }

        else -> {
          t.log.error("Unexpected error: ${cause.customMessage}")
          call.respond(cause.status, cause.customMessage ?: "InternalServerError")
        }
      }
    }
  }
}

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application.
 *
 * This function installs the `CORS` feature, allowing requests from any host
 * and enabling specific headers and methods.
 */
fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowMethod(HttpMethod.Delete)
  }
}

/**
 * Configures content negotiation for serialization and deserialization of JSON.
 *
 * This function installs the `ContentNegotiation` feature with JSON support,
 * setting the leniency to `false`.
 */
fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

/**
 * Configures static HTML serving for specified paths and index files.
 *
 * @param pathAndIndex A vararg parameter of pairs, where each pair consists of a remote path
 *                     and the corresponding index file. The function sets up routing to serve
 *                     static resources from the specified paths.
 */
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

/**
 * Configures routing for the application.
 *
 * @param routingCallback A lambda function to define custom routing logic.
 *                        This function sets up the routing using the provided callback.
 */
fun Application.configureRouting(
  routingCallback: Routing.() -> Unit = {}
) {
  routing {
    routingCallback()
  }
}