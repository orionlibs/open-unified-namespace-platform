plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.orionlibs"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

tasks.withType<JavaCompile> {
    dependsOn("buildUserServiceProject")
    dependsOn("buildDocumentsProject")
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Werror"))
}

tasks.register("buildUserServiceProject", Exec::class) {
    workingDir("../user-service")
    // For Windows, you might need to use "cmd", "/c", "gradlew.bat", ...
    commandLine("sh", "-c", "./gradlew clean build")
    description = "Builds the 'user-service' project."
}

tasks.register("buildDocumentsProject", Exec::class) {
    workingDir("../documents")
    // For Windows, you might need to use "cmd", "/c", "gradlew.bat", ...
    commandLine("sh", "-c", "./gradlew clean build")
    description = "Builds the 'documents' project."
}

repositories {
	mavenLocal()
    mavenCentral()
}

val isMonorepoContext = gradle.parent != null

extra["springShellVersion"] = "3.4.0"

dependencies {
	implementation("org.springframework.shell:spring-shell-starter")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-json")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	if (isMonorepoContext) {
        implementation(project(":core"))
		implementation(project(":user-service"))
		implementation(project(":documents"))
    } else {
        implementation("io.github.orionlibs:core:0.0.1")
		implementation("io.github.orionlibs:user-service:0.0.1")
		implementation("io.github.orionlibs:documents:0.0.1")
    }

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.shell:spring-shell-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.shell:spring-shell-dependencies:${property("springShellVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
