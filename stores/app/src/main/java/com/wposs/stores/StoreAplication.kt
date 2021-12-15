package com.wposs.stores

import android.app.Application
import android.provider.DocumentsContract
import androidx.room.Room

class StoreAplication : Application() {
    companion object{
        lateinit var database: StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
            StoreDatabase::class.java,
            "StoreDatabase")
            .build()
    }
}