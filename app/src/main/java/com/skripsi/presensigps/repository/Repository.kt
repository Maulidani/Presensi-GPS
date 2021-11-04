package com.skripsi.presensigps.repository

import com.skripsi.presensigps.network.api.ApiService


class Repository(private val apiService: ApiService) {

    //user
    fun salesInfo(
        token: String,
        userId: Int,
    ) =
        apiService.postSalesInfo(token, userId)

    fun login(
        email: String,
        password: String
    ) =
        apiService.postLogin(email, password)

    fun register(
        name: String,
        position: String,
        email: String,
        password: String,
        image: String,
    ) =
        apiService.postRegister(name, position, email, password, image)

    fun logout(
        token: String,
    ) =
        apiService.postLogout(token)

    fun user(
        token: String,
    ) =
        apiService.postUser(token)

    fun userDelete(
        id: Int,
    ) =
        apiService.postUserDelete(id)

    fun userEdit(
        id: Int,
        name: String,
        position: String,
        email: String,
        password: String,
    ) =
        apiService.postUserEdit(id, name, position, email, password)

    fun userEditImage(
        id: Int,
        image: String,
    ) =
        apiService.postUserEditImage(id, image)

    fun userIndex(
    ) =
        apiService.getUserIndex()


    //presence
    fun presenceIndex(
    ) =
        apiService.getPresenceIndex()

    fun presenceUser(
        userId: Int
    ) =
        apiService.postPresenceUser(userId)

    fun presenceAdd(
        userId: Int,
        image: String,
    ) =
        apiService.postPresenceAdd(userId, image)

    fun presenceDelete(
        id: Int,
    ) =
        apiService.postPresenceDelete(id)

    fun presenceVerify(
        id: Int,
        status: Int,
    ) =
        apiService.postPresenceverify(id, status)

    fun presenceBack(
        id: Int,
    ) =
        apiService.postPresenceBack(id)


    //report
    fun reportIndex(
    ) =
        apiService.getReportIndex()

    fun reportUser(
        userId: Int
    ) =
        apiService.postReportUser(userId)

    fun reportAdd(
        userId: Int,
        name: String,
        latitude: String,
        longitude: String,
        note: String,
        image: String,
    ) =
        apiService.postReportAdd(userId, name, latitude, longitude, note, image)

    fun reportDelete(
        id: Int,
    ) =
        apiService.postReportDelete(id)

    fun reportVerify(
        id: Int,
        status: Int,
    ) =
        apiService.postReportverify(id, status)

}