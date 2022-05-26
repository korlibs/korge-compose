import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//kotlin.version=1.6.10
//compose.version=1.1.0

plugins {
    //kotlin("jvm") version "1.6.21" apply false
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev686" apply true
    //id("org.jetbrains.compose") version "1.1.0" apply true
    id("com.soywiz.korge") version "2.0.0.999" apply true
    //application
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    add("commonTestImplementation", kotlin("test"))
    //add("commonMainImplementation", "org.jetbrains.compose.runtime:runtime:1.1.0")
    add("commonMainImplementation", "org.jetbrains.compose.runtime:runtime:1.2.0-alpha01-dev686")
    //add("jvmMainImplementation", "org.jetbrains.compose.runtime:runtime-desktop")
    //testImplementation(kotlin("test"))
    //implementation(compose.runtime)
}

//tasks.test { useJUnit() }

//tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

/*
application {
    mainClass.set("MainKt")
}
*/

korge {

}