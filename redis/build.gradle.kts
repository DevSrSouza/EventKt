plugins {
    kotlin("jvm") version "1.3.71"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":remote-serialization"))

    implementation(Libs.jedis)
    implementation(Libs.kotlinX.serialization.runtimeJvm)
}