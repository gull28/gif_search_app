plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android") version "2.51.1"
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.gif_search_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gif_search_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GIPHY_API_KEY", "\"${project.findProperty("GIPHY_API_KEY") ?: "default_api_key"}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            buildConfigField("String", "GIPHY_API_KEY", "\"${project.findProperty("GIPHY_API_KEY") ?: "default_api_key"}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.play.services.tasks)
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.common)
    kapt(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)

    val nav_version = "2.8.5"

    implementation(libs.hilt.android)
    implementation(libs.volley)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)


    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.gif)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.accompanist.systemuicontroller)


    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.paging.common)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.androidx.core.testing)

    testImplementation(libs.kotlinx.coroutines.test.v164)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.core.ktx)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}