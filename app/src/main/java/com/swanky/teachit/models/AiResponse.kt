package com.swanky.teachit.models

data class AiResponse(
    val missingInfo: String,
    val explanationQuality: String,
    val suggestions: List<String>,
    val score: Int
)