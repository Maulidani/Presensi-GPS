package com.skripsi.presensigps.network.model

class PresenceIndexModel(
    val success: Boolean,
    val message: String,
    val data: ArrayList<DataPresenceModel>,
)

class PresenceAddModel(
    val success: Boolean,
    val message: String,
    val data: DataPresenceModel,
)

class PresenceDeleteModel(
    val success: Boolean,
    val message: String,
)

class PresenceVerifyModel(
    val success: Boolean,
    val message: String,
)

class PresenceBackModel(
    val success: Boolean,
    val message: String,
)

data class DataPresenceModel(
    val id: Int,
    val office_id: Int,
    val user_id: Int,
    val image: String,
    val back: Int,
    val status: Int,
    val created_at: String,
)