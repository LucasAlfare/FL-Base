# 🚀 FL-Base
![](https://jitpack.io/v/LucasAlfare/FL-Base.svg)

FL-Base is a foundational template for quickly bootstrapping REST APIs and backend servers. Since I frequently use this structure, I've centralized it here to streamline access to dependencies, architecture, and best practices.

**Tech stack:**
- [Ktor](https://github.com/ktorio/ktor) – Fast and lightweight Kotlin web framework
- [Exposed](https://github.com/JetBrains/Exposed) – SQL ORM framework for Kotlin

---

## 📦 How to Use

TODO

---

## 📜 Dependencies
This project declares its dependencies using Gradle's **API scope**, making them **transitive**—accessible to any project that includes FL-Base as a dependency.

Check [libs.versions.toml](gradle/libs.versions.toml) to see the full details of the dependencies.

### Version Catalog Overview

Below are listed a basic overview of what items are included in this project. In practical terms, this means that all these items are available to use if some project include FL-Base as dependency.

- **Kotlin** – JVM and serialization plugins, plus Dokka for documentation.
- **Ktor**
    - **Server** – Core framework, Netty engine, content negotiation, JSON serialization, CORS, status pages, authentication, JWT.
    - **Client** – Core client, CIO engine, content negotiation.
- **Exposed** – Database access (core, JDBC support, Kotlin datetime integration).
- **Databases**
    - **H2** – Lightweight in-memory SQL database.
    - **SQLite** – File-based SQL database.
    - **PostgreSQL** – Production-grade relational database driver.
- **Infrastructure**
    - **BCrypt** – Secure password hashing.
    - **HikariCP** – High-performance JDBC connection pool.
    - **Logback** – Logging framework.
- **Testing**
    - **Ktor Test Host** – Embedded server for integration testing.
    - **Kotlin Test** – Standard Kotlin testing library.

### Dependency Bundles

To simplify inclusion, dependencies are also grouped into bundles:

- **ktorServer** – All core server modules (core, Netty, content negotiation, serialization, CORS, status pages, authentication, JWT).
- **ktorClient** – Core client modules (core, CIO engine, content negotiation).
- **exposed** – Database access modules (core, JDBC, Kotlin datetime).
- **databases** – Database drivers (H2, SQLite, PostgreSQL).
- **infra** – Infrastructure support (BCrypt, HikariCP, Logback).
- **testing** – Testing utilities (Ktor Test Host, Kotlin Test).

---

## 🚀 Getting Started

This project uses the **error throwing pattern**: instead of returning failure results, functions throw exceptions. A middleware (e.g., Ktor’s `StatusPages`) intercepts these and sends appropriate HTTP responses.

As a result, your code only needs to handle successful outcomes. For example, a database query can return an `Entity` directly, not `Result<Entity>`. If a `SomeDatabaseError` occurs, it's automatically handled by the middleware.

---

## 🛠️ Core Components

TODO
