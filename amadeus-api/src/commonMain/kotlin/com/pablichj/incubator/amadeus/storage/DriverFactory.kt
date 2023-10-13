package com.pablichj.incubator.amadeus.storage

import app.cash.sqldelight.db.SqlDriver
import com.pablichj.incubator.amadeus.Database

interface DriverFactory {
    suspend fun createDriver(
        //schema: SqlSchema<QueryResult.AsyncValue<Unit>>
    ): SqlDriver
}

suspend fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(driver)
}
