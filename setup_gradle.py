import os

# ─── Root build.gradle.kts ───────────────────────────────────────────────────
root_gradle = """
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}
""".strip()

# ─── settings.gradle.kts ─────────────────────────────────────────────────────
settings_gradle = """
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SiamAi"
include(":app")
""".strip()

# ─── app/build.gradle.kts ────────────────────────────────────────────────────
app_gradle = """
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.siam.ai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.siam.ai"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "2.5.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
""".strip()

# ─── gradle-wrapper.properties ───────────────────────────────────────────────
wrapper_props = """
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\\://services.gradle.org/distributions/gradle-8.4-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
""".strip()

# ─── GitHub Actions Workflow ──────────────────────────────────────────────────
workflow = """
name: Build Siam AI APK

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Setup Java 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3

      - name: Make gradlew executable
        run: chmod +x gradlew

      - name: Build Release APK
        run: ./gradlew assembleRelease

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: siam-ai-apk
          path: app/build/outputs/apk/release/app-release-unsigned.apk
""".strip()

# ─── gradlew (stub that downloads actual wrapper) ────────────────────────────
gradlew = """#!/bin/sh
GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
APP_HOME=$(dirname "$0")
exec "$GRADLE_USER_HOME/wrapper/dists/gradle-8.4-bin/*/gradle-8.4/bin/gradle" "$@"
""".strip()

# ─── Write all files ─────────────────────────────────────────────────────────
files = {
    "build.gradle.kts": root_gradle,
    "settings.gradle.kts": settings_gradle,
    "app/build.gradle.kts": app_gradle,
    "gradle/wrapper/gradle-wrapper.properties": wrapper_props,
    ".github/workflows/build.yml": workflow,
}

for path, content in files.items():
    os.makedirs(os.path.dirname(path) if os.path.dirname(path) else ".", exist_ok=True)
    with open(path, "w") as f:
        f.write(content + "\n")
    print(f"✅ Created: {path}")

# gradlew via curl (actual binary needed)
print("\n⚠️  gradlew binary লাগবে। নিচের command রান করো:")
print("curl -o gradlew https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradlew && chmod +x gradlew")
print("mkdir -p gradle/wrapper && curl -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar")

print("\n✅ সব শেষ! এখন push করো:")
print("git add -A && git commit -m 'build: add gradle setup and workflow' && git push")
