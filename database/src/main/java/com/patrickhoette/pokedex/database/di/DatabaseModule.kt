package com.patrickhoette.pokedex.database.di

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.patrickhoette.pokedex.database.Database
import org.koin.dsl.bind
import org.koin.dsl.module

val DatabaseModule
    get() = module {
        factory { get<Database>().pokemonQueries }
        factory { get<Database>().pokemonListQueries }

        single { Database(get()) }
        single {
            val schema = Database.Schema.synchronous()
            AndroidSqliteDriver(
                schema = schema,
                context = get(),
                name = "main_database",
                callback = object : AndroidSqliteDriver.Callback(schema) {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        db.execSQL("PRAGMA foreign_keys = ON;")
                    }
                },
            )
        } bind SqlDriver::class
    }
