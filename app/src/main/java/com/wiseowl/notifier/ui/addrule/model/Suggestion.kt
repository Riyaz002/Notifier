package com.wiseowl.notifier.ui.addrule.model

import android.text.SpannableString

data class Suggestion(
    val placeId: String,
    val fullText: SpannableString?,
    val primaryText: SpannableString?,
    val secondaryText: SpannableString?
)