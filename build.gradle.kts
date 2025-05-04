// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Add local.properties values to project properties
val localProperties = java.util.Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
    localProperties.forEach { key, value ->
        project.extensions.extraProperties[key.toString()] = value
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    alias(libs.plugins.navigation.safeargs) apply false
}

