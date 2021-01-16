package com.app.todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.app.todolist.database.AppDatabase
import com.app.todolist.database.TaskDao
import com.app.todolist.recyclerView.ItemTouchHelperCallback
import com.app.todolist.recyclerView.SwipeListener
import com.app.todolist.recyclerView.TaskAdapter
import com.app.todolist.recyclerView.TaskDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val POST_DATABASE_NAME = "postDatabaseName"
        private const val REQUEST_CODE = 42
    }

    private lateinit var taskDao: TaskDao
    private val taskAdapter = TaskAdapter { it ->
        taskDao.delete(it)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { showError(it.toString()) },
                onComplete = {
                    taskDao.getAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy { checkCountTask(it.size) }
                }
            )
    }
    private val taskDecorator = TaskDecorator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), REQUEST_CODE)
        }

        taskDao = Room.databaseBuilder(this, AppDatabase::class.java, POST_DATABASE_NAME)
            .build().taskDao()

        val callback =
            ItemTouchHelperCallback(taskAdapter as SwipeListener)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            recyclerView.adapter = taskAdapter
            addItemDecoration(taskDecorator)
        }

        getTasks()
    }

    private fun getTasks() {
        taskDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { showError(it.toString()) },
                onSuccess = { it ->
                    val list = it.toMutableList()
                    list.sortBy { it.dateCompletion }
                    taskAdapter.update(list)
                    checkCountTask(it.size)
                }
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            getTasks()
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun checkCountTask(count: Int) {
        if (count == 0)
            empty.visibility = View.VISIBLE
        else empty.visibility = View.GONE
    }
}