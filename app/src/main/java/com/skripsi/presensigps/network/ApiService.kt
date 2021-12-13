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

    @POST("logout")
    fun apiLogout(
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

    @FormUrlEncoded
    @POST("show-user")
    fun apiShowUser(
        @Field("position") position: String,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("delete-user")
    fun apiDeleteUser(
        @Field("id") id: Int,
    ): Call<ResponseModel>

    @POST("show-report")
    fun apiShowReport(
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("verify-report")
    fun apiVerifyReport(
        @Field("id") id: Int,
        @Field("status") status: Int,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("verify-presence")
    fun apiVerifyPresence(
        @Field("id") id: Int,
        @Field("status") status: Int,
    ): Call<ResponseModel>

    @POST("show-presence")
    fun apiShowPresence(
    ): Call<ResponseModel>

    @GET("get-presence-today")
    fun apiGetPresenceToday(): Call<ResponseModel>

    @GET("location-presence")
    fun apiGetLocation(): Call<ResponseModel>

    @Multipart
    @POST("add-presence")
    fun apiAddPresence(
        @Part image: MultipartBody.Part,
    ): Call<ResponseModel>

    @POST("back-presence")
    fun apiBackPresence(
    ): Call<ResponseModel>

    @Multipart
    @POST("add-report")
    fun apiAddReport(
        @Part("name") name: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("note") note: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("create-pdf-presence")
    fun apiCreatePdfPresence(
        @Field("date") date: String,
        @Field("month") month: String,
        @Field("year") year: String,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("create-pdf-report")
    fun apiCreatePdfReport(
        @Field("date") date: String,
        @Field("month") month: String,
        @Field("year") year: String,
    ): Call<ResponseModel>

    @Multipart
    @POST("off-presence")
    fun apiOffSakit(
        @Part image: MultipartBody.Part,
    ): Call<ResponseModel>
}