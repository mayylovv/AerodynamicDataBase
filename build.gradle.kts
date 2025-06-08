plugins {
	java
	id("org.springframework.boot") version "3.2.8"
	id("io.spring.dependency-management") version "1.1.6"
	id("application")
	id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql:42.7.2")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.openjfx:javafx-controls:17")
	implementation("org.openjfx:javafx-fxml:17")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0") // API JAXB
	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.4") // Runtime для JAXB
	implementation("net.objecthunter:exp4j:0.4.8")
	implementation("org.apache.commons:commons-math3:3.6.1")
	implementation ("org.jboss.logging:jboss-logging:3.5.0.Final")
}

application {
	mainClass.set("org.example.demo.CubesatNtuApplication")
//	mainClass.set("org.example.demo.ForGraph")
}
