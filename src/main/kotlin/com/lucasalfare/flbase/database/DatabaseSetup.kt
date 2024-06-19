package com.lucasalfare.flbase.database

import com.lucasalfare.flbase.Constants
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

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