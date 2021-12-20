package com.example.tasks.service.repository.local

import android.os.FileObserver.DELETE
import androidx.room.Dao
import androidx.room.Query
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.PriorityRepository

@Dao
interface  PriorityDAO {

    fun save(list: List<PriorityModel> )

    @Query("DELETE FROM priority")
    fun clear()

    @Query("SELECT description FROM priority WHERE id = :id")
    fun getDescription(id: Int)

    @Query("SELECT * FROM priority")
    fun list(): List<PriorityModel>


}