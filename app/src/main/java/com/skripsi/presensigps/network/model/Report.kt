package com.skripsi.presensigps.network.model

class ReportIndexModel(
    val success: Boolean,
    val message: String,
    val data: ArrayList<DataReportModel>,
)

class ReportAddModel(
    val success: Boolean,
    val message: String,
    val data: DataReportModel,
)

class ReportDeleteModel(
    val success: Boolean,
    val message: String,
)

class ReportVerifyModel(
    val success: Boolean,
    val message: String,
)

data class DataReportModel(
    val id: Int,
    val user_id: Int,
    val name: String,
    val latitude: String,
    val longitude: String,
    val note: String,
    val image: String,
    val status: Int,
    val created_at: String,
)