package com.app.todolist.recyclerView

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.todolist.R
import com.app.todolist.database.Task
import kotlinx.android.synthetic.main.view_holder_task.view.*

class TaskViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_task, parent, false)
            view.checkbox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) view.text.paintFlags = view.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else view.text.paintFlags = 0

            }
            return TaskViewHolder(view)
        }
    }

    fun bind(task: Task) {
        itemView.text.text = task.text
    }
}