package com.swanky.teachit.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Topic::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Evaluation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int,
    val missingInfo: String,
    val expressionQuality: String,
    val suggestions: String,
    val score: Int,
    val createdAt: Long = System.currentTimeMillis()
)