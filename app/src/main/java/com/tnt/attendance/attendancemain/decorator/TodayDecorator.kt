package com.tnt.attendance.attendancemain.decorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.tnt.attendance.R

class TodayDecorator(private val context: Context) : DayViewDecorator {
    private val today = CalendarDay.today()
    private val drawable = ContextCompat.getDrawable(context, R.drawable.bg_calendar_today)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == today
    }
    override fun decorate(view: DayViewFacade?) {
        view?.setSelectionDrawable(drawable!!)
    }
}