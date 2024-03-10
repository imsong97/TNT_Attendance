package com.tnt.attendance.manage

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.tnt.attendance.R
import com.tnt.attendance.databinding.ActivityManageBinding
import com.tnt.attendance.manage.dialog.CalendarDialog
import com.tnt.commonlibrary.RxBus
import com.tnt.commonlibrary.RxEvent
import com.tnt.commonlibrary.calendar.InitDecorator
import com.tnt.commonlibrary.calendar.SelectDecorator
import com.tnt.commonlibrary.calendar.TodayDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AttendManageActivity : AppCompatActivity(), AttendManageContract.View {

    private lateinit var binding: ActivityManageBinding
    private val compositeDisposable = CompositeDisposable()
    private lateinit var selectedDate: CalendarDay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AttendManageActivity, R.layout.activity_manage)

        title = getString(com.tnt.commonlibrary.R.string.title_attend_manage)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        CalendarDialog(this@AttendManageActivity, null).show()

        RxBus.listen(RxEvent.SelectedDate::class.java)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                selectedDate = it.date
                binding.txtSelectedDate.text = "${selectedDate.year}년 ${selectedDate.month}월 ${selectedDate.day}일" // TODO 요일?
            }.also {
                compositeDisposable.add(it)
            }
    }
}