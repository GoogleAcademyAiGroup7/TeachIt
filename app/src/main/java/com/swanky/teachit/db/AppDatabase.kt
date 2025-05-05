package com.swanky.teachit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swanky.teachit.db.entity.SpeechRecord
import com.swanky.teachit.models.Topic
import com.swanky.teachit.models.Evaluation
import com.swanky.teachit.models.TestResult
import com.swanky.teachit.helpers.Converters

@Database(
    entities = [Topic::class, Evaluation::class, TestResult::class, SpeechRecord::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "teachit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}