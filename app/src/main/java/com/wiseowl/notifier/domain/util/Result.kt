package com.wiseowl.notifier.domain.util


sealed interface Result{
    data class Success<T>(val data: T): Result
    data class Failure(val error: Exception?): Result
}