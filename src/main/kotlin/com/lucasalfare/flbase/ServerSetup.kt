@file:Suppress("unused")

package com.lucasalfare.flbase

import com.lucasalfare.flbase.EnvsLoader.webServerPort
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * Starts a web server using the Netty engine.
 *
 * This function creates and starts an embedded HTTP server using the [Netty] engine,
 * listening on the specified port. It leverages Ktor's [embeddedServer] function, which
 * handles the server infrastructure, installs plugins, and manages the application lifecycle.
 *
 * The [setupCallback] parameter allows configuring the application behavior—such as routes,
 * middlewares, serialization, status pages, etc.—by providing a lambda with [Application] as receiver.
 *
 * Calling `.start(true)` launches the server in blocking mode, which keeps the main thread active
 * until the server is shut down manually.
 *
 * - Example:
 * ```kotlin
 * // Example usage with custom routing
 * startWebServer(port = 8080) {
 *     configureRouting {
 *         get("/") {
 *             call.respondText("Hello, World!")
 *         }
 *     }
 * }
 * ```
 *
 * @param port The port the server will listen on. Defaults to the value of `webServerPort`
 *             from the environment loader [EnvsLoader].
 * @param setupCallback A lambda with [Application] receiver to configure server behavior
 *                      (e.g., routes, status pages, CORS).
 */
fun startWebServer(
  port: Int = webServerPort.toInt(),
  setupCallback: Application.() -> Unit = {}
) {
  embeddedServer(factory = Netty, port = port) {
    setupCallback()
  }.start(true)
}