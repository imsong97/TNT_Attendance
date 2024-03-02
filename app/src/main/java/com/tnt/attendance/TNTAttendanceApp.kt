package com.tnt.attendance

import android.app.Application
import com.google.firebase.FirebaseApp

class TNTAttendanceApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this@TNTAttendanceApp)
    }
}