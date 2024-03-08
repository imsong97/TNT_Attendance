package com.tnt.attendance_data.remote

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import io.reactivex.Single

class RealtimeDB {

    private val database by lazy {
        Firebase.database("")
    }

    // FirebaseDatabase.getInstance() 가 synchronized 처리 되어있는데, 또 쓰는게 맞나?
    companion object {
        private var mInstance: RealtimeDB? = null
        val instance: RealtimeDB?
            get() = synchronized(RealtimeDB::class.java) {
                if (mInstance == null) {
                    mInstance = RealtimeDB()
                }
                return mInstance
            }
    }

    //[{isDormancy=false, isExecutive=true, name=, position=13}, {isDormancy=false, isExecutive=true, name=, position=11}]
    fun getMemberList(): Single<String> =
        Single.create { emitter ->
            database.reference.child("member_list").get().addOnSuccessListener {
                emitter.onSuccess(it.value?.toString() ?: "")
            }.addOnFailureListener {
                it.printStackTrace()
                emitter.onError(Throwable(it.printStackTrace().toString()))
            }
        }
}