package com.app.todolist.recyclerView

interface DecorationTypeProvider {
    fun getType(position: Int) : DecorationType
}