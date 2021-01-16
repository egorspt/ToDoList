package com.app.todolist

import com.app.todolist.recyclerView.DecorationType
import java.util.*

object DateFormatter {
    private val currentDate: Calendar = Calendar.getInstance()
    private const val TEXT_PREVIOUS = "ПРЕДЫДУЩИЕ"
    private const val TEXT_TODAY = "СЕГОДНЯ"
    private const val TEXT_TOMORROW = "ЗАВТРА"
    private const val TEXT_FUTURE = "ПРЕДСТОЯЩИЕ"

    fun compareDate(current: Long, previous: Long): DecorationType {
        val presentDate: Calendar = Calendar.getInstance()
        val previousDate: Calendar = Calendar.getInstance()
        presentDate.time = Date(current)
        previousDate.time = Date(previous)

        val currentYear = currentDate.get(Calendar.YEAR)
        val currentDays = currentDate.get(Calendar.DAY_OF_YEAR)
        val presentYear = presentDate.get(Calendar.YEAR)
        val presentDays = presentDate.get(Calendar.DAY_OF_YEAR)
        val previousYear = previousDate.get(Calendar.YEAR)
        val previousDays = previousDate.get(Calendar.DAY_OF_YEAR)

        val presentDecoration: DecorationType
        val previousDecoration: DecorationType

        presentDecoration = when {
            presentYear < currentYear -> DecorationType.Text(TEXT_PREVIOUS)
            presentDays < currentDays -> DecorationType.Text(TEXT_PREVIOUS)
            presentDays == currentDays -> DecorationType.Text(TEXT_TODAY)
            presentDays == currentDays + 1 -> DecorationType.Text(TEXT_TOMORROW)
            presentDays > currentDays + 1 -> DecorationType.Text(TEXT_FUTURE)
            else -> DecorationType.Text(TEXT_TODAY)
        }

        if (previous == 0L)
            return presentDecoration

        previousDecoration = when {
            previousYear < currentYear -> DecorationType.Text(TEXT_PREVIOUS)
            previousDays < currentDays -> DecorationType.Text(TEXT_PREVIOUS)
            previousDays == currentDays -> DecorationType.Text(TEXT_TODAY)
            previousDays == currentDays + 1 -> DecorationType.Text(TEXT_TOMORROW)
            previousDays > currentDays + 1 -> DecorationType.Text(TEXT_FUTURE)
            else -> DecorationType.Text(TEXT_TODAY)
        }

        return when (presentDecoration.text) {
            previousDecoration.text -> DecorationType.Space
            else -> presentDecoration
        }
    }

}