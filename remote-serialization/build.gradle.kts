plugins {
    kotlin("multiplatform") version "1.3.71"
    kotlin("plugin.serialization") version "1.3.71"
    id("maven-publish")
}

kotlin {
    jvm()
    js()

    // TODO: Native

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":remote-core"))
                implementation(Libs.kotlinX.serialization.runtimeNative) // metadata
                implementation(Libs.kotlinX.serialization.protoBufNative)
            }
        }
    }
}