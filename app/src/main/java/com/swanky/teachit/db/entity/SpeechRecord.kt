package com.swanky.teachit.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speech_records") // Veritabanı tablo adı
data class SpeechRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val text: String,

    val timestamp: Long = System.currentTimeMillis()
)