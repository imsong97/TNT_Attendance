package com.tnt.attendance_data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.reactivex.Single


class Firestore {

    private val ATTENDANCE_STATUS by lazy { "attendance_status" }

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

    fun getMonthList(year: String, nowMonth: String): Single<ArrayList<String>> =
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
        }.map {
            // 새로운 월 바뀔 때 doc이 없을 시 자동 추가
            if (!it.contains(nowMonth)) {
                setNewDocument(year, nowMonth)
                it.add(nowMonth)
            }
            it
        }

    fun getAttendMemberList(year: String, month: String): Single<String> =
        Single.create { emitter ->
            FirebaseFirestore.getInstance().collection(year).document(month).get()
                .addOnCompleteListener {
                    println(it.result.data?.toString())
                    emitter.onSuccess(it.result.data?.toString() ?: "")
                }
                .addOnFailureListener {
                    Log.e("Firestore - getAttendMemberList", it.printStackTrace().toString())
                    emitter.onError(Throwable("Firestore - getAttendMemberList"))
                }
        }

    fun setAttendList(year: String, month: String, attendMap: Map<String, ArrayList<String>>): Single<Boolean> =
        Single.create { emitter ->
            FirebaseFirestore.getInstance()
                .collection(year).document(month)
                .set(attendMap, SetOptions.merge())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(true)
                    } else {
                        emitter.onSuccess(false)
                    }
                }
                .addOnFailureListener {
                    Log.e("Firestore - setAttendList", it.printStackTrace().toString())
                    emitter.onSuccess(false)
                }
        }

    fun setAttendStatus(docName: String, field: Map<String, ArrayList<Int>>): Single<Boolean> =
        Single.create { emitter ->
            FirebaseFirestore.getInstance()
                .collection(ATTENDANCE_STATUS).document(docName)
                .set(field, SetOptions.merge())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(true)
                    } else {
                        emitter.onSuccess(false)
                    }
                }
                .addOnFailureListener {
                    Log.e("Firestore - setAttendStatus", it.printStackTrace().toString())
                    emitter.onSuccess(false)
                }
        }

    private fun setNewDocument(year: String, month: String) {
        FirebaseFirestore.getInstance().collection(year).document(month).set(hashMapOf<Int, ArrayList<String>>())
            .addOnSuccessListener {
                println("add New Document success")
            }.addOnFailureListener {
                it.printStackTrace()
                println("add New Document fail")

            }
    }
}