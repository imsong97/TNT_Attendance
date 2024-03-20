package com.tnt.attendance_data

import com.tnt.attendance_data.entity.ErrorEntity
import com.tnt.attendance_data.remote.RealtimeDB
import java.text.SimpleDateFormat
import java.util.*

class ErrorRepository {
    companion object {
        private var mInstance: ErrorRepository? = null
        val instance: ErrorRepository?
            get() = synchronized(ErrorRepository::class.java) {
                if (mInstance == null) {
                    mInstance = ErrorRepository()
                }
                return mInstance
            }
    }

    fun sendError(userPhone: String, errorMsg: String) {
        val entity = ErrorEntity(
            userId = userPhone,
            errorMsg = errorMsg,
            time = run {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                dateFormat.format(Date())
            }
        )

        RealtimeDB.instance?.sendError(entity)
    }
}