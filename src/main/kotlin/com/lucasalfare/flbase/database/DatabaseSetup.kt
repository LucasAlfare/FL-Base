@file:Suppress("unused")

package com.lucasalfare.flbase.database

import com.lucasalfare.flbase.Constants
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Initializes the database and sets up the specified tables.
 *
 * This function initializes the database connection using environment variables for the
 * JDBC URL, JDBC driver class name, username, password and maximum pool size. It optionally
 * drops tables on startup and then creates missing tables and columns for the provided tables.
 *
 * @param tables The tables to be initialized in the database.
 * @param dropTablesOnStart If true, drops the specified tables before creating them. The default is false.
 *
 * @example
 * ```
 * // Example usage to initialize the database with User and Order tables, dropping them on start
 * initDatabase(UserTable, OrderTable, dropTablesOnStart = true)
 * ```
 */
fun initDatabase(
  vararg tables: Table,
  dropTablesOnStart: Boolean = false
): String {
  val targetDriverClassName = System.getenv("DB_JDBC_DRIVER") ?: Constants.SQLITE_DRIVER

  AppDB.initialize(
    jdbcUrl = System.getenv("DB_JDBC_URL") ?: Constants.SQLITE_URL,
    jdbcDriverClassName = targetDriverClassName,
    username = System.getenv("DB_USERNAME") ?: "",
    password = System.getenv("DB_PASSWORD") ?: "",
    maximumPoolSize = (System.getenv("DB_POOL_SIZE")?.toInt()) ?: Constants.DEFAULT_MAXIMUM_POOL_SIZE
  ) {
    tables.forEach {
      if (dropTablesOnStart) SchemaUtils.drop(it)
      transaction(AppDB.DB) { SchemaUtils.createMissingTablesAndColumns(it) }
    }
  }

  return targetDriverClassName
}