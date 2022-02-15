package ru.gb.weatherkotlin.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistoryEntity::class), version = 1, exportSchema = false)

abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}