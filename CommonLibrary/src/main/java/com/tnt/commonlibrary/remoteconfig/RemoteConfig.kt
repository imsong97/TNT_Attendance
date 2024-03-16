package com.tnt.commonlibrary.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.tnt.commonlibrary.R
import org.json.JSONObject

class RemoteConfig {

    private val utilConfig by lazy {
        val value = FirebaseRemoteConfig.getInstance().getString("utils")
        JSONObject(value)
    }

    companion object {
        private var mInstance: RemoteConfig? = null
        val instance: RemoteConfig?
            get() = synchronized(RemoteConfig::class.java) {
                if (mInstance == null) {
                    mInstance = RemoteConfig()
                }
                return mInstance
            }
    }

    fun init() {
        println("++ Remote Config init ++")
        FirebaseRemoteConfig.getInstance().apply {
            val remoteConfigSettings = FirebaseRemoteConfigSettings
                .Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build()
            this.setConfigSettingsAsync(remoteConfigSettings)
            this.setDefaultsAsync(R.xml.remote_config_default)
            this.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    this.activate()
                    println("++ Remote Config success++")
                } else {
                    it.exception?.printStackTrace()
                }

            }
        }
    }

    fun getMinAttendCount(): Int = if (utilConfig.has("minAttendCount")) {
        utilConfig.getInt("minAttendCount")
    } else {
        6
    }

    fun getLogoutList(): ArrayList<String> = if (utilConfig.has("logout")) {
        arrayListOf<String>().apply {
            utilConfig.getJSONArray("logout").also {
                if (it.length() < 1) return@apply
                for(i in 0 until it.length()){
                    this.add(it.getString(i))
                }
            }
        }
    } else {
        arrayListOf()
    }

    fun getAdminList(): ArrayList<String> = if (utilConfig.has("adminList")) {
        arrayListOf<String>().apply {
            utilConfig.getJSONArray("adminList").also {
                for(i in 0 until it.length()){
                    this.add(it.getString(i))
                }
            }
        }
    } else {
        arrayListOf()
    }

//    fun isAdmin(userId: String): Boolean = true // TODO test
    fun isAdmin(userId: String): Boolean = getAdminList().contains(userId)
}