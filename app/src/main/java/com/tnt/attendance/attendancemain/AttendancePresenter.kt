package com.tnt.attendance.attendancemain

import com.tnt.attendance_data.AttendanceDataRepository
import com.tnt.commonlibrary.ContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class AttendancePresenter(
    private val view: AttendanceContract.View,
    private val contextWrapper: ContextWrapper
) : AttendanceContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val repository by lazy { AttendanceDataRepository() }
    private var attendList: Map<String, ArrayList<String>>? = null

    override fun getSpinnerData(year: String) {
        repository.getMonthList(year)
            ?.map {
                ArrayList(it.sortedDescending())
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                view.setSpinner(it)
            }, {
                it.printStackTrace()
                view.setSpinner(arrayListOf())
                view.showErrorDialog("") // TODO
            })?.also {
                compositeDisposable.add(it)
            }
    }

    override fun getAttendDateList(year: String, month: String) {
        repository.getAttendDateList(year, month)
            ?.map {
                attendList = it
                ArrayList(it.keys.toList())
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                view.setDayDecorator(it)
            }, {
                it.printStackTrace()
                view.showErrorDialog("") // TODO
            })?.also {
                compositeDisposable.add(it)
            }
    }

    override fun getAttendMemberList(date: String) = attendList?.get(date) ?: arrayListOf()

    override fun dispose() {
        compositeDisposable.dispose()
    }
}