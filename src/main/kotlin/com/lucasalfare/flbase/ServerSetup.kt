package com.lucasalfare.flbase

import com.lucasalfare.flbase.database.AppDB
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object ServerSetup {

  fun startWebServer(
    port: Int = 3000,
    vararg setupCallbacks: () -> Unit = emptyArray()
  ) {
    embeddedServer(
      factory = Netty,
      port = port
    ) {
      setupCallbacks.forEach { it() }
    }.start(true)
  }

  fun initDatabase(
    vararg tables: Table,
    dropTablesOnStart: Boolean = false
  ) {
    AppDB.initialize(
      jdbcUrl = System.getenv("DB_JDBC_URL") ?: Constants.SQLITE_URL,
      jdbcDriverClassName = System.getenv("DB_JDBC_DRIVER") ?: Constants.SQLITE_DRIVER,
      username = System.getenv("DB_USERNAME") ?: "",
      password = System.getenv("DB_PASSWORD") ?: ""
    ) {
      tables.forEach {
        if (dropTablesOnStart) SchemaUtils.drop(it)
        transaction(AppDB.DB) { SchemaUtils.createMissingTablesAndColumns(it) }
      }
    }
  }

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
    vararg pathAndIndex: Pair<String, String>
  ) {
    routing {
      pathAndIndex.forEach {
        staticResources(remotePath = it.first, basePackage = "assets", index = it.second)
      }
    }
  }

  fun Application.configureRouting(
    vararg routesCallbacks: () -> Unit = emptyArray()
  ) {
    routing {
      routesCallbacks.forEach {
        it()
      }
    }
  }
}