plugins {
    kotlin("jvm") version "1.3.72"
    id("java-gradle-plugin")
    kotlin("plugin.serialization") version "1.3.50"
}

group = "com.salmat.android"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("org.simpleframework:simple-xml:2.7.1")
    implementation("com.android.tools.build:gradle:4.0.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}