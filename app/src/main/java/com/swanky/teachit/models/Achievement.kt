package com.swanky.teachit.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String,
    val iconName: String,
    var goal: Long,
    var enable: Boolean = false,
    var isShowing: Boolean = false
)