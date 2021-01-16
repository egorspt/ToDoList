package com.app.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): Single<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task): Completable

    @Query("delete from tasks WHERE id = :id")
    fun delete(id: Int): Completable

    @Query("DELETE FROM tasks")
    fun deleteAll(): Completable

    @Query("select count(*) from tasks")
    fun count(): LiveData<Int>
}