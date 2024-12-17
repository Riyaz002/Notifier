package com.wiseowl.notifier.ui.common.model

data class InputField<T>(
    val value: T? = null,
    val label: String,
    val error: String? = null,
    val required: Boolean = true
){
    fun updateValue(value: T): InputField<T>{
        return InputField(value, label, null, required)
    }
}