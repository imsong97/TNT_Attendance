package com.tnt.attendance.manage

import com.tnt.attendance_data.AttendanceDataRepository
import com.tnt.attendance_data.entity.ClubMember
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AttendManagePresenter(
    private val view: AttendManageContract.View,
) : AttendManageContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val repository by lazy { AttendanceDataRepository() }
    private lateinit var memberList: ArrayList<ClubMember>

    override fun getMemberList() {
        if (::memberList.isInitialized) {
            view.setAdapter(memberList)
            return
        }

        repository.getMemberList()
            ?.observeOn(Schedulers.computation())
            ?.map {
                ArrayList(it.sortedWith(compareBy(ClubMember::position)))
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                memberList = it
                view.setAdapter(it)
            }, {
                it.printStackTrace()
                view.setAdapter(arrayListOf())
            })?.also {
                compositeDisposable.add(it)
            }
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }
}