package com.tnt.attendance.main

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.tnt.attendance.R
import com.tnt.attendance.databinding.FragmentAttendanceMainBinding
import com.tnt.commonlibrary.ContextWrapper
import java.util.*

class AttendanceMainFragment : Fragment(), AttendanceContract.View {

    private lateinit var binding: FragmentAttendanceMainBinding
    private lateinit var presenter: AttendanceContract.Presenter
    private var selectDecorator: SelectDecorator? = null

    companion object {
        fun getInstance(month: Int, attendDate: ArrayList<String>): AttendanceMainFragment {
            val bundle = Bundle().run {
                this.putInt("month", month)
                this.putSerializable("attendDate", attendDate)
                this
            }
            val fragment = AttendanceMainFragment().apply {
                this.arguments = bundle
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance_main, null, false)
        presenter = AttendancePresenter(this, ContextWrapper(requireContext()))
        initView()
        return binding.root
    }

    private fun initView() {
        binding.calendarView.apply {
            this.isPagingEnabled = false // disable swipe
            this.topbarVisible = false // 캘린더 선택 bar 삭제
            this.isLongClickable = false

            val calendar = Calendar.getInstance()
            val nowMonth = arguments?.getInt("month")
            this.currentDate = CalendarDay.from(calendar.get(Calendar.YEAR), nowMonth ?: (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH))

            setCalendarDecorator(this)

            this.setOnMonthChangedListener { _, _ ->
                this.clearSelection()
            }

            // 날짜 선택 리스너
            this.setOnDateChangedListener { widget, date, selected ->
                selectDecorator?.also {
                    this.removeDecorator(it)
                }
                selectDecorator = SelectDecorator(requireContext(), date)
                this.addDecorator(selectDecorator)
            }
            this.invalidate()
        }
    }

    private fun setCalendarDecorator(calendar: MaterialCalendarView) {
        calendar.addDecorator(TodayDecorator(requireContext())) // 오늘 날짜 deco
        calendar.addDecorator(InitDecorator(requireContext())) // 전체 disable

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("attendDate", ArrayList::class.java)
        } else {
            arguments?.getSerializable("attendDate") as ArrayList<*>
        }?.forEach {
            if (it !is String) return
            calendar.addDecorator(EnableDayDecorator(requireContext(), it.toInt())) // 출석체크 날짜만 enable
        }
    }

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

    class SelectDecorator(private val context: Context, private val date: CalendarDay) : DayViewDecorator {
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

    class InitDecorator(private val context: Context) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return true
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(context.getColor(R.color.color_e0e0e0)))
            view?.setDaysDisabled(true)
        }
    }

    class EnableDayDecorator(private val context: Context, private val date: Int) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.day == date
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(context.getColor(R.color.color_212121)))
            view?.setDaysDisabled(false)
        }
    }
}