package com.skripsi.presensigps.network.model

data class SalesInfoModel(
    val success: Boolean,
    val message: String,
    val count: CountSalesInfoModel,
    val presence: DataPresenceModel?,
    val user: DataUserModel,
)

data class CountSalesInfoModel(
    val presence_this_month: Int,
    val report_this_month: Int,
)

data class LoginModel(
    val success: Boolean,
    val message: String,
    val link_image: LinkImageModel,
    val data: DataUserModel,
    val office: OfficeModel,
)
data class OfficeModel(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Double,
)

data class RegisterModel(
    val success: Boolean,
    val message: String,
    val data: DataUserModel,
)

data class LogoutModel(
    val message: String,
)

data class UserModel(
    val data: DataUserModel,
)

data class UserIndexModel(
    val success: Boolean,
    val message: String,
    val data: ArrayList<DataUserModel>,
)

data class UserDeleteModel(
    val success: Boolean,
    val message: String,
)

data class UserEditModel(
    val success: Boolean,
    val message: String,
)

data class LinkImageModel(
    val user: String,
    val presence: String,
    val report: String,
)

data class DataUserModel(
    val id: Int,
    val name: String,
    val position: String,
    val email: String,
    val password: String,
    val image: String,
    val token: String,
    val created_at: String,
)