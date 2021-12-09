package com.skripsi.presensigps.network

data class ResponseModel(
    val message: String,
    val status: Boolean,
    val data: ArrayList<DataModel>,
    val user: UserModel, //user
    val token: String //login
)

data class DataModel(
    val id: Int,
)

data class UserModel(
    val id: Int,
    val name: String,
    val position: String,
    val email: String,
    val password: String,
    val image: String,
)
