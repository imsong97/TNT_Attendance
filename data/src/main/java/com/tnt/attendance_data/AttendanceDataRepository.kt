package com.tnt.attendance_data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tnt.attendance_data.entity.Member
import com.tnt.attendance_data.remote.Firestore
import com.tnt.attendance_data.remote.RealtimeDB
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AttendanceDataRepository {

    /**
     * spinner의 월 데이터를 가져오는 메서드
     * */
    fun getMonthList(year: String): Single<ArrayList<String>>? =
        Firestore.instance?.getMonthList(year)
            ?.subscribeOn(Schedulers.io())

    /**
     * 날짜 별 출석자 리스트를 가져오는 메서드
     * */
    fun getAttendDateList(year: String, month: String): Single<Map<String, ArrayList<String>>>? =
        Firestore.instance?.getAttendMemberList(year, month)
            ?.subscribeOn(Schedulers.io())
            ?.map {
                Gson().run {
                    val type = object : TypeToken<Map<String, ArrayList<String>>>() {}.type
                    this.fromJson(it, type)
                }
            }

    /**
     * 클럽 멤버 정보를 가져오는 메서드
     * */
    fun getMemberList(): Single<ArrayList<Member>>? =
        RealtimeDB.instance?.getMemberList()
            ?.subscribeOn(Schedulers.io())
            ?.map {
                Gson().run {
                    val type = object : TypeToken<ArrayList<Member>>() {}.type
                    this.fromJson(it, type)
                }
            }
}