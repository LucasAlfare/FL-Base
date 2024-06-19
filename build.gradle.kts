@file:Suppress("PropertyName", "SpellCheckingInspection")

val ktor_version: String by project

plugins {
  kotlin("jvm") version "2.0.0"
}

group = "com.lucasalfare"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  // Ktor (base and engine)
  implementation("io.ktor:ktor-server-core:$ktor_version")
  implementation("io.ktor:ktor-server-netty:$ktor_version")

  // Serialization
  implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

  // CORS...
  implementation("io.ktor:ktor-server-cors:$ktor_version")

  // StatusPages plugin
  implementation("io.ktor:ktor-server-status-pages:$ktor_version")

  // Cryptography
  implementation("org.mindrot:jbcrypt:0.4")

  // SQL Framework Exposed
  implementation("org.jetbrains.exposed:exposed-core:0.48.0")

  /*
  // SQLite dependencies
  Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
  TransactionManager
    .manager
    .defaultIsolationLevel = Connection
      .TRANSACTION_SERIALIZABLE
   */
  implementation("org.xerial:sqlite-jdbc:3.45.2.0")

  // Postgres dependency
  implementation("org.postgresql:postgresql:42.7.3")

  // HikariCP (ConnectionPool)
  implementation("com.zaxxer:HikariCP:5.1.0")
}

kotlin {
  jvmToolchain(17)
}