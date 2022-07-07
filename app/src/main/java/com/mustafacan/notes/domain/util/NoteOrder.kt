package com.mustafacan.notes.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrder(orderType)
    class Date(orderType: OrderType) : NoteOrder(orderType)
}