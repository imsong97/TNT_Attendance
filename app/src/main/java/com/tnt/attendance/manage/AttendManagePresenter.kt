package com.tnt.attendance.manage

import com.tnt.attendance_data.AttendanceDataRepository
import com.tnt.attendance_data.ErrorRepository
import com.tnt.attendance_data.entity.ClubMember
import com.tnt.commonlibrary.wrapper.ContextWrapper
import com.tnt.commonlibrary.wrapper.PreferenceWrapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AttendManagePresenter(
    private val view: AttendManageContract.View,
    private val contextWrapper: ContextWrapper,
    private val preference: PreferenceWrapper
) : AttendManageContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val repository by lazy { AttendanceDataRepository() }
    private val userPhone by lazy { preference.getUserId() }
    private lateinit var memberList: ArrayList<ClubMember>

    override fun getMemberList() {
        if (::memberList.isInitialized && memberList.isNotEmpty()) {
            view.setAdapter(memberList)
            return
        }

        repository.getMemberListRC()
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
                ErrorRepository.instance?.sendError(userPhone, "getMemberList - ${it.printStackTrace()}")
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
                ErrorRepository.instance?.sendError(userPhone, "setAttendMember - ${it.printStackTrace()}")
            })?.also {
                compositeDisposable.add(it)
            }
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }
}