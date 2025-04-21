@file:Suppress("unused")

package com.lucasalfare.flbase.database

import com.lucasalfare.flbase.EnvsLoader.databasePassword
import com.lucasalfare.flbase.EnvsLoader.databasePoolSize
import com.lucasalfare.flbase.EnvsLoader.databaseUrl
import com.lucasalfare.flbase.EnvsLoader.databaseUsername
import com.lucasalfare.flbase.EnvsLoader.driverClassName
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Initializes the database connection and sets up the specified tables.
 *
 * This function sets up the connection to the database using configuration values from environment variables,
 * including JDBC URL, driver class name, username, password, and connection pool size. Once the connection is
 * established, it ensures that the specified tables exist by creating any missing tables or columns.
 *
 * Optionally, it can drop the specified tables before recreating them, which is useful for development or testing
 * environments where a fresh schema is desired on every run.
 *
 * Internally, it uses `AppDB.initialize` to configure the connection pool and transaction handling, and relies on
 * `SchemaUtils.createMissingTablesAndColumns` to update the database schema without data loss.
 *
 * @param tables The database tables to be initialized. These must extend the `Table` class from Exposed.
 * @param dropTablesOnStart Whether to drop the given tables before creating them. Useful for testing. Default is `false`.
 *
 * @example
 * ```
 * // Example usage to initialize the database with UserTable and OrderTable,
 * // dropping them at startup to ensure a clean schema
 * initDatabase(UserTable, OrderTable, dropTablesOnStart = true)
 * ```
 */
fun initDatabase(
  vararg tables: Table,
  dropTablesOnStart: Boolean = false
) {
  AppDB.initialize(
    jdbcUrl = databaseUrl,
    jdbcDriverClassName = driverClassName,
    username = databaseUsername,
    password = databasePassword,
    maximumPoolSize = databasePoolSize.toInt()
  ) {
    tables.forEach {
      if (dropTablesOnStart) SchemaUtils.drop(it) // Drops the table if the flag is set
      transaction(AppDB.DB) {
        // Ensures that any missing tables or columns are created without affecting existing data
        SchemaUtils.createMissingTablesAndColumns(it)
      }
    }
  }
}