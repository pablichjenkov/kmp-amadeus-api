plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight")
    id("org.jetbrains.dokka")
    id("maven-publish")
    signing
}

group = "io.github.pablichjenkov"
version = "0.1.10"
val mavenCentralUser = extra["mavenCentral.user"] as String
val mavenCentralPass = extra["mavenCentral.pass"] as String

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
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
                val projectGitUrl =
                    "https://github.com/pablichjenkov/templato/tree/master/component"
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

signing {
    sign(publishing.publications)
}

kotlin {
    // ANDROID
    android()
    android {
        publishLibraryVariants("release")
    }

    // IOS
    iosArm64()
    iosSimulatorArm64()
    /*ios {
        binaries {
            framework {
                baseName = 'shared'
            }
        }
    }*/

    // JS
    js(IR) {
        browser()
    }

    // JVM
    jvm("desktop")

    /*sourceSets.forEach {
        it.dependencies {
            implementation(project.dependencies.enforcedPlatform("io.ktor:ktor-bom:2.2.4"))
        }
    }
*/
    sourceSets {
        val ktorVersion = "2.2.4"
        // COMMON
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation ("ch.qos.logback:logback-classic:1.3.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
            // kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "coroutines" }
        }

        // ANDROID
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("app.cash.sqldelight:android-driver:2.0.0-alpha05")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val androidInstrumentedTest by getting

        // IOS
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("app.cash.sqldelight:native-driver:2.0.0-alpha05")
            }
        }
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        // JS
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("app.cash.sqldelight:sqljs-driver:2.0.0-alpha05")
                implementation(npm("sql.js", "1.6.2"))
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
                // implementation(npm("@js-joda/timezone", "2.3.0"))
            }
        }

        // JVM
        val desktopMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-java:$ktorVersion")
                implementation("app.cash.sqldelight:sqlite-driver:2.0.0-alpha05")
            }
        }
    }

}

android {
    namespace = "com.pablichj.incubator.amadeus"
    compileSdk = 33
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.pablichj.incubator.amadeus")
        }
    }
}