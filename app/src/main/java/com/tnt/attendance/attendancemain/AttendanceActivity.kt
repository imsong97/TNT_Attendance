package com.tnt.attendance.attendancemain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.tnt.attendance.R
import com.tnt.attendance.attendancemain.decorator.EnableDayDecorator
import com.tnt.attendance.attendancemain.decorator.InitDecorator
import com.tnt.attendance.attendancemain.decorator.SelectDecorator
import com.tnt.attendance.attendancemain.decorator.TodayDecorator
import com.tnt.attendance.databinding.ActivityMainBinding
import com.tnt.attendance_data.remote.Firestore
import com.tnt.commonlibrary.ContextWrapper
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : AppCompatActivity(), AttendanceContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: AttendanceContract.Presenter
    private lateinit var popupMenu: PopupMenu
    private lateinit var currentFragment: Fragment

    private var selectDecorator: SelectDecorator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AttendanceActivity, R.layout.activity_main)
        presenter = AttendancePresenter(this@AttendanceActivity, ContextWrapper(this@AttendanceActivity))
        initView()
    }

    private fun initView() {
        Firestore.instance?.getData()
        val calendar = Calendar.getInstance(Locale.KOREA)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        setSpinnerText(year, month)

        setCalendar(year, month)

        presenter.getSpinnerData(year.toString())

        binding.layoutSpinner.setOnClickListener(listener)
        binding.btnAttendanceMain.setOnClickListener(listener)
        binding.btnRankingList.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener { v ->
        when(v.id) {
            R.id.layout_spinner -> if (::popupMenu.isInitialized) {
                popupMenu.show()
            }
            R.id.btn_attendance_main -> {

            }
            R.id.btn_ranking_list -> {

            }
        }
    }

    private fun setCalendar(year: Int, month: Int) {
        presenter.getAttendDateList(year.toString(), month.toString())
        binding.calendarView.apply {
            val calendar = Calendar.getInstance(Locale.KOREA)

            this.isPagingEnabled = false // disable swipe
            this.topbarVisible = false // 캘린더 선택 bar 삭제
            this.isLongClickable = false
            this.currentDate = CalendarDay.from(year, month, calendar.get(Calendar.DAY_OF_MONTH))

            this.addDecorator(TodayDecorator(this@AttendanceActivity)) // 오늘 날짜 deco
            this.addDecorator(InitDecorator(this@AttendanceActivity)) // 전체 disable

            this.setOnMonthChangedListener { _, _ ->
                this.clearSelection()
            }

            // 날짜 선택 리스너
            this.setOnDateChangedListener { widget, date, selected ->
                selectDecorator?.also {
                    this.removeDecorator(it)
                }
                selectDecorator = SelectDecorator(this@AttendanceActivity, date)
                this.addDecorator(selectDecorator)
            }
            this.invalidate()
        }
    }

    override fun setSpinner(monthList: ArrayList<String>) {
        if (monthList.size == 0) return

        val calendar = Calendar.getInstance(Locale.KOREA)
        val year = calendar.get(Calendar.YEAR)

        PopupMenu(this@AttendanceActivity, binding.layoutSpinner).apply {
            monthList.forEach { m ->
                this.menu.add(0, m.toInt(), 0, "${year}년 ${m}월")
            }

            this.setOnMenuItemClickListener {
                setSpinnerText(year, it.itemId)
                setCalendar(year, it.itemId)
                true
            }
        }.also {
            popupMenu = it
        }
    }

    override fun setDayDecorator(dayList: ArrayList<String>) {
        dayList.forEach {
            binding.calendarView.addDecorator(EnableDayDecorator(this@AttendanceActivity, it.toIntOrNull()))
        }
    }

    private fun setSpinnerText(year: Int, month: Int) {
        binding.txtSpinner.text = "${year}년 ${month}월"
    }

    override fun showErrorDialog(msg: String) {
        AlertDialog.Builder(this@AttendanceActivity)
            .setMessage(msg)
            .setPositiveButton("확인", null)
            .show()
    }

    override fun onDestroy() {
        presenter.dispose()
        super.onDestroy()
    }
}