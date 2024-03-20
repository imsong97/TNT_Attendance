package com.tnt.attendance_data.entity

import com.google.gson.annotations.SerializedName

data class ErrorEntity(
    @SerializedName("user")
    val userId: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("error_msg")
    val errorMsg: String
)
