# 🚀 FL-Base

**A solid and reusable REST API template built with Ktor and Exposed**  

FL-Base is a foundational template for quickly bootstrapping REST APIs. Since I frequently use this structure, I've centralized it here to streamline access to dependencies, architecture, and best practices.  

🔹 **Tech stack:**  
- [Ktor](https://github.com/ktorio/ktor) – Fast and lightweight Kotlin web framework  
- [Exposed](https://github.com/JetBrains/Exposed) – SQL ORM framework for Kotlin  

---

## 📦 How to Use

The best way to integrate this project into other repositories is by leveraging **Gradle Sources Dependency**. This approach allows pulling a Gradle project from a Git repository and using it as a local dependency.  

### 🔧 Setup  

Modify your **`settings.gradle.kts`** to register the repository:  

```kotlin
sourceControl {
  gitRepository(java.net.URI("https://github.com/LucasAlfare/FL-Base")) {
    producesModule("com.lucasalfare.flbase:FL-Base")
  }
}
```

Then, in your `build.gradle.kts`, add the dependency:
```kotlin
implementation("com.lucasalfare.flbase:FL-Base") {
  version {
    branch = "main" // Pulls the latest version from the "main" branch
  }
}
```

---

## 📂 Project Structure
FL-Base follows a **clean and modular structure** to ensure scalability and maintainability.
### 🔹 Features & Highlights
**✅ Dependency injection** – Clean and modular design
**✅ Database abstraction** – Uses Exposed ORM with HikariCP
**✅ Error handling** – Well-structured application errors
**✅ CORS & JSON support** – Pre-configured serialization and API security

## 📜 Dependencies
This project declares its dependencies using Gradle's **API scope**, making them **transitive**—accessible to any project that includes FL-Base as a dependency.

Check build.gradle.kts to see the full list of dependencies.
For versioning details, refer to gradle.properties.

---

## 🚀 Getting Started
To start using FL-Base, ensure your environment is set up correctly:

1️⃣ Database Configuration
Set up the required environment variables for database access:

DB_JDBC_DRIVER – Default: org.sqlite.JDBC
DB_JDBC_URL – Default: jdbc:sqlite:./data.db
DB_USERNAME / DB_PASSWORD – Optional (for non-SQLite databases)
DB_POOL_SIZE – Default: 6

2️⃣ Web Server Configuration

WEB_SERVER_PORT – Default: 80

---

## 🛠️ Core Components

### 🏗️ Database Initialization
This project provides a structured approach to setting up database tables dynamically.
You can initialize the database using:

```kotlin
initDatabase(YourTablesHere, dropTablesOnStart = false)
```

This function:
✅ Establishes a HikariCP connection
✅ Supports automated table creation
✅ Can drop tables on startup if enabled

---

## 🛡️ Error Handling
FL-Base includes a robust error-handling mechanism with custom exception classes:

Exception Class	HTTP Status Code	Description
UnavailableDatabaseService	500 Internal Server Error	Database operation failure
BadRequest	400 Bad Request	Invalid request payload
SerializationError	400 Bad Request	JSON serialization failure
ValidationError	400 Bad Request	Invalid field values
NullEnvironmentVariable	500 Internal Server Error	Missing required environment variable
EmptyEnvironmentVariable	500 Internal Server Error	Environment variable is empty

---

## 🌍 CORS & JSON Configuration
The project comes with pre-configured:
✅ CORS support – Allows API requests from any host
✅ JSON serialization – Uses Kotlinx.serialization with strict parsing
```kotlin
install(CORS) {
  anyHost()
  allowHeader(HttpHeaders.ContentType)
  allowMethod(HttpMethod.Delete)
}
install(ContentNegotiation) {
  json(Json { isLenient = false })
}
```

---

## 🎯 Contributing
Feel free to fork, clone, and contribute to FL-Base! 🚀
For any suggestions, open an issue or submit a pull request.

📧 Contact: @lucas.alfare (Instagram)
