package com.swanky.teachit.helpers

import android.content.Context
import android.graphics.Color

class ColorCycler(context: Context) {
    private val colors = listOf(
        Color.parseColor("#FFCDD2"), // Pastel Kırmızı
        Color.parseColor("#C8E6C9"),  // Pastel Yeşil
        Color.parseColor("#B2DFDB"), // Pastel Nane Yeşili
        Color.parseColor("#E1BEE7"), // Pastel Mor
        Color.parseColor("#D1C4E9"), // Pastel Leylak
        Color.parseColor("#C5CAE9"), // Pastel Mavi
        Color.parseColor("#F8BBD0"), // Pastel Pembe
        Color.parseColor("#BBDEFB"), // Pastel Açık Mavi
        Color.parseColor("#B3E5FC"), // Pastel Turkuaz
        Color.parseColor("#B2EBF2") // Pastel Cam Göbeği
    )

    fun getColor(position: Int): Int {
        return colors[position % colors.size]
    }
}