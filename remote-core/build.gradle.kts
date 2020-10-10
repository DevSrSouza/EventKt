import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }

    // TODO: Native

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
            }
        }
    }
}