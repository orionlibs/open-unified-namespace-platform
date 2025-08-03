pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "documents"
//include("src")

if (gradle.parent == null) {
    includeBuild("../../libs/core") {
        dependencySubstitution {
            substitute(module("io.github.orionlibs:core"))
                .using(project(":"))
        }
    }
}
