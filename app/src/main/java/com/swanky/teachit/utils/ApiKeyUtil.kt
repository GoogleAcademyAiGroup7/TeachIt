package com.swanky.teachit.utils
import com.swanky.teachit.BuildConfig

object ApiKeyUtil {
    val apiKey: String
        get() = BuildConfig.GEMINI_API_KEY
} 