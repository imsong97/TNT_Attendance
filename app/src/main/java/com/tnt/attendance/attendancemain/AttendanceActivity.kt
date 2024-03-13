package com.tnt.attendance.attendancemain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.tnt.attendance.R
import com.tnt.commonlibrary.calendar.EnableDayDecorator
import com.tnt.commonlibrary.calendar.InitDecorator
import com.tnt.commonlibrary.calendar.SelectDecorator
import com.tnt.commonlibrary.calendar.TodayDecorator
import com.tnt.attendance.attendancemain.recylcerview.MemberListAdapter
import com.tnt.attendance.databinding.ActivityAttendanceBinding
import com.tnt.attendance.manage.AttendManageActivity
import com.tnt.attendance_data.AttendanceDataRepository
import com.tnt.commonlibrary.wrapper.ContextWrapper
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : AppCompatActivity(), AttendanceContract.View {

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var presenter: AttendanceContract.Presenter

    private var exist = false
    private var memberListAdapter: MemberListAdapter? = null
    private var popupMenu: PopupMenu? = null
    private var selectDecorator: SelectDecorator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AttendanceActivity, R.layout.activity_attendance)
        presenter = AttendancePresenter(this@AttendanceActivity, ContextWrapper(this@AttendanceActivity))
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        showLottie()

        val calendar = Calendar.getInstance(Locale.KOREA)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        setSpinnerText(year, month)

        setCalendar(year, month)

        presenter.getSpinnerData(year.toString(), month.toString())

        binding.layoutSpinner.setOnClickListener(listener)
        binding.btnAttendanceMain.setOnClickListener(listener)
        binding.btnRankingList.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener { v ->
        when(v.id) {
            R.id.layout_spinner -> popupMenu?.show()
            R.id.btn_attendance_main -> {

            }
            R.id.btn_ranking_list -> {
                Intent(this@AttendanceActivity, AttendManageActivity::class.java).also {
                    startActivity(it)
                }
//                throw RuntimeException("Test Crash") // Force a crash
            }
        }
    }

    private fun setCalendar(year: Int, month: Int) {
        if (popupMenu != null) { // 초기 init 시 호출 금지
            showLottie()
            presenter.getAttendDateList(year.toString(), month.toString())
        }
        binding.calendarView.apply {
            val calendar = Calendar.getInstance(Locale.KOREA)

            this.isPagingEnabled = false // disable swipe
            this.topbarVisible = false // 캘린더 선택 bar 삭제
            this.isLongClickable = false
            this.currentDate = CalendarDay.from(year, month, calendar.get(Calendar.DAY_OF_MONTH))

            this.removeDecorators()
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
                setMemberListAdapter(date.day)
            }
            this.invalidate()
        }

        binding.includeAttendMember.root.visibility = View.GONE
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
        hideLottie()
        dayList.forEach {
            binding.calendarView.addDecorator(EnableDayDecorator(this@AttendanceActivity, it.toIntOrNull()))
        }
    }

    private fun setMemberListAdapter(date: Int) {
        binding.includeAttendMember.root.visibility = View.VISIBLE
        val list = presenter.getAttendMemberList(date.toString())
        binding.includeAttendMember.txtDateTitle.text = "${date}일 - 총 ${list.size}명"

        if (memberListAdapter == null) {
            memberListAdapter = MemberListAdapter(list)
            binding.includeAttendMember.recyclerView.apply {
                this.setHasFixedSize(true)
                this.layoutManager = GridLayoutManager(this@AttendanceActivity, 2)
                this.adapter = memberListAdapter
            }
        } else {
            memberListAdapter?.setMemberList(list)
            memberListAdapter?.notifyDataSetChanged()
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

    private fun showLottie() {
        binding.lottieLayout.visibility = View.VISIBLE
        Glide.with(this@AttendanceActivity)
            .load(R.drawable.tennis_court)
            .skipMemoryCache(true) // glide clear 시에 메모리에 남기지 않게 하기 위해
            .into(binding.lottieImage)
    }

    private fun hideLottie() {
        binding.lottieLayout.visibility = View.GONE
        Glide.with(binding.lottieImage.context)
            .clear(binding.lottieImage)
    }

    private fun backPressEvent() {
        if (exist) {
            finish()
            return
        }
        exist = true
        Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ exist = false }, 1500)
    }

    override fun onDestroy() {
        presenter.dispose()
        super.onDestroy()
    }
}