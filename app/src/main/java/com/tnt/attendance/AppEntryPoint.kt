package com.tnt.attendance

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tnt.attendance.attendancemain.AttendanceActivity

class AppEntryPoint : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isOnline()) {
            // 파이어베이스는 offline을 지원하지만 앱 안정성을 위해
            AlertDialog.Builder(this@AppEntryPoint)
                .setMessage("인터넷 연결을 확인하세요.") // TODO string
                .setPositiveButton("확인") { _, _ ->
                    finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .show()
            return
        }

        Intent(this, AttendanceActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}