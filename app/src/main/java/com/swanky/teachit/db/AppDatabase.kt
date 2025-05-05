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
    // SpeechRecord entity'sini ekle
    entities = [Topic::class, Evaluation::class, TestResult::class, SpeechRecord::class],
    // Versiyonu artır (Örn: 1 ise 2 yap)
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getDao(): Dao // Bu metod aynı kalır

    // --- Singleton Pattern ---
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "teachit_database" // Veritabanı adını kontrol et
                )
                    // !!! ÖNEMLİ: Migration Stratejisi !!!
                    // Geliştirme için en kolayı (Veri kaybı olur):
                    .fallbackToDestructiveMigration()
                    // Yayınlama için doğru olan (Migration tanımlanmalı):
                    // .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}