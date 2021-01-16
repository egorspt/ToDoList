package com.app.todolist.recyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.todolist.DateFormatter
import com.app.todolist.database.Task

class TaskAdapter(private val removeListener: (id: Int) -> Unit) :
    RecyclerView.Adapter<TaskViewHolder>(),
    SwipeListener, DecorationTypeProvider {

    private var tasks: MutableList<Task> = emptyList<Task>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TaskViewHolder.create(parent)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    fun update(list: MutableList<Task>) {
        tasks = list
        notifyDataSetChanged()
    }

    override fun onSwipe(position: Int, direction: Int) {
        removeListener(tasks[position].id)
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getType(position: Int): DecorationType {
        if (position < 0)
            return DecorationType.Empty

        val current = tasks[position].dateCompletion
        val previous = if (position - 1 < 0) 0 else tasks[position - 1].dateCompletion

        return DateFormatter.compareDate(current, previous)
    }
}