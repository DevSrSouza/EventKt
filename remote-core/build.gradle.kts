import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform") version "1.3.71"
    id("maven-publish")
}

kotlin {
    jvm()
    js()

    // TODO: Native

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
            }
        }
    }
}