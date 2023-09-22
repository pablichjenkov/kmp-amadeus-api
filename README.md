## KMP client for Amadeus Self Service API
A kotlin multiplatform library to access Amadeus System APIs.

<H5>amadeus-api is on Maven Central</H5>

```
// your-project-root-path/shared/build.gradle

val commonMain by getting {
    dependencies {
        implementation("io.github.pablichjenkov:amadeus-api:0.3.0")
    }
}
```

<H5>Amadeus API Key</H5>

In order to be able to access Amadeus public API once need to create an Account here:

https://developers.amadeus.com/self-service

and get an Api key. Pass the token as parameter to the different use cases that need it.

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
