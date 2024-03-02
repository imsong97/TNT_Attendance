package com.tnt.attendance.main

import com.tnt.commonlibrary.ContextWrapper

class AttendancePresenter(
    private val view: AttendanceContract.View,
    private val contextWrapper: ContextWrapper
) : AttendanceContract.Presenter {

}