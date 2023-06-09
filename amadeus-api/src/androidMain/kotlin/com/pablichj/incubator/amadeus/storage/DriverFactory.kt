package com.pablichj.incubator.amadeus.storage

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pablichj.incubator.amadeus.Database

actual class DriverFactory(private val context: Context) {
  actual suspend fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(Database.Schema, context, "amadeus_demo.db")
  }
}