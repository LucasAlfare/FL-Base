@file:Suppress("PropertyName", "SpellCheckingInspection")

plugins {
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.dokka)
  `maven-publish`
}

group = "com.lucasalfare.flbase"
version = "1.2.1"

repositories {
  mavenCentral()
}

dependencies {
  api(libs.bundles.ktorServer)
  api(libs.bundles.ktorClient)
  api(libs.bundles.exposed)
  api(libs.bundles.databases)
  api(libs.bundles.infra)

  testApi(libs.bundles.testing)
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