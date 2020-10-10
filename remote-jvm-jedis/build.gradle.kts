plugins {
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.serialization") version "1.3.71"
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(Libs.jedis)
    implementation(Libs.kotlinX.serialization.runtimeJvm)
}