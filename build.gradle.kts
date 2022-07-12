// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version libs.versions.plugin.android.version apply false
    id("com.android.library") version libs.versions.plugin.android.version apply false
    id("org.jetbrains.kotlin.android") version libs.versions.plugin.kotlin.version apply false
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}