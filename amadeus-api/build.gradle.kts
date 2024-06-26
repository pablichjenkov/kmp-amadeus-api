plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("signing")
}

group = "io.github.pablichjenkov"
version = "0.3.5"
val mavenCentralUser = extra["mavenCentral.user"] as String
val mavenCentralPass = extra["mavenCentral.pass"] as String

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

signing {
    sign(publishing.publications)
}

publishing {
    repositories {
        maven {
            name = "Central"
            // setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            // setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            setUrl("https://s01.oss.sonatype.org/content/repositories/releases/")
            credentials {
                username = mavenCentralUser
                password = mavenCentralPass
            }
        }
    }
    publications {
        println("publication = $name")
        withType<MavenPublication> {
            groupId = group as String
            artifactId = "amadeus-api"//makeArtifactId(name)
            version
            artifact(javadocJar)
            pom {
                val projectGitUrl = "https://github.com/pablichjenkov/kmp-amadeus-api"
                name.set(rootProject.name)
                description.set(
                    "kotlin multiplatform client to consume Amadeus API Services."
                )
                url.set(projectGitUrl)
                inceptionYear.set("2023")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("pablichjenkov")
                    }
                }
                issueManagement {
                    system.set("GitHub")
                    url.set("$projectGitUrl/issues")
                }
                scm {
                    connection.set("scm:git:$projectGitUrl")
                    developerConnection.set("scm:git:$projectGitUrl")
                    url.set(projectGitUrl)
                }
            }
        }
        /*create<MavenPublication>("state3x") {
            groupId = "org.gradle.sample"
            artifactId = "state3-desktop"
            version = "1.1"

            from(components["java"])
        }*/
    }
}

// Workaround for gradle issue: https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

kotlin {
    // ANDROID
    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    // IOS
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "AmadeusApiKt"
        }
    }
    /*ios {
        binaries.framework {
            baseName = project.name

            compilations.all {
                kotlinOptions.freeCompilerArgs += arrayOf("-linker-options", "-lsqlite3")
            }
        }
    }
    */

    // Browser
    js(IR) {
        browser()
    }

    /*@OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "AmadeusApiKt"
        browser()
        binaries.library()
    }*/

    // JVM
    jvm()

    /*sourceSets.forEach {
        it.dependencies {
            implementation(project.dependencies.enforcedPlatform("io.ktor:ktor-bom:2.2.4"))
        }
    }
*/
    sourceSets {
        val ktorVersion = "2.3.11"
        // COMMON
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            implementation("io.ktor:ktor-client-logging:$ktorVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        // kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "coroutines" }

        // Mobile Only
        val commonMobileOnly by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation("io.realm.kotlin:library-base:1.11.1")
            }
        }

        // ANDROID
        androidMain {
            dependsOn(commonMobileOnly)
            dependencies {
                implementation("ch.qos.logback:logback-classic:1.3.5")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("app.cash.sqldelight:android-driver:2.0.2")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val androidInstrumentedTest by getting

        // IOS
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            // dependsOn(commonMain.get())
            dependsOn(commonMobileOnly)
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("app.cash.sqldelight:native-driver:2.0.2")
            }
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest.get())
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        // JS
        jsMain.dependencies {
            implementation("io.ktor:ktor-client-js:$ktorVersion")
            implementation("app.cash.sqldelight:web-worker-driver:2.0.0")
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.0"))
            implementation(npm("sql.js", "1.8.0"))
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        }

        // JVM
        jvmMain.dependencies {
            implementation("ch.qos.logback:logback-classic:1.4.11")
            implementation("io.ktor:ktor-client-java:$ktorVersion")
            implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
        }

    }

}

android {
    namespace = "com.pablichj.incubator.amadeus"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.pablichj.incubator.amadeus")
            generateAsync.set(true)
        }
    }
    linkSqlite = true  // <-- Important so it pass the linker flag to the obj-compiler
}
