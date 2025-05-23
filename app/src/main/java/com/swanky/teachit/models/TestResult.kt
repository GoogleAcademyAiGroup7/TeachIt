package com.swanky.teachit.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Topic::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("topicId")]
)
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: Int,
    var score: Int,
    var totalQuestions: Int,
    var correctAnswers: Int,
    var timestamp: Long = System.currentTimeMillis()
)