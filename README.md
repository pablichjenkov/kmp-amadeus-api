## KMP client for Amadeus Self Service API
A kotlin multiplatform library to access Amadeus System APIs.

<H3>amadeus-api is on Maven Central</H3>

```
// your-project-root-path/shared/build.gradle

val commonMain by getting {
    dependencies {
        implementation("io.github.pablichjenkov:amadeus-api:0.3.5")
    }
}
```

<H3>Amadeus API Key</H3>

In order to be able to access Amadeus public API you need to create an Account in the following site:

https://developers.amadeus.com/self-service

In the portal you will get an Api key which you can use to access the free self service API.

<H3>Xcode Setup</H3>

In the project's `Build Settings` make sure the following properties contain bellow values. It ensures passing the linker flags to the objective-c compiler so it links AmadeusDemoKt and sqlite3 frameworks to the build.

**Framework Search Path**

Ensure the `Framework Search Paths` contains bellow directory path. Otherwise, add it.
```
$(SRCROOT)/../composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)
```

**Other Linker Flags**

Add the `-lsqlite3` flag in the Other Linker Flags section. Otherwise the objc compiler won't find the sqlite3 symbols. The setting will look like bellow:
```
$(inherited) -framework ComposeApp -lsqlite3
```

To link sqlite3 see this issue:
https://github.com/cashapp/sqldelight/issues/1442

<H3>Projects using it</H3>

This is a project using it:<BR>
https://github.com/pablichjenkov/amadeus-hotel-app
