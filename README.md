# FL-Base

FL-Base
This project serves as a raw REST API template. I frequently use this structure, so I am abstracting it into this repository to easily retrieve information about dependencies or even architecture.

We are using [Ktor](https://github.com/ktorio/ktor) as the web server and [Exposed](https://github.com/JetBrains/Exposed) as the SQL Framework ORM.

# How to use

The most convenient way to use this project as a base dependency for other projects is by utilizing the Gradle Sources Dependency feature. This feature allows you to pull other Gradle projects from a Git repository (e.g., GitHub remote) and build them as local dependencies for another repository.

To do this, you need to modify your `settings.gradle.kts` (global Gradle settings script) and the targeted module's `build.gradle.kts` configuration script by adding the following code:

- In `settings.gradle.kts`:
```kotlin
sourceControl {
  gitRepository(java.net.URI("https://github.com/LucasAlfare/FL-Base")) {
    producesModule("com.lucasalfare.flbase:FL-Base")
  }
}
```
- In `build.gradle.kts`:
```kotlin
implementation("com.lucasalfare.flbase:FL-Base") {
  version {
    branch = "main" // convenience to target the branch "main" 
  }
}
```

# Contents

This project declares its dependencies using "api" calls. This means we are defining these dependencies as "transitive", which allows them to be accessible and usable by those who incorporate this repository. You can check [build.gradle.kts](build.gradle.kts) to see the set of dependencies we provide through this project.

Additionally, there are common versioning details specified in the [gradle.properties](gradle.properties) file.