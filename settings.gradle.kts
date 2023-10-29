pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://www.jitpack.io")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://www.jitpack.io")

    }
}

rootProject.name = "MyChat"
include(":app")
include(":data")
include(":domain")
