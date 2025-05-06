package com.swanky.teachit.utils

import android.view.View
import java.text.DateFormat
import java.util.Date
import java.util.Locale

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

// Date format -> for example: 9 October 2024
fun Long.dateFormat(): String {
    val date = Date(this)
    val formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
    return formatter.format(date)
}