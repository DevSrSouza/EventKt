plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
}

kotlin {
    jvm()
    js{
        browser()
        nodejs()
    }

    // TODO: Native

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Libs.kotlinX.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))
                api(Libs.mockK.common)
            }
        }
        val jvmTest by getting {
            dependencies {
                api(kotlin("test"))
                api(kotlin("test-junit"))
                api(Libs.mockK.jvm)
                api(Libs.kotlinX.coroutines.test)
            }
        }
    }
}