package com.lucasalfare.flbase

data class EnvValue(
  private val name: String,
  private val defaultWhenNull: String,
  private val defaultWhenEmpty: String
) {

  private var value = System.getenv(name).let {
    if (it == null) {
      defaultWhenNull
    } else if (it.isEmpty()) {
      defaultWhenEmpty
    } else {
      it
    }
  }

  override fun toString() = value
}

object EnvsLoader {
  val driverClassName = EnvValue("DB_JDBC_DRIVER", Constants.SQLITE_DRIVER, Constants.SQLITE_DRIVER)
  val databaseUrl = EnvValue("DB_JDBC_URL", Constants.SQLITE_URL, Constants.SQLITE_URL)
  val databaseUsername = EnvValue("DB_USERNAME", "", "")
  val databasePassword = EnvValue("DB_PASSWORD", "", "")
  val databasePoolSize = EnvValue(
    "DB_POOL_SIZE",
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString(),
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString()
  )
}