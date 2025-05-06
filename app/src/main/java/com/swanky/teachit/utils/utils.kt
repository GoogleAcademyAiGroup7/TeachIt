package com.swanky.teachit.utils

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
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

// Hide and show item with animated
fun View.showWithAnimated(rootView: ViewGroup) {
    TransitionManager.beginDelayedTransition(rootView, AutoTransition())
    this.show()
}

fun View.hideWithAnimated(rootView: ViewGroup) {
    TransitionManager.beginDelayedTransition(rootView, AutoTransition())
    this.gone()
}