package com.tnt.attendance.attendancemain.decorator

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.tnt.attendance.R

class SelectDecorator(private val context: Context, private val date: CalendarDay) :
    DayViewDecorator {
    private val drawable = ContextCompat.getDrawable(context, R.drawable.bg_calendar_day_selected_dot)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }
    override fun decorate(view: DayViewFacade?) {
        view?.let {
            it.addSpan(ForegroundColorSpan(context.getColor(R.color.color_ffffff)))
            it.setSelectionDrawable(drawable!!)
        }
    }
}