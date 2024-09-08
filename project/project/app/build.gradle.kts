plugins {
    kotlin("jvm") version "1.8.0"
    application
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.umjammer:jlayer:1.0.3")
}

application {
    mainClass.set("your.package.MainClassKt")
}
