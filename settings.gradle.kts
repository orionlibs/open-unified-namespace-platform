rootProject.name = "open-uns"
include(":core")
include(":user-service")
include(":documents")
project(":core").projectDir = file("core")
project(":user-service").projectDir = file("user-service")
project(":documents").projectDir = file("documents")
