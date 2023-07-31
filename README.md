## Amadeus Kotlin Multiplatform Client
A kotlin multiplatform library to access Amadeus System APIs.

<H3>Want to try it?</H3>

```
// your-project-root-path/shared/build.gradle

val commonMain by getting {
    dependencies {
        implementation("io.github.pablichjenkov:amadeus-api:0.1.10")
    }
}
```

<H5>Xcode Setup</H5>

In the project's `Build Settings` make sure the following properties contain bellow values. It ensures passing the linker flags to the objective-c compiler so it links AmadeusDemoKt and sqlite3 frameworks to the build.

*Framework Search Path*
```
FRAMEWORK_SEARCH_PATHS = $(inherited) "${PODS_ROOT}/../../shared/build/cocoapods/framework"
```

*Other Linker Flags*
```
OTHER_LDFLAGS = $(inherited) -ObjC -l"c++" -framework "AmadeusDemoKt" -lsqlite3
```

<H5>Examples</H5>

This is a project using it:<BR>
https://github.com/pablichjenkov/amadeus-hotel-app

Check the shared module within this repo and run the different platform Apps:<BR>
https://github.com/pablichjenkov/kmp-amadeus-api/tree/main/shared
