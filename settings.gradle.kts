pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "TPGuild"

include("SimplePlayer")
project(":SimplePlayer").projectDir = file("/home/sorayumi/JavaProjects/SimplePlayer")
