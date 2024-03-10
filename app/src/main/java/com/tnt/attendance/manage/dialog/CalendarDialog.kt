package com.tnt.attendance.manage.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.tnt.attendance.R
import com.tnt.attendance.databinding.ViewDialogCalendarBinding
import com.tnt.attendance_data.entity.ClubMember
import com.tnt.commonlibrary.RxBus
import com.tnt.commonlibrary.RxEvent
import com.tnt.commonlibrary.calendar.SelectDecorator

class CalendarDialog(
    private val context: Context,
    private val memberList: ArrayList<ClubMember>?
) {

    private lateinit var dialog: AlertDialog
    private val builder: AlertDialog.Builder by lazy { AlertDialog.Builder(context) }
    private val view: ViewDialogCalendarBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_dialog_calendar, null, false)
    }

    init {
        view.calendarView.apply {
            this.isPagingEnabled = false // disable swipe
            this.topbarVisible = false // 캘린더 선택 bar 삭제
            this.isLongClickable = false


            // 날짜 선택 리스너
            this.setOnDateChangedListener { widget, date, selected ->
                RxBus.publish(RxEvent.SelectedDate(date))
                dialog.dismiss()
            }
            this.invalidate()
        }
    }

    fun show() {
        dialog = builder.setView(view.root).create()
        dialog.show()
    }
}