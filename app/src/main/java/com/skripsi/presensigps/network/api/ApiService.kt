package com.skripsi.presensigps.network.api

import com.skripsi.presensigps.network.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //user
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("image") image: String,
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("logout")
    fun postLogout(
        @Header("Authorization") token: String,
    ): Call<LogoutModel>

    @POST("user")
    fun postUser(
        @Header("Authorization") token: String,
    ): Call<UserModel>

    @FormUrlEncoded
    @POST("user-delete")
    fun postUserDelete(
        @Field("id") id: Int,
    ): Call<UserDeleteModel>

    @FormUrlEncoded
    @POST("user-edit")
    fun postUserEdit(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<UserEditModel>

    @POST("user-edit-image")
    fun postUserEditImage(
        @Field("id") id: Int,
        @Field("image") image: String,
    ): Call<UserEditModel>

    @GET("user-index")
    fun getUserIndex(
    ): Call<UserIndexModel>

    @FormUrlEncoded
    @POST("user-sales-info")
    fun postSalesInfo(
        @Header("Authorization") token: String,
        @Field("user_id") userId: Int,
    ): Call<SalesInfoModel>


    //presence
    @GET("presence-index")
    fun getPresenceIndex(
    ): Call<PresenceIndexModel>

    @FormUrlEncoded
    @POST("presence-user")
    fun postPresenceUser(
        @Field("user_id") userId: Int,
    ): Call<PresenceIndexModel>

    @FormUrlEncoded
    @POST("presence-add")
    fun postPresenceAdd(
        @Field("user_id") userId: Int,
        @Field("image") image: String,
    ): Call<PresenceAddModel>

    @FormUrlEncoded
    @POST("presence-delete")
    fun postPresenceDelete(
        @Field("id") id: Int,
    ): Call<PresenceDeleteModel>

    @FormUrlEncoded
    @POST("presence-verify")
    fun postPresenceverify(
        @Field("id") id: Int,
        @Field("status") status: Int,
    ): Call<PresenceDeleteModel>

    @FormUrlEncoded
    @POST("presence-back")
    fun postPresenceBack(
        @Field("id") id: Int,
    ): Call<PresenceBackModel>


    //report
    @GET("report-index")
    fun getReportIndex(
    ): Call<ReportIndexModel>

    @FormUrlEncoded
    @POST("report-user")
    fun postReportUser(
        @Field("user_id") userId: Int,
    ): Call<ReportIndexModel>

    @FormUrlEncoded
    @POST("report-add")
    fun postReportAdd(
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("note") note: String,
        @Field("image") image: String,
    ): Call<ReportAddModel>

    @FormUrlEncoded
    @POST("report-delete")
    fun postReportDelete(
        @Field("id") id: Int,
    ): Call<ReportDeleteModel>

    @FormUrlEncoded
    @POST("report-verify")
    fun postReportverify(
        @Field("id") id: Int,
        @Field("status") status: Int,
    ): Call<ReportVerifyModel>

}