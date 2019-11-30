import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset

plugins {
    kotlin("multiplatform") version "1.3.50"
    id("maven-publish")
}

repositories {
    mavenCentral()
}

val ideaActive = System.getProperty("idea.active").toBoolean()
val hostManager = HostManager()

kotlin {
    jvm()
    js()

    if (!ideaActive) {
        linuxX64()
        mingwX64()
        macosX64()
        iosX64()
        iosArm64()
        iosArm32()
    } else {
        targets {
            val linuxPreset = presets.findByName("linuxX64") as KotlinNativeTargetPreset
            val windowsPreset = presets.findByName("mingwX64") as KotlinNativeTargetPreset
            val macPreset = presets.findByName("macosX64") as KotlinNativeTargetPreset

            val ideaPreset = if(hostManager.isEnabled(windowsPreset.konanTarget))
                windowsPreset
            else if(hostManager.isEnabled(macPreset.konanTarget))
                macPreset
            else if(hostManager.isEnabled(linuxPreset.konanTarget))
                linuxPreset
            else null

            targetFromPreset(ideaPreset ?: throw RuntimeException(), "native")
        }

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.2") // metadata
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

        if(!ideaActive) {
            val nativeMain by creating {
                dependsOn(commonMain)
            }

            val sourcesNames = listOf("macosX64Main", "linuxX64Main", "mingwX64Main", "iosX64Main", "iosArm32Main", "iosArm64Main")
            for(sourceName in sourcesNames) {
                getByName(sourceName).dependsOn(nativeMain)
            }

        }
    }
}