import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.1"
    id("kotlinx-serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.squareup.sqldelight")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

// CocoaPods requires the podspec to have a version.
version = "1.0"

android {
    compileSdkVersion(AndroidSdk.compile)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

kotlin {
    val sdkName: String? = System.getenv("SDK_NAME")

    val isiOSDevice = sdkName.orEmpty().startsWith("iphoneos")
    if (isiOSDevice) {
        iosArm64("iOS")
    } else {
        iosX64("iOS")
    }

    val isWatchOSDevice = sdkName.orEmpty().startsWith("watchos")
    if (isWatchOSDevice) {
        watchosArm64("watch")
    } else {
        watchosX86("watch")
    }

    macosX64("macOS")
    android()
    jvm()

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
    }

    js {
        browser {
        }
    }

    sourceSets {

        sourceSets["commonMain"].dependencies {
            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}") {
                isForce = true
            }

            // Ktor
            implementation(Ktor.clientCore)
            implementation(Ktor.clientJson)
            implementation(Ktor.clientLogging)
            implementation(Ktor.clientSerialization)

            // Kotlinx Serialization
            implementation(Serialization.core)

            // SQL Delight
            implementation(SqlDelight.runtime)
            implementation(SqlDelight.coroutineExtensions)

            // koin
            api(Koin.core)
            api(Koin.test)

            // kermit
            api(Deps.kermit)
        }
        sourceSets["commonTest"].dependencies {
        }

        sourceSets["androidMain"].dependencies {
            implementation(Ktor.clientAndroid)
            implementation(SqlDelight.androidDriver)

            implementation("androidx.compose.compiler:compiler:${Versions.compose}")
            implementation("androidx.compose.runtime:runtime:${Versions.compose}")

            implementation("com.google.android.material:material:1.3.0")
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01")
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha01")
            implementation("androidx.activity:activity-compose:1.3.0-alpha05")

            implementation(Compose.ui)
            implementation(Compose.uiGraphics)
            implementation(Compose.uiTooling)
            implementation(Compose.foundationLayout)
            implementation(Compose.material)
        }
        sourceSets["androidTest"].dependencies {
            implementation(kotlin("test-junit"))
            implementation(Test.junit)
        }

        sourceSets["jvmMain"].dependencies {
            implementation(Ktor.clientApache)
            implementation(Ktor.slf4j)
            implementation(SqlDelight.jdbcDriver)
            implementation(SqlDelight.sqlliteDriver)
        }

        sourceSets["iOSMain"].dependencies {
            implementation(Ktor.clientIos)
            implementation(SqlDelight.nativeDriver)
        }
        sourceSets["iOSTest"].dependencies {
        }

        sourceSets["watchMain"].dependencies {
            implementation(Ktor.clientIos)
            implementation(SqlDelight.nativeDriver)
        }

        sourceSets["macOSMain"].dependencies {
            implementation(Ktor.clientCio)
            implementation(SqlDelight.nativeDriverMacos)
        }

        sourceSets["jsMain"].dependencies {
            implementation(Ktor.clientJs)
        }
    }
}

sqldelight {
    database("PeopleInSpaceDatabase") {
        packageName = "com.surrus.peopleinspace.db"
        sourceFolders = listOf("sqldelight")
    }
}

multiplatformSwiftPackage {
    packageName("PeopleInSpace")
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("13") }
    }
}

