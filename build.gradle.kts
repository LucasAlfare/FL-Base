@file:Suppress("PropertyName", "SpellCheckingInspection")

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
}

group = "com.lucasalfare.flbase"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  // Ktor
  api(libs.ktor.core)
  api(libs.ktor.netty)
  api(libs.ktor.content.negotiation)
  api(libs.ktor.serialization)
  api(libs.ktor.cors)
  api(libs.ktor.status.pages)

  // Cryptography
  api(libs.bcrypt)

  // Exposed (SQL Framework)
  api(libs.exposed.core)
  api(libs.exposed.jdbc)

  // Database Drivers
  api(libs.sqlite)
  api(libs.postgres)

  // HikariCP (Connection Pool)
  api(libs.hikaricp)

  // Logging
  api(libs.logback)

  // Test
  testImplementation(libs.kotlin.test)
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(21)
}