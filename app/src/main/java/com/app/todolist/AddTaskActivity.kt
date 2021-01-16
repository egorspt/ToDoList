package com.app.todolist

import android.app.Activity
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.app.todolist.database.AppDatabase
import com.app.todolist.database.Task
import com.app.todolist.database.TaskDao
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_task.*
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    companion object {
        private const val POST_DATABASE_NAME = "postDatabaseName"
        private val selectedDate = Calendar.getInstance()
        private val c = Calendar.getInstance()
        private val year = c.get(Calendar.YEAR)
        private val month = c.get(Calendar.MONTH)
        private val day = c.get(Calendar.DAY_OF_MONTH)
    }

    private var date: Long = 0L

    private lateinit var taskDao: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initListeners()
        initDatabase()
    }

    private fun initDatabase() {
        taskDao = Room.databaseBuilder(this, AppDatabase::class.java, POST_DATABASE_NAME).build()
            .taskDao()
    }

    private fun initListeners() {
        iconClose.setOnClickListener { setResult(Activity.RESULT_CANCELED); finish() }
        iconDone.setOnClickListener { contentCheck() }

        dateToday.setOnClickListener {
            selectedDate.set(Calendar.YEAR, c.get(Calendar.YEAR))
            selectedDate.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR))
            date = selectedDate.timeInMillis
            dateToday.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeSelected))
            dateTomorrow.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeNotSelected))
            dateOther.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeNotSelected))
        }

        dateTomorrow.setOnClickListener {
            selectedDate.set(Calendar.YEAR, c.get(Calendar.YEAR))
            selectedDate.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1)
            date = selectedDate.timeInMillis
            dateToday.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeNotSelected))
            dateTomorrow.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeSelected))
            dateOther.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeNotSelected))
        }

        dateOther.setOnClickListener {
            DatePickerDialog(
                this@AddTaskActivity,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, monthOfYear)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = selectedDate.timeInMillis
                    dateOther.text = "$dayOfMonth.${monthOfYear + 1}.$year"
                    dateToday.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this,
                            R.color.strokeNotSelected
                        )
                    )
                    dateTomorrow.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this,
                            R.color.strokeNotSelected
                        )
                    )
                    dateOther.strokeColor =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.strokeSelected))
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun contentCheck() {
        if (editText.text.toString() == "") {
            Toast.makeText(this, "Напишите задачу", Toast.LENGTH_SHORT).show()
            return
        }
        if (date == 0L) {
            Toast.makeText(this, "Укажите дату", Toast.LENGTH_SHORT).show()
            return
        }

        taskDao.insert(
            Task(
                editText.text.toString(),
                date,
                Calendar.getInstance().time.time,
                false
            )
        )
            .subscribeOn(Schedulers.io())
            .subscribe {
                setResult(Activity.RESULT_OK)
                finish()
            }
    }
}