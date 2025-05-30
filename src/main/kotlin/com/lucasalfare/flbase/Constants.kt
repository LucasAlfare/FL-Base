package com.lucasalfare.flbase

/**
 * A utility class that holds constant values used throughout the application.
 *
 * This class centralizes constants related to SQLite database configuration and web server setup.
 * Keeping these values in a single place improves maintainability and readability.
 */
class Constants {
  companion object {
    /**
     * The URL for the SQLite database connection.
     *
     * This URL tells the JDBC driver to use SQLite and points to a database file named `data.db`
     * located in the root directory of the application. If the file does not exist, SQLite will create it.
     *
     * Format: `jdbc:sqlite:[path]` is the standard URI for SQLite via JDBC.
     */
    const val SQLITE_URL = "jdbc:sqlite:./data.db"

    /**
     * The fully qualified class name of the SQLite JDBC driver.
     *
     * This class must be present in the classpath so the DriverManager can locate it when a
     * connection is requested using the `SQLITE_URL`.
     * Typically, the driver is automatically registered when the class is loaded.
     */
    const val SQLITE_DRIVER = "org.sqlite.JDBC"

    /**
     * The default maximum number of connections in the database connection pool.
     *
     * Connection pools help manage database resources efficiently by reusing connections.
     * If no custom pool size is defined, this default is used to avoid exhausting resources
     * while supporting concurrent operations.
     */
    const val DEFAULT_MAXIMUM_POOL_SIZE = 3

    /**
     * The default port number used by the embedded web server.
     *
     * Port 3000 is the default HTTP port, allowing the application to serve web requests
     */
    const val DEFAULT_WEB_SERVER_PORT = 3000

    /**
     * Default dummy jwt algorithm secret.
     */
    const val JWT_ALGORITHM_SIGN_SECRET = "JWT_ALGORITHM_SIGN_SECRET"

    const val DEFAULT_JWT_EXPIRATION_TIME = 5
  }
}