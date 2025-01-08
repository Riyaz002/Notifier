package com.wiseowl.notifier.ui

import android.annotation.SuppressLint
import android.os.VibrationEffect
import com.wiseowl.notifier.ui.navigation.Screen

open class Event

data class SnackBar(val text: String): Event()
data class Navigate(val screen: Screen): Event()
data object PopBackStack: Event()
data class ProgressBar(val show: Boolean): Event()
data class Vibrate(val type: Effect): Event(){
    @SuppressLint("InlinedApi")
    enum class Effect(val type: Int){
        THUD(VibrationEffect.Composition.PRIMITIVE_THUD)
    }
}