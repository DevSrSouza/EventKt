plugins {
    id("maven-publish")
}

subprojects {
    apply(plugin = "maven-publish")

    group = "br.com.devsrsouza.eventkt"
    version = "0.2.0-SNAPSHOT"

    repositories {
        jcenter()
    }

    afterEvaluate {
        publishing {
            publications.withType<MavenPublication>().configureEach {
                artifactId = "eventkt-${artifactId}"
            }
        }
    }
}
