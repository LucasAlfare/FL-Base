@file:Suppress("unused")

package com.lucasalfare.flbase

/**
 * Represents a single environment variable, providing optional fallback values or throwing errors based on configuration.
 *
 * This class simplifies loading and validating environment variables in a consistent and declarative way.
 *
 * @property name The name of the environment variable to retrieve.
 * @property defaultWhenNull The fallback value used if the environment variable is not set (`null`).
 * @property defaultWhenEmpty The fallback value used if the environment variable is set but empty (`""`).
 * @property throwWhenNull Whether to throw [NullEnvironmentVariable] if the variable is `null`.
 * @property throwWhenEmpty Whether to throw [EmptyEnvironmentVariable] if the variable is an empty string.
 *
 * @throws NullEnvironmentVariable If `throwWhenNull` is true and the variable is not set.
 * @throws EmptyEnvironmentVariable If `throwWhenEmpty` is true and the variable is empty.
 *
 * @constructor Creates a new [EnvValue] and resolves its value immediately upon initialization.
 *
 * @example
 * ```
 * val port = EnvValue("PORT", "8080", "8080").toString()
 * val dbUser = EnvValue("DB_USER", "", "", throwWhenNull = true).toString()
 * ```
 */
data class EnvValue(
  private val name: String,
  private val defaultWhenNull: String,
  private val defaultWhenEmpty: String,
  private val throwWhenNull: Boolean = false,
  private val throwWhenEmpty: Boolean = false
) {
  private val value = System.getenv(name).let {
    if (it == null) {
      if (throwWhenNull)
        throw NullEnvironmentVariable("Server environment variable [$name] is missing/null")
      defaultWhenNull
    } else if (it.isEmpty()) {
      if (throwWhenEmpty)
        throw EmptyEnvironmentVariable("Server environment variable [$name] is missing/empty")
      defaultWhenEmpty
    } else {
      it
    }
  }

  /**
   * Returns the resolved value as a string.
   */
  override fun toString() = value
}

/**
 * Provides a centralized utility to load and manage commonly used environment variables in the application.
 *
 * All values are initialized at object creation, making them immediately accessible as immutable properties.
 *
 * Includes a convenience method for loading custom environment variables with fallback options.
 *
 * @property driverClassName JDBC driver name (e.g., `org.sqlite.JDBC`).
 * @property databaseUrl JDBC URL to connect to the database.
 * @property databaseUsername Optional username for DB access.
 * @property databasePassword Optional password for DB access.
 * @property databasePoolSize Max number of database connections (as a string).
 * @property webServerPort Port number the web server should bind to.
 */
object EnvsLoader {
  internal val driverClassName = EnvValue("DB_JDBC_DRIVER", Constants.SQLITE_DRIVER, Constants.SQLITE_DRIVER).toString()
  internal val databaseUrl = EnvValue("DB_JDBC_URL", Constants.SQLITE_URL, Constants.SQLITE_URL).toString()
  internal val databaseUsername = EnvValue("DB_USERNAME", "", "").toString()
  internal val databasePassword = EnvValue("DB_PASSWORD", "", "").toString()
  internal val databasePoolSize = EnvValue(
    "DB_POOL_SIZE",
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString(),
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString()
  ).toString()
  internal val webServerPort = EnvValue(
    "WEB_SERVER_PORT",
    Constants.DEFAULT_WEB_SERVER_PORT.toString(),
    Constants.DEFAULT_WEB_SERVER_PORT.toString()
  ).toString()

  /**
   * Loads a custom environment variable with fallback values and optional error throwing.
   *
   * @param name The name of the environment variable.
   * @param defaultWhenNull The fallback if the variable is `null`.
   * @param defaultWhenEmpty The fallback if the variable is an empty string.
   * @param throwWhenNull If true, throws [NullEnvironmentVariable] when the variable is not set.
   * @param throwWhenEmpty If true, throws [EmptyEnvironmentVariable] when the variable is empty.
   * @return The resolved environment variable value as a string.
   */
  fun customLoad(
    name: String,
    defaultWhenNull: String,
    defaultWhenEmpty: String,
    throwWhenNull: Boolean = false,
    throwWhenEmpty: Boolean = false
  ) = EnvValue(
    name, defaultWhenNull, defaultWhenEmpty, throwWhenNull, throwWhenEmpty
  ).toString()
}