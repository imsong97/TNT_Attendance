package com.tnt.attendance.manage

import com.tnt.attendance_data.entity.ClubMember

interface AttendManageContract {

    interface View {
        fun setAdapter(list: ArrayList<ClubMember>)
        fun successAttendMember()
        fun showErrorDialog(msg: String)
    }
    interface Presenter {
        fun getMemberList()
        fun setAttendMember(year: String, month: String, attendList: Map<String, ArrayList<String>>)
        fun dispose()
    }
}