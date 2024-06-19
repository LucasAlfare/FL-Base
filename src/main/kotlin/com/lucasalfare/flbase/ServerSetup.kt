package com.lucasalfare.flbase

import com.lucasalfare.flbase.database.AppDB
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object ServerSetup {

  fun startWebServer(
    port: Int = 3000,
    setupCallback: Application.() -> Unit = {}
  ) {
    embeddedServer(factory = Netty, port = port) {
      setupCallback()
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
}