/*
 * Copyright © 2025 Prasidh Gopal Anchan
 *
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://creativecommons.org/licenses/by-nc-nd/4.0/
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

import AndroidConfig.COMPILE_SDK
import AndroidConfig.JAVA_VERSION
import AndroidConfig.MIN_SDK
import AndroidConfig.TARGET_SDK
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.mca.codemonk"
    compileSdk = COMPILE_SDK

    defaultConfig {
        applicationId = "com.mca.codemonk"
        minSdk = MIN_SDK
        targetSdk = TARGET_SDK
        versionCode = 16
        versionName = "1.0.3"

        testInstrumentationRunner = "com.mca.codemonk.HiltRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "Code Monk Debug")
        }
    }
    compileOptions {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

tasks.register("printVersionName") {
    doLast {
        println("${android.defaultConfig.versionName}")
    }
}

dependencies {

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.realtime.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)

    // Admob
    implementation(libs.play.services.ads)

    // InApp Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson.converter)

    // Navigation Compose
    implementation(libs.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)

    // Instrumentation Test
    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.hilt.test)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.core.test)
    androidTestImplementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":data:remote"))
    implementation(project(":data:repository"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:post"))
    implementation(project(":feature:search"))
    implementation(project(":feature:leaderboard"))
    implementation(project(":feature:notification"))
}