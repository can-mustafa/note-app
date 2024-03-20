package com.mustafacan.notes.presentation.view.extensions

import android.view.View
import com.mustafacan.notes.presentation.util.SafeClickListener

fun View.setOnSafeClickListener(onSafeClick: (View) -> Unit) {
    setOnClickListener(SafeClickListener {
        onSafeClick.invoke(it)
    })
}

fun View.setOnSafeClickListener(interval: Int, onSafeClick: (View) -> Unit) {
    setOnClickListener(SafeClickListener(interval) {
        onSafeClick.invoke(it)
    })
}