plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.github.gmazzo.buildconfig")
}

version = "1.0.0"

kotlin {
    applyDefaultHierarchyTemplate()

    // IOS
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries {
            framework {
                baseName = "AmadeusDemoKt"
                isStatic = true
            }
        }
    }

    // JS
    js(IR) {
        browser()
    }

    // JVM
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(project(":amadeus-api"))
                implementation("io.github.pablichjenkov:component-toolkit:0.5.10-rc02")
                implementation("org.jetbrains.compose.components:components-resources:1.5.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

buildConfig {
    useKotlinOutput { internalVisibility = true }
    packageName("com.pablichj.incubator.amadeus.demo")

    val amadeusApiKey = extra["amadeus.apiKey"] as String
    require(amadeusApiKey.isNotEmpty()) {
        "Register your api key from amadeus and place it in local.properties as `amadeus.apiKey`"
    }

    val amadeusApiSecret = extra["amadeus.apiSecret"] as String
    require(amadeusApiKey.isNotEmpty()) {
        "Register your api secret from amadeus and place it in local.properties as `amadeus.apiSecret`"
    }

    buildConfigField(
        "String",
        "AMADEUS_API_KEY", amadeusApiKey
    )

    buildConfigField(
        "String",
        "AMADEUS_API_SECRET", amadeusApiSecret
    )

}
