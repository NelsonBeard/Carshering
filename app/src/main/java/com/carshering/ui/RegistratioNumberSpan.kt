package com.carshering.ui

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView

fun setRegistrationNumber(
    regNoTextView: TextView,
    regNoFromServer: String
) {
    val regNoWithWhitespaces = addWhitespaces(regNoFromServer)
    regNoTextView.text = regNoDesigner(regNoWithWhitespaces)
}

private fun addWhitespaces(regNoFromServer: String): StringBuffer {
    return StringBuffer(regNoFromServer).apply {
        insert(1, " ")
        insert(5, " ")
        insert(8, " ")
    }
}

private fun regNoDesigner(regNoWithWhitespaces: StringBuffer): SpannableString {
    return SpannableString(regNoWithWhitespaces).apply {
        setSpan(
            ForegroundColorSpan(Color.BLACK),
            2, 5,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        setSpan(
            RelativeSizeSpan(0.75f),
            0, 1,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        setSpan(
            RelativeSizeSpan(0.75f),
            6, 8,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
}