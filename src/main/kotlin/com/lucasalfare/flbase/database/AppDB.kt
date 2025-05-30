@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.lucasalfare.flbase.database

import com.lucasalfare.flbase.Constants
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Object responsible for managing the application's database connection using HikariCP and Exposed.
 *
 * This singleton encapsulates database initialization and query execution logic. It sets up a
 * HikariCP connection pool and binds it to the Exposed ORM, providing a streamlined and reusable
 * database layer for the entire application.
 *
 * Responsibilities:
 * - Initialize and configure the database connection pool.
 * - Provide access to the Exposed `Database` instance.
 * - Support suspended transactional queries for coroutine-friendly operations.
 *
 * @note There's a TODO to abstract Exposed-specific logic in future iterations.
 */
object AppDB {

  /**
   * The HikariDataSource instance used for managing physical database connections.
   *
   * Initialized in the `initialize()` method. This pool manages connection reuse, limiting the
   * number of simultaneous connections and improving performance under load.
   */
  private lateinit var hikariDataSource: HikariDataSource

  /**
   * Lazily initialized Exposed `Database` instance.
   *
   * This is the database handle used internally by Exposed for transactions. It is connected
   * to the HikariDataSource and created only on first access.
   */
  val DB by lazy { Database.connect(hikariDataSource) }

  /**
   * Initializes the database connection using the provided parameters.
   *
   * This method sets up the HikariCP configuration with the given credentials and settings,
   * and then binds the resulting `HikariDataSource` to the Exposed `Database` instance.
   * It also runs a first transaction to validate the connection and optionally perform schema setup.
   *
   * @param jdbcUrl The JDBC URL for the database.
   * @param jdbcDriverClassName The fully qualified class name of the JDBC driver.
   * @param username The database user name.
   * @param password The user password.
   * @param maximumPoolSize The maximum number of connections in the pool.
   * @param onFirstTransactionCallback Callback executed inside the first transaction (e.g. for schema creation).
   */
  fun initialize(
    jdbcUrl: String,
    jdbcDriverClassName: String,
    username: String,
    password: String,
    maximumPoolSize: Int,
    onFirstTransactionCallback: () -> Unit = {}
  ) {
    // Set up the HikariCP connection pool with the given configuration
    hikariDataSource = createHikariDataSource(
      jdbcUrl = jdbcUrl,
      jdbcDriverClassName = jdbcDriverClassName,
      username = username,
      password = password,
      maximumPoolSize = maximumPoolSize
    )

    // Open an initial transaction to validate the setup and run the callback
    transaction(db = DB) {
      onFirstTransactionCallback()
    }
  }

  /**
   * Executes a suspended database query block inside a coroutine-friendly transaction.
   *
   * This method uses Exposed's `newSuspendedTransaction` with `Dispatchers.IO`, allowing database
   * operations to be performed in a non-blocking, coroutine-safe context.
   *
   * @param T The return type of the query block.
   * @param queryCodeBlock The suspended code block to be executed within the transaction.
   * @return The result of the query execution.
   */
  suspend fun <T> dbTransaction(queryCodeBlock: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = DB) {
      queryCodeBlock()
    }

  /**
   * Creates and configures a `HikariDataSource` with the provided connection parameters.
   *
   * This method sets important properties for connection pooling such as transaction isolation,
   * maximum pool size, autocommit behavior, and driver validation.
   *
   * @param jdbcUrl The JDBC URL to connect to.
   * @param jdbcDriverClassName The JDBC driver class.
   * @param username The database username.
   * @param password The database password.
   * @param maximumPoolSize Maximum number of connections allowed in the pool.
   * @return A fully configured and validated `HikariDataSource`.
   */
  private fun createHikariDataSource(
    jdbcUrl: String,
    jdbcDriverClassName: String,
    username: String,
    password: String,
    maximumPoolSize: Int,
  ): HikariDataSource {
    // Build the Hikari configuration
    val hikariConfig = HikariConfig().apply {
      // SQLite requires SERIALIZABLE isolation for proper transactional behavior
      if (jdbcDriverClassName == Constants.SQLITE_DRIVER)
        this.transactionIsolation = IsolationLevel.TRANSACTION_SERIALIZABLE.name

      this.jdbcUrl = jdbcUrl
      this.driverClassName = jdbcDriverClassName
      this.username = username
      this.password = password
      this.maximumPoolSize = maximumPoolSize
      this.isAutoCommit = true // Allows simple usage unless manual control is needed

      this.validate() // Validates the config before creating the data source
    }

    // Return the configured connection pool
    return HikariDataSource(hikariConfig)
  }
}