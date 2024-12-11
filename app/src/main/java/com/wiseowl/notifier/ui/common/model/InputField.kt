package com.wiseowl.notifier.ui.common.model

data class InputField<T>(
    val value: T,
    val label: String,
    val error: String? = null
)