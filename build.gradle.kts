
buildscript {
    repositories {
        mavenLocal() {
            content {
                includeGroup("io.insert-koin")
                includeGroup("co.touchlab")
            }
        }
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}")
    }
}

allprojects {
    repositories {
        mavenLocal() {
            content {
                includeGroup("io.insert-koin")
                includeGroup("co.touchlab")
            }
        }
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
        maven(url = "https://jitpack.io")
    }
}


