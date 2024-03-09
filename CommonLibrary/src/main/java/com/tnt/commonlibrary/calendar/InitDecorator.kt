package com.tnt.commonlibrary.calendar

import android.content.Context
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.tnt.commonlibrary.R

class InitDecorator(private val context: Context) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(context.getColor(R.color.color_e0e0e0)))
        view?.setDaysDisabled(true)
    }
}