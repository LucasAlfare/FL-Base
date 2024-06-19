package com.lucasalfare.flbase

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun startWebServer(
  port: Int = 3000,
  setupCallback: Application.() -> Unit = {}
) {
  embeddedServer(factory = Netty, port = port) {
    setupCallback()
  }.start(true)
}