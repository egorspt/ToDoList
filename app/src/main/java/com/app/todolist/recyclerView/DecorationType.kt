package com.app.todolist.recyclerView

sealed class DecorationType {
    object Empty : DecorationType()
    object Space : DecorationType()
    class Text(val text: String) : DecorationType()
}