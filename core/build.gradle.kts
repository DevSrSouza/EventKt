import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform") version "1.3.71"
}


val ideaActive = System.getProperty("idea.active").toBoolean()
val hostManager = HostManager()

kotlin {
    jvm()
    js()

    // TODO: Native

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.6") // metadata
            }
        }
        val commonTest by getting {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))
                api("io.mockk:mockk-common:1.9.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                api(kotlin("test"))
                api(kotlin("test-junit"))
                api("io.mockk:mockk:1.9.3")
            }
        }
        val jsMain by getting {
            dependencies {
                api(kotlin("stdlib-js"))
            }
        }
    }
}