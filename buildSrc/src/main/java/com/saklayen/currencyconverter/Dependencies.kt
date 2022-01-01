import org.gradle.api.JavaVersion

object Versions {
    const val versionName = "1"
    private const val versionCodeBase = 1
    const val versionCodeMobile = versionCodeBase + 3
    const val COMPILE_SDK = 30
    const val TARGET_SDK = COMPILE_SDK
    const val MIN_SDK = 21
    const val ANDROID_GRADLE_PLUGIN = "7.1.0-alpha02"
    const val BENCHMARK = "1.0.0"
    const val ktLint = "0.41.0"
    val JAVA_VERSION = JavaVersion.VERSION_1_8
}

object Libs {
    const val androidGradlePlugin =
        "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val benchmarkGradlePlugin =
        "androidx.benchmark:benchmark-gradle-plugin:${Versions.BENCHMARK}"
    const val ktLint = "com.pinterest:ktlint:${Versions.ktLint}"

    object Kotlin {
        private const val version = "1.5.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"

        object Coroutines {
            private const val version = "1.5.1"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        object Ui {
            private const val coreVersion = "1.6.0"
            private const val appcompatVersion = "1.3.1"
            private const val materialVersion = "1.4.0"
            private const val constraintLayoutVersion = "2.0.4"
            private const val swipeRefreshLayoutVersion = "1.1.0"
            private const val fragmentVersionVersion = "1.3.5"
            private const val supportVersion = "1.0.0"
            private const val pagingVersion = "3.0.1"

            const val CORE_KTX = "androidx.core:core-ktx:$coreVersion"
            const val APPCOMPAT = "androidx.appcompat:appcompat:$appcompatVersion"
            const val APPCOMPAT_KTX = "androidx.activity:activity-ktx:1.2.3"
            const val FRAGMENT = "androidx.fragment:fragment:$fragmentVersionVersion"
            const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:$fragmentVersionVersion"
            const val MATERIAL = "com.google.android.material:material:$materialVersion"
            const val CONSTRAINT_LAYOUT =
                "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
            const val SWIPE_REFRESH_LAYOUT =
                "androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion"
            const val LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:$supportVersion"
            const val PAGING_RUNTIME = "androidx.paging:paging-runtime-ktx:$pagingVersion"
        }

        object Lifecycle {
            private const val lifecycleVersion = "2.4.0-alpha02"
            private const val archVersion = "2.1.0"
            const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
            const val SAVE_STATE =
                "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion"

            //const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
            const val COMPILER = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
            const val SERVICE = "androidx.lifecycle:lifecycle-service:$lifecycleVersion"
            const val PROCESS = "androidx.lifecycle:lifecycle-process:$lifecycleVersion"
            const val REACTIVE_STREAMS =
                "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion"
            const val TEST = "androidx.arch.core:core-testing:$archVersion"
        }

        object Navigation {
            private const val version = "2.3.5"
            const val gradlePlugin =
                "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            const val FRAGMENT = "androidx.navigation:navigation-fragment:$version"
            const val UI = "androidx.navigation:navigation-ui:$version"
            const val FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:$version"
            const val UI_KTX = "androidx.navigation:navigation-ui-ktx:$version"
            const val TEST = "androidx.navigation:navigation-testing:$version"
        }

        object Test {
            private const val runnerAndRulesVersion = "1.3.0"
            private const val junitVersion = "1.1.2"
            private const val espressoVersion = "3.2.0"

            const val runner = "androidx.test:runner:$runnerAndRulesVersion"
            const val rules = "androidx.test:rules:$runnerAndRulesVersion"
            const val junit = "androidx.test.ext:junit-ktx:$junitVersion"
            const val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
        }
    }


    object Hilt {
        private const val version = "2.37"

        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
        const val testing = "com.google.dagger:hilt-android-testing:$version"
    }

    object Font {
        private const val calligraphyVersion = "3.1.1"
        private const val viewpumpVersion = "2.0.3"

        const val CALLIGRAPHY = "io.github.inflationx:calligraphy3:$calligraphyVersion"
        const val VIEWPUMP = "io.github.inflationx:viewpump:$viewpumpVersion"
    }

    object Database {
        private const val roomVersion = "2.3.0"
        private const val sqlcipherVersion = "4.4.3"
        private const val datastoreVersion = "1.0.0-rc01"

        const val ROOM_COMPILER = "androidx.room:room-compiler:$roomVersion"
        const val ROOM_KTX = "androidx.room:room-ktx:$roomVersion"
        const val ROOM_RUNTIME = "androidx.room:room-runtime:$roomVersion"
        const val SQLCIPHER = "net.zetetic:android-database-sqlcipher:$sqlcipherVersion"
        const val PREFERENCE = "androidx.datastore:datastore-preferences:$datastoreVersion"
    }

    object Network {
        private const val retrofitVersion = "2.9.0"
        private const val moshiVersion = "1.12.0"
        private const val stethoVersion = "1.6.0"
        private const val okhttpVersion = "4.9.1"

        const val RETROFIT = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val CONVERTER_MOSHI = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"

        const val MOSHI = "com.squareup.moshi:moshi-kotlin:$moshiVersion"
        const val MOSHI_COMPILER = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"

        const val STHETO = "com.facebook.stetho:stetho:$stethoVersion"
        const val STHETO_OKHTTP = "com.facebook.stetho:stetho-okhttp3:$stethoVersion"

        const val BOM = "com.squareup.okhttp3:okhttp-bom:$okhttpVersion"
        const val OKHTTP = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        const val INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
    }

    object Logging {
        private const val version = "4.7.1"
        const val TIMBER = "com.jakewharton.timber:timber:$version"
    }

    object JUnit {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
    }

}
