package com.swanky.teachit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swanky.teachit.models.Topic
import com.swanky.teachit.models.Evaluation
import com.swanky.teachit.models.TestResult
import com.swanky.teachit.helpers.Converters

@Database(
    entities = [Topic::class, Evaluation::class, TestResult::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): Dao
}