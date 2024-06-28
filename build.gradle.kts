plugins {
    `java-plugin`
}

val isSnapshot = false

rootProject.version = "1.9.2"

val content: String = rootProject.file("CHANGELOG.md").readText(Charsets.UTF_8)

subprojects.filter { it.name != "api" }.forEach {
    it.project.version = rootProject.version
}
