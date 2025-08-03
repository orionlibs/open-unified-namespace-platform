pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "documents"
//include("src")

if (gradle.parent == null) {
    includeBuild("../core")
}
