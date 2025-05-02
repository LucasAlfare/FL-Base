@file:Suppress("PropertyName", "SpellCheckingInspection")

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.dokka)
  `maven-publish`
}

group = "com.lucasalfare.flbase"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  // Ktor Server
  api(libs.ktor.server.core)
  api(libs.ktor.server.netty)
  api(libs.ktor.server.content.negotiation)
  api(libs.ktor.server.serialization)
  api(libs.ktor.server.cors)
  api(libs.ktor.server.status.pages)

  // Quick Ktor Client
  api(libs.ktor.client.core)
  api(libs.ktor.client.cio)
  api(libs.ktor.client.content.negotiation)

  // Cryptography
  api(libs.bcrypt)

  // Exposed (SQL Framework)
  api(libs.exposed.core)
  api(libs.exposed.jdbc)

  // Database Drivers
  api(libs.h2)
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

/**
 * Helper block to configure Maven Publishing.
 */
publishing {
  publications {
    create<MavenPublication>("Maven") {
      from(components["kotlin"])
    }
  }
}