plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
}

kotlin {
    jvm()
    js()

    // TODO: Native

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":remote-core"))
                implementation(Libs.kotlinX.serialization.core)
            }
        }
    }
}