package com.tnt.attendance

import android.app.Application
import com.google.firebase.FirebaseApp
import com.tnt.commonlibrary.remoteconfig.RemoteConfig

class TNTAttendanceApp : Application() {

    override fun onCreate() {
        super.onCreate()
        println("++ TNTAttendanceApp onCreate ++")
        FirebaseApp.initializeApp(this@TNTAttendanceApp)
        RemoteConfig.instance?.init()
    }
}