package com.tnt.attendance.attendancemain

interface AttendanceContract {
    interface View {
        fun setSpinner(monthList: ArrayList<String>)
        fun setDayDecorator(dayList: ArrayList<String>)
        fun showErrorDialog(msg: String)
    }
    interface Presenter {
        fun getSpinnerData(year: String)
        fun getAttendDateList(year: String, month: String)
        fun dispose()
    }
}