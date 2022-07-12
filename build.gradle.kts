import com.android.build.gradle.*

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

subprojects {
    project.plugins.applyBaseConfig(project)
}

fun BaseExtension.baseConfig() {
    compileSdkVersion(32)
    defaultConfig.apply {
        minSdk = 21
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin -> {
                project.extensions
                    .getByType<AppExtension>()
                    .apply {
                        baseConfig()
                    }
            }
            is LibraryPlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        baseConfig()
                    }
            }
        }
    }
}