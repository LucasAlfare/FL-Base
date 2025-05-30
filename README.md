# 🚀 FL-Base

**A solid and reusable REST API template built with Ktor and Exposed**  

FL-Base is a foundational template for quickly bootstrapping REST APIs and backend servers. Since I frequently use this structure, I've centralized it here to streamline access to dependencies, architecture, and best practices.  

🔹 **Tech stack:**  
- [Ktor](https://github.com/ktorio/ktor) – Fast and lightweight Kotlin web framework  
- [Exposed](https://github.com/JetBrains/Exposed) – SQL ORM framework for Kotlin

---

## 📦 How to Use

TODO

---

## 📜 Dependencies
This project declares its dependencies using Gradle's **API scope**, making them **transitive**—accessible to any project that includes FL-Base as a dependency.

Check [libs.versions.toml](gradle/libs.versions.toml) to see the full list of dependencies.

---

## 🚀 Getting Started

This project uses the **error throwing pattern**: instead of returning failure results, functions throw exceptions. A middleware (e.g., Ktor’s `StatusPages`) intercepts these and sends appropriate HTTP responses.

As a result, your code only needs to handle successful outcomes. For example, a database query can return an `Entity` directly, not `Result<Entity>`. If a `SomeDatabaseError` occurs, it's automatically handled by the middleware. 

---

## 🛠️ Core Components

TODO