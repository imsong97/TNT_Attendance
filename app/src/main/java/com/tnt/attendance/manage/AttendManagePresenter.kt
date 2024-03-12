package com.tnt.attendance.manage

import com.tnt.attendance_data.AttendanceDataRepository
import com.tnt.attendance_data.entity.ClubMember
import io.reactivex.Single
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
        if (::memberList.isInitialized && memberList.isNotEmpty()) {
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

    override fun setAttendMember(
        year: String,
        month: String,
        attendList: Map<String, ArrayList<String>>
    ) {
        repository.setAttendList(year, month, attendList)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (it) {
                    view.successAttendMember()
                } else {
                    view.showErrorDialog("") // TODO
                }
            }, {
                it.printStackTrace()
                view.showErrorDialog("") // TODO
            })?.also {
                compositeDisposable.add(it)
            }
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }
}