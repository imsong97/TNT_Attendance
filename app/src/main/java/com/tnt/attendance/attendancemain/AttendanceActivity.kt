package com.tnt.attendance.attendancemain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tnt.attendance.R
import com.tnt.attendance.databinding.ActivityMainBinding
import com.tnt.attendance.calendar.AttendanceCalendarFragment
import com.tnt.attendance.remote.Firestore
import java.util.*

class AttendanceActivity : AppCompatActivity(), AttendanceContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: AttendanceContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AttendanceActivity, R.layout.activity_main)
        initView()
    }

    private fun initView() {
        Firestore.instance?.getData()
        val calendar = Calendar.getInstance(Locale.KOREA)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        binding.txtSpinner.text = "${year}년 ${month}월"

        supportFragmentManager.beginTransaction().replace(binding.contentLayout.id, AttendanceCalendarFragment.getInstance(month, arrayListOf())).commitAllowingStateLoss()
    }
}