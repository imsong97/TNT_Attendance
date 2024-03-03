package com.tnt.attendance.attendancemain.decorator

import android.content.Context
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.tnt.attendance.R

class EnableDayDecorator(private val context: Context, private val date: Int?) :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.day == (date ?: false)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(context.getColor(R.color.color_212121)))
        view?.setDaysDisabled(false)
    }
}