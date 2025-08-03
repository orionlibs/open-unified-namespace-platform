pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

rootProject.name = "open-uns"
include(":core", ":user-service", ":user-service-sdk", ":documents")
//include(":uns-cli")

project(":core").projectDir = file("core")
project(":user-service").projectDir = file("user-service")
project(":user-service-sdk").projectDir = file("user-service-sdk")
project(":documents").projectDir = file("documents")
//project(":uns-cli").projectDir = file("uns-cli")
