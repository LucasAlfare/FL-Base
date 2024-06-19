@file:Suppress("PropertyName", "SpellCheckingInspection")

val ktor_version: String by project

plugins {
  kotlin("jvm") version "2.0.0"
}

group = "com.lucasalfare.flbase"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  // Ktor (base and engine)
  api("io.ktor:ktor-server-core:$ktor_version")
  api("io.ktor:ktor-server-netty:$ktor_version")

  // Serialization
  api("io.ktor:ktor-server-content-negotiation:$ktor_version")
  api("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

  // CORS...
  api("io.ktor:ktor-server-cors:$ktor_version")

  // StatusPages plugin
  api("io.ktor:ktor-server-status-pages:$ktor_version")

  // Cryptography
  api("org.mindrot:jbcrypt:0.4")

  // SQL Framework Exposed
  api("org.jetbrains.exposed:exposed-core:0.48.0")

  /*
  // SQLite dependencies
  Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
  TransactionManager
    .manager
    .defaultIsolationLevel = Connection
      .TRANSACTION_SERIALIZABLE
   */
  api("org.xerial:sqlite-jdbc:3.45.2.0")

  // Postgres dependency
  api("org.postgresql:postgresql:42.7.3")

  // HikariCP (ConnectionPool)
  api("com.zaxxer:HikariCP:5.1.0")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}