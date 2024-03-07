package com.tnt.attendance_data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single


class Firestore {

    // FirebaseFirestore.getInstance() 가 synchronized 처리 되어있는데, 또 쓰는게 맞나?
    companion object {
        private var mInstance: Firestore? = null
        val instance: Firestore?
            get() = synchronized(Firestore::class.java) {
                if (mInstance == null) {
                    mInstance = Firestore()
                }
                return mInstance
            }
    }

    fun getMonthList(year: String): Single<ArrayList<String>> =
        Single.create { emitter ->
            val monthList = arrayListOf<String>()
            FirebaseFirestore.getInstance().collection(year).get()
                .addOnCompleteListener {
                    it.result.forEach { doc ->
                        monthList.add(doc.id)
                    }
                    emitter.onSuccess(monthList)
                }
                .addOnFailureListener {
                    Log.e("Firestore - getMonthList", it.printStackTrace().toString())
                    emitter.onError(Throwable("Firestore - getMonthList"))
                }
        }

    fun getAttendMemberList(year: String, month: String): Single<String> =
        Single.create { emitter ->
            FirebaseFirestore.getInstance().collection(year).document(month).get()
                .addOnCompleteListener {
                    emitter.onSuccess(it.result.data?.toString() ?: "")
                }
                .addOnFailureListener {
                    Log.e("Firestore - getAttendMemberList", it.printStackTrace().toString())
                    emitter.onError(Throwable("Firestore - getAttendMemberList"))
                }
        }
}