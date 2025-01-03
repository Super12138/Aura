plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
    kotlin("plugin.allopen") version "2.0.20"
    `maven-publish`
}

group = "com.kyant"
version = libs.versions.lib.version.get()

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        freeCompilerArgs.addAll(
            "-Xno-param-assertions",
            "-Xno-call-assertions",
            "-Xno-receiver-assertions",
        )
    }
}
benchmark {
    targets {
        register("test")
    }
}
allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

dependencies {
    implementation("androidx.annotation:annotation:1.9.1")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
    testImplementation(libs.m3color)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.value("sources")
}

afterEvaluate {
    publishing {
        publications {
            register("mavenRelease", MavenPublication::class) {
                artifact(sourcesJar)
                groupId = "com.kyant"
                artifactId = "aura-core"
                version = libs.versions.lib.version.get()
            }
        }
    }
}
