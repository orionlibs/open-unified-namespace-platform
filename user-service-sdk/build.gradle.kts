plugins {
    `java-library`
    //`kotlin-dsl`
    id("org.openapi.generator") version "7.13.0"
}

group = "io.github.orionlibs"
version = "0.0.1"

dependencies {
  implementation("com.squareup.okhttp3:okhttp:5.1.0")
}

tasks.named("openApiGenerate") {
  dependsOn(":user-service:generateOpenApiDocs")
}

openApiGenerate {
  inputSpec.set(file("../user-service/build/openapi/openapi.json")
    .toURI()
    .toString())
  generatorName.set("java")
  library.set("okhttp-gson")
  outputDir.set("$projectDir/sdk-src")
  apiPackage.set("io.github.orionlibs.sdk.user.api")
  modelPackage.set("io.github.orionlibs.sdk.user.model")
  invokerPackage.set("io.github.orionlibs.sdk.user.invoker")
  configOptions.set(
    mapOf(
      "dateLibrary" to "java8",
      "useTags" to "true"
    )
  )
}

sourceSets {
  main {
    java {
      srcDir("$buildDir/generated/src/main/java")
    }
  }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Werror"))
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}
