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
    compile("net.sourceforge.htmlcleaner:htmlcleaner:2.6.1")
    compile("org.json:json:20180813")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.6"
}