package com.tnt.attendance.manage

import com.tnt.attendance_data.entity.ClubMember

interface AttendManageContract {

    interface View {
        fun setAdapter(list: ArrayList<ClubMember>)
    }
    interface Presenter {
        fun getMemberList()
        fun dispose()
    }
}