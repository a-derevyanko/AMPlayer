import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}


repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("de.u-mass:lastfm-java:0.1.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.6"
}