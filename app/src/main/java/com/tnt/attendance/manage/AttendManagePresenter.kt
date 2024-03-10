package com.tnt.attendance.manage

import com.tnt.attendance_data.AttendanceDataRepository
import io.reactivex.disposables.CompositeDisposable

class AttendManagePresenter(
    private val view: AttendManageContract.View,
) : AttendManageContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val repository by lazy { AttendanceDataRepository() }

    override fun getMemberList() {

    }

    override fun dispose() {
        compositeDisposable.dispose()
    }
}