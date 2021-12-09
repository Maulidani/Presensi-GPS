package com.skripsi.presensigps.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //user
    @GET("user")
    fun apiUser(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    fun apiLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @Multipart
    @POST("register")
    fun apiAdduser(
        @Part("name") name: RequestBody,
        @Part("position") position: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("edit-user")
    fun apiEditUser(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @Multipart
    @POST("edit-image-user")
    fun apiEditImgUser(
        @Part("id") id: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<ResponseModel>
}