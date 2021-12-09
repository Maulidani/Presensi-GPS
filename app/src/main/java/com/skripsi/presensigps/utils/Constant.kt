package com.skripsi.presensigps.utils

class Constant {
    companion object {

        const val BASE_URL = "http://192.168.70.5:8000/"
        const val URL_IMG_USER = BASE_URL + "image/user/"
        const val URL_IMG_PRESENCE = BASE_URL + "image/presence/"
        const val URL_IMG_REPORT = BASE_URL + "image/report/"

        const val PREF_IS_LOGIN = "PREF_IS_LOGIN"
        const val PREF_USER_TOKEN = "PREF_USER_TOKEN"
        const val PREF_USER_ID = "PREF_USER_ID"
        const val PREF_USER_POSITION = "PREF_USER_POSITION"

    }
}