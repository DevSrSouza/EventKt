plugins {
    kotlin("jvm") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion // test
}

dependencies {
    api(kotlin("stdlib"))
    api(project(":remote-core"))

    api(Libs.jedis)

    // test
    testImplementation(project(":remote-encoder-serialization"))
    testImplementation(Libs.kotlinX.serialization.json)
}