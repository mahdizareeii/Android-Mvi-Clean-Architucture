plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)

    kotlin("kapt")
    id(libs.plugins.dagger.hilt.android.plugin.get().pluginId)

    kotlin(libs.plugins.serialization.get().pluginId)
}

dependencies {
    implementation(project(":primary:core"))
    implementation(libs.androidx.core.ktx)
    //hilt
    implementation(libs.com.google.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.compiler)

    implementation(libs.kotlinx.serialization)
}