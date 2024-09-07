plugins {
    id("com.gradleup.shadow").version("8.3.0")
    id("java")
    kotlin("jvm")
}

group = "kr.tpmc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
    maven("https://jitpack.io")
    maven("https://repo.lushplugins.org/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.lushplugins:ChatColorHandler:3.1.0")
    implementation("io.github.monun:kommand-api:3.1.7")
    implementation("com.github.SkriptLang:Skript:2.9.0")
    implementation("io.github.monun:invfx-api:3.3.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    implementation("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit")
    }
    implementation(project(":SimplePlayer"))
}
kotlin {
    jvmToolchain(21)
}
tasks.shadowJar {
    // Specify where to place the final JAR
    destinationDirectory.set(File("/home/sorayumi/Files/1.20.1/plugins"))

    // Include the output from the SimplePlayer project
    from(project(":SimplePlayer").sourceSets.main.get().output)

    // Bundle the specific dependency into the final JAR
    dependencies {
        include(dependency("org.lushplugins:ChatColorHandler:3.1.0"))
    }
}
tasks.jar {
//    destinationDirectory = File("/home/sorayumi/Files/1.20.1/plugins")
//
//    from(project(":SimplePlayer").sourceSets.main.get().output)
    dependsOn(tasks.shadowJar)
    enabled = false
}