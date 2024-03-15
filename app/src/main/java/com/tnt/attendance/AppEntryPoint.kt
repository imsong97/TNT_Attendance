package com.tnt.attendance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tnt.attendance.attendancemain.AttendanceActivity

class AppEntryPoint : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, AttendanceActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}