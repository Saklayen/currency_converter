plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.saklayen.currencyconverter"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.versionCodeMobile
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "DATABASE_ENCRYPTION_KEY",
                "\"AIzaSyAhJx57ikQH9rYc8IT8W3d2As5cGHMBvuo\""
            )
        }

        debug {
            isDebuggable = true
            buildConfigField(
                "String",
                "DATABASE_ENCRYPTION_KEY",
                "\"AIzaSyAhJx57ikQH9rYc8IT8W3d2As5cGHMBvuo\""
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = Versions.JAVA_VERSION
        targetCompatibility = Versions.JAVA_VERSION
    }
    kotlinOptions {
        jvmTarget = Versions.JAVA_VERSION.toString()
    }
    lint {
        // Eliminates UnusedResources false positives for resources used in DataBinding layouts
        isCheckGeneratedSources = true
        // Running lint over the debug variant is enough
        isCheckReleaseBuilds = false
    }
}

kapt {
    arguments {
        arg("dagger.hilt.shareTestComponents", "true")
    }
    javacOptions {
// These options are normally set automatically via the Hilt Gradle plugin, but we
// set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)


    implementation(Libs.AndroidX.Lifecycle.VIEWMODEL)
    implementation(Libs.AndroidX.Lifecycle.LIVEDATA)
    implementation(Libs.AndroidX.Lifecycle.RUNTIME)
    implementation(Libs.AndroidX.Lifecycle.SAVE_STATE)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    kapt(Libs.AndroidX.Lifecycle.COMPILER)
    implementation(Libs.AndroidX.Lifecycle.SERVICE)
    implementation(Libs.AndroidX.Lifecycle.PROCESS)
    implementation(Libs.AndroidX.Lifecycle.REACTIVE_STREAMS)
    testImplementation(Libs.AndroidX.Lifecycle.TEST)

    implementation(Libs.AndroidX.Ui.APPCOMPAT)
    implementation(Libs.AndroidX.Ui.MATERIAL)
    implementation(Libs.AndroidX.Ui.CONSTRAINT_LAYOUT)
    implementation(Libs.AndroidX.Ui.SWIPE_REFRESH_LAYOUT)
    implementation(Libs.AndroidX.Ui.LEGACY_SUPPORT)
    implementation(Libs.AndroidX.Ui.PAGING_RUNTIME)

    implementation(Libs.AndroidX.Navigation.FRAGMENT)
    implementation(Libs.AndroidX.Navigation.UI)
    implementation(Libs.AndroidX.Navigation.FRAGMENT_KTX)
    implementation(Libs.AndroidX.Navigation.UI_KTX)
    androidTestImplementation(Libs.AndroidX.Navigation.TEST)

    implementation(Libs.Hilt.hilt)
    kapt(Libs.Hilt.compiler)

// Font
    implementation(Libs.Font.CALLIGRAPHY)
    implementation(Libs.Font.VIEWPUMP)

// Database
    kapt(Libs.Database.ROOM_COMPILER)
    implementation(Libs.Database.ROOM_KTX)
    implementation(Libs.Database.ROOM_RUNTIME)
    implementation(Libs.Database.SQLCIPHER)
    implementation(Libs.Database.PREFERENCE)


// Network
    implementation(Libs.Network.RETROFIT)
    implementation(Libs.Network.CONVERTER_MOSHI)
    implementation(Libs.Network.MOSHI)
    kapt(Libs.Network.MOSHI_COMPILER)
    implementation(Libs.Network.STHETO)
    implementation(Libs.Network.STHETO_OKHTTP)
    implementation(Libs.Network.BOM)
    implementation(Libs.Network.OKHTTP)
    implementation(Libs.Network.INTERCEPTOR)

//Logging
    implementation(Libs.Logging.TIMBER)

}