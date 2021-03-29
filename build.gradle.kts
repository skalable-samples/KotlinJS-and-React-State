plugins {
    kotlin("js") version "1.4.30"
}

val kotlin = "1.4.30"
val kotlinBase = "-pre.148-kotlin-"
val kotlinReact = "17.0.1"
val kotlinStyled = "5.2.1"
val kotlinJSReact = kotlinReact + kotlinBase + kotlin
val kotlinJSStyled = kotlinStyled + kotlinBase + kotlin

group = "dev.skalable"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
}

dependencies {
    implementation("org.jetbrains:kotlin-react:${kotlinJSReact}")
    implementation("org.jetbrains:kotlin-react-dom:${kotlinJSReact}")
    implementation("org.jetbrains:kotlin-styled:${kotlinJSStyled}")
}

kotlin {
    js(IR) {
        browser {
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
        binaries.executable()
    }
}