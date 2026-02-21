plugins {
    `java-library`
    id("com.gradleup.shadow") version("9.3.1")
    id("xyz.jpenilla.run-paper") version("3.0.2")
}

group = "org.lushplugins"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://repo.lushplugins.org/snapshots/") // LushLib
}

dependencies {
    // Dependencies
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("me.clip:placeholderapi:2.11.5")

    // Libraries
    implementation("io.github.4drian3d:jdwebhooks:2.0.0")
    implementation("org.lushplugins:LushLib:0.10.86")
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.14")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.14")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    registerFeature("optional") {
        usingSourceSet(sourceSets["main"])
    }

    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    shadowJar {
        minimize()

        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    processResources{
        filesMatching("plugin.yml") {
            expand(project.properties)
        }

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }

    runServer {
        minecraftVersion("1.21.11")

        downloadPlugins {
            modrinth("viaversion", "5.7.1")
            modrinth("viabackwards", "5.7.1")
        }
    }
}
