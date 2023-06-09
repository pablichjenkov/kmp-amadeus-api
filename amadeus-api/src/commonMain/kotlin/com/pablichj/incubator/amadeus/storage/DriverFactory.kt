package com.pablichj.incubator.amadeus.storage

import app.cash.sqldelight.db.SqlDriver
import com.pablichj.incubator.amadeus.Database

expect class DriverFactory {
  suspend fun createDriver(): SqlDriver
}

suspend fun createDatabase(driverFactory: DriverFactory): Database {
  val driver = driverFactory.createDriver()
  return Database(driver)
}
