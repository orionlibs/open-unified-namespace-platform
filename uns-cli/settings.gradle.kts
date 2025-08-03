pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "uns-cli"

if (gradle.parent == null) {
    includeBuild("../core")
    includeBuild("../user-service")
    includeBuild("../documents")
}
