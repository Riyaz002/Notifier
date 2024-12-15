package com.wiseowl.notifier.ui.common.model

data class InputField<T>(
    var value: T? = null,
    val label: String,
    var error: String? = null,
    val required: Boolean = true
){
    fun updateValue(value: T): InputField<T>{
        this.value = value
        error = null
        return this
    }
}