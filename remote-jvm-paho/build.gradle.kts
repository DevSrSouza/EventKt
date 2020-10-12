plugins {
    kotlin("jvm") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion // test
}

repositories {
    maven(Repos.eclipsePaho)
}

dependencies {
    api(kotlin("stdlib"))
    api(project(":remote-core"))

    api(Libs.pahoMqtt)

    // test
    testImplementation(project(":remote-encoder-serialization"))
    testImplementation(Libs.kotlinX.serialization.protoBuf)
}