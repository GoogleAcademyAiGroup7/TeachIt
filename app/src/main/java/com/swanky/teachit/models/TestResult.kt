package com.swanky.teachit.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: Int,
    val testDate: Long,
    val correctCount: Int,
    val incorrectCount: Int
)