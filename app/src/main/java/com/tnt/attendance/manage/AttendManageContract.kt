package com.tnt.attendance.manage

interface AttendManageContract {

    interface View {

    }
    interface Presenter {
        fun getMemberList()
        fun dispose()
    }
}