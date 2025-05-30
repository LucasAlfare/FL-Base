@file:Suppress("unused")

package com.lucasalfare.flbase

import com.lucasalfare.flbase.auth.JwtGenerator
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
 * This function installs the [StatusPages] plugin, which intercepts unhandled exceptions during
 * HTTP request processing. It inspects the root cause of the exception using [myRootCause], and:
 *
 * - If it’s an instance of [AppError], it logs the error and returns an HTTP response with
 *   the status and custom message defined by the exception.
 * - If it’s any other type of exception, it logs it as unexpected and returns
 *   [HttpStatusCode.InternalServerError] with the message.
 *
 * This setup ensures consistent error handling and user-friendly responses for known error types.
 */
fun Application.configureStatusPages() {
  val applicationRef = this
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      when (val root = cause.myRootCause()) {
        is AppError -> {
          applicationRef.log.error("\nCaught root AppError in server -> $root: ${root.customMessage}\n")

          if (root.parent != null) {
            applicationRef.log.error("\nAppError parent throwable -> ${root.parent}: ${root.parent.message}\n")
          }

          call.respond(
            status = root.status,
            message = "$root: ${root.customMessage}"
          )
        }

        else -> {
          applicationRef.log.error("Unhandled error caught:\n$cause: ${cause.message}")
          // TODO: avoid responding the real error!
          call.respond(HttpStatusCode.InternalServerError, "$cause: ${cause.message}")
        }
      }
    }
  }
}

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application.
 *
 * This function installs the [CORS] plugin, which defines the rules for allowing cross-origin HTTP requests.
 * It configures the server to:
 *
 * - Accept requests from any host using [anyHost()] function
 * - Allow the `Content-Type` header in requests
 * - Allow the HTTP DELETE method explicitly
 *
 * This is useful when building APIs or frontend-backend systems that operate on different domains.
 */
fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowMethod(HttpMethod.Delete)
  }
}

/**
 * Configures content negotiation for JSON serialization and deserialization.
 *
 * This function installs the [ContentNegotiation] plugin with [kotlinx.serialization.json.Json] as the format.
 * It sets the behavior of the JSON parser as follows:
 *
 * - `isLenient = false`: Disables lenient parsing, ensuring strict compliance with JSON format.
 * - `ignoreUnknownKeys = true`: Allows incoming JSON to contain unknown fields without throwing errors.
 *
 * This configuration ensures that the server communicates using well-structured JSON,
 * while remaining tolerant to extra fields from clients.
 */
fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json {
      isLenient = false
      ignoreUnknownKeys = true
    })
  }
}

/**
 * Configures static HTML serving for specified routes.
 *
 * This function defines static routes using the [staticResources] block. It uses `assets` as the base resource path.
 *
 * @param pathAndIndex A list of pairs where:
 * - The first value is the public route path.
 * - The second is the name of the default index file to serve in that path (e.g. "index.html").
 *
 * For example, passing `"/docs" to "index.html"` means that visiting `/docs` in the browser will
 * serve `/assets/index.html` from the application's resources.
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
 * Configures routing for the application by applying the user-defined callback.
 *
 * This function calls [routing] and applies the provided [routingCallback] to define routes.
 * It acts as a wrapper to organize route logic in a cleaner and more modular way.
 *
 * @param routingCallback A lambda with a [Routing] receiver where routes are defined.
 *
 * @example
 * ```
 * configureRouting {
 *   get("/") { call.respondText("Hello, world!") }
 * }
 * ```
 */
fun Application.configureRouting(routingCallback: Routing.() -> Unit = {}) {
  routing {
    routingCallback()
  }
}

/**
 * Configures JWT-based authentication for the Ktor application.
 *
 * This function installs the [Authentication] feature and sets up JWT authentication using the verifier
 * provided by [JwtGenerator.verifier]. It also supports a custom token validation callback.
 *
 * @param onReceivedJwtCallback A callback function that receives the raw JWT token (if any) extracted
 * from the "Authorization" header. It should return a [JWTPrincipal] if the token is valid, or null otherwise.
 */
fun Application.configureJwtAuth(
  onReceivedJwtCallback: (receivedToken: String?) -> JWTPrincipal? = { null }
) {
  install(Authentication) {
    jwt {
      verifier(verifier = JwtGenerator.verifier)
      validate { jwtCredential ->
        val theToken = this.request.headers["Authorization"]?.removePrefix("Bearer ")
        return@validate onReceivedJwtCallback(theToken)
      }
    }
  }
}

/**
 * Recursively finds the root cause of a [Throwable].
 *
 * This function works similarly to Ktor’s internal `rootCause`, but avoids using Ktor’s
 * `@InternalAPI`-annotated version to ensure future compatibility.
 *
 * It recursively follows the `cause` chain until it finds the original exception.
 *
 * @return The deepest [Throwable] in the cause chain.
 */
fun Throwable.myRootCause(): Throwable {
  return if (cause == null) this else cause!!.myRootCause()
}