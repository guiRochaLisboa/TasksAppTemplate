package com.example.tasks.service.repository.remote

import com.example.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {

    @GET("Task")
    fun all(): Call<List<TaskModel>>

    @GET("Task/Next7Days")
    fun nextWeek(): Call<List<TaskModel>>

    @GET("Task/Overdue")
    fun overdue(): Call<List<TaskModel>>

    @GET("Task/{id}")
    fun load(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    fun createTask(
        @Field("PriorityId") priority: Int,
        @Field("Description") descption: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean


    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task", hasBody = true)
    fun update(
        @Field("Id") Id: Int,
        @Field("PriorityId") priority: Int,
        @Field("Description") descption: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean


    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    fun complete(
        @Field("Id") Id: Int
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    fun undo(
        @Field("Id") Id: Int
    ): Call<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    fun delete(
        @Field("Id") Id: Int
    ): Call<Boolean>
}