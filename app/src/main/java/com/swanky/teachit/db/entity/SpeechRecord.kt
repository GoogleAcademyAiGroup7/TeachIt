package com.swanky.teachit.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speech_records") // Veritabanı tablo adını belirtir
data class SpeechRecord(
    @PrimaryKey(autoGenerate = true) // Bu alanın birincil anahtar olduğunu ve otomatik artacağını belirtir
    val id: Long = 0,

    val text: String, // Kaydedilecek metin alanı

    val timestamp: Long = System.currentTimeMillis() // Kayıt zamanı, varsayılan olarak mevcut zaman
)