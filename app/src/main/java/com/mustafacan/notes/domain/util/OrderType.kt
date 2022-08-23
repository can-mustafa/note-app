package com.mustafacan.notes.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}