package com.swanky.teachit.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Topic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val lastStudiedAt: Long? = null,
    val studyCount: Int = 0
)