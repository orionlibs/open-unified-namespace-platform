pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "user-service"

if (gradle.parent == null) {
    includeBuild("../core")
}
