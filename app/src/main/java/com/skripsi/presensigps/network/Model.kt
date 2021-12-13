package com.skripsi.presensigps.network

import java.sql.Timestamp

data class ResponseModel(
    val message: String,
    val status: Boolean,
    val data: ArrayList<DataModel>,
    val data_today: DataModel,
    val user: UserModel, //user
    val list_user: ArrayList<UserModel>, //user
    val token: String //login
)

data class DataModel(
    val id: Int,
    val name: String,
    val position: String,
    val email: String,
    val password: String,
    val image: String,
    val created_at: Timestamp,
    val latitude: Double,
    val longitude: Double,
    val radius: Double,
    val note: String,
    val status: Int,
    val off: Int,
    val back_at: Int,
    val count: Int,
)

data class UserModel(
    val id: Int,
    val name: String,
    val position: String,
    val email: String,
    val password: String,
    val image: String,
)
