import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadowjar)

    alias(libs.plugins.modrinth)

    alias(libs.plugins.runpaper)

    alias(libs.plugins.hangar)

    `maven-publish`
}

base {
    archivesName.set(rootProject.name)
}

val mcVersion = rootProject.properties["minecraftVersion"] as String

dependencies {
    api(project(":common"))

    implementation(libs.cluster.paper)

    implementation(libs.triumph.cmds)

    implementation(libs.metrics)

    implementation(libs.nbtapi)

    implementation(libs.foliaschedulerwrapper)

    compileOnly(libs.holographicdisplays)

    compileOnly(libs.decentholograms)

    compileOnly(libs.placeholderapi)

    compileOnly(libs.itemsadder)

    compileOnly(libs.oraxen)

    compileOnly("com.sk89q.worldguard", "worldguard-bukkit", "7.1.0-SNAPSHOT")

    compileOnly(fileTree("libs").include("*.jar"))

    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:$mcVersion-R0.1-SNAPSHOT")
}

val isBeta: Boolean get() = rootProject.extra["isBeta"]?.toString()?.toBoolean() ?: false
val type = if (isBeta) "Beta" else "Release"

val description = """
## Fixes:
 * Fix an NPE with item builder

## Other:
 * [Feature Requests](https://github.com/Crazy-Crew/${rootProject.name}/issues)
 * [Bug Reports](https://github.com/Crazy-Crew/${rootProject.name}/issues)
"""

val component: SoftwareComponent = components["java"]

tasks {
    val file = project.layout.buildDirectory.file("libs/${rootProject.name}-${rootProject.version}.jar").get()

    // Publish to hangar.papermc.io.
    hangarPublish {
        publications.register("plugin") {
            version.set("${rootProject.version}")

            id.set(rootProject.name)

            channel.set(type)

            changelog.set(description)

            apiKey.set(System.getenv("hangar_key"))

            platforms {
                register(Platforms.PAPER) {
                    jar.set(file)

                    platformVersions.set(listOf(mcVersion))
                }
            }
        }
    }

    // Publish to modrinth.
    modrinth {
        autoAddDependsOn.set(false)

        token.set(System.getenv("modrinth_token"))

        projectId.set(rootProject.name.lowercase())

        versionName.set("${rootProject.name} ${rootProject.version}")

        versionNumber.set("${rootProject.version}")

        versionType.set(type.lowercase())

        uploadFile.set(file)

        gameVersions.add(mcVersion)

        changelog.set(description)

        loaders.addAll("paper", "purpur")
    }

    // Runs a test server.
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        minecraftVersion(mcVersion)
    }

    // Assembles the plugin.
    assemble {
        dependsOn(reobfJar)
    }

    publishing {
        repositories {
            maven {
                url = uri("https://repo.crazycrew.us/releases/")

                credentials {
                    this.username = System.getenv("GRADLE_USERNAME")
                    this.password = System.getenv("GRADLE_PASSWORD")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = "${rootProject.name.lowercase()}-${project.name.lowercase()}-api"
                version = rootProject.version.toString()

                from(component)
            }
        }
    }

    shadowJar {
        archiveClassifier.set("")

        exclude("META-INF/**")

        listOf(
                "de.tr7zw.changeme.nbtapi",
                "dev.triumphteam.cmd",
                "org.bstats"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }

    processResources {
        val properties = hashMapOf(
                "name" to rootProject.name,
                "version" to rootProject.version,
                "group" to rootProject.group,
                "description" to rootProject.description,
                "apiVersion" to rootProject.properties["apiVersion"],
                "authors" to rootProject.properties["authors"],
                "website" to rootProject.properties["website"]
        )

        inputs.properties(properties)

        filesMatching("plugin.yml") {
            expand(properties)
        }
    }
}