package com.pablichj.incubator.amadeus.storage

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.sqljs.initSqlDriver
import com.pablichj.incubator.amadeus.Database
import kotlinx.coroutines.await

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return initSqlDriver(Database.Schema).await()
    }
}