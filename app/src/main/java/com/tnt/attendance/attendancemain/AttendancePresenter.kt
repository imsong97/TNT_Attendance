package com.tnt.attendance.attendancemain

import com.tnt.attendance_data.AttendanceDataRepository
import com.tnt.commonlibrary.wrapper.ContextWrapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AttendancePresenter(
    private val view: AttendanceContract.View,
    private val contextWrapper: ContextWrapper
) : AttendanceContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val repository by lazy { AttendanceDataRepository() }
    private var attendList: Map<String, ArrayList<String>>? = null

    override fun getSpinnerData(year: String, nowMonth: String) {
        repository.getMonthList(year, nowMonth)
            ?.map {
                ArrayList(it.sortedDescending())
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.map {
                view.setSpinner(it)
            }
            ?.observeOn(Schedulers.io())
            ?.flatMap {
                getDateList(year, nowMonth)
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                view.setDayDecorator(it)
            }, {
                it.printStackTrace()
                view.setSpinner(arrayListOf())
                view.setDayDecorator(arrayListOf())
                view.showErrorDialog("") // TODO
            })?.also {
                compositeDisposable.add(it)
            }
    }

    override fun getAttendDateList(year: String, month: String) {
        getDateList(year, month)
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

    private fun getDateList(year: String, month: String) =
        Single.zip(
            repository.getAttendDateList(year, month),
            Single.just(1).subscribeOn(Schedulers.io()).delay(700, TimeUnit.MILLISECONDS),
            BiFunction { list, t2 ->
                return@BiFunction list
            }
        )?.map {
            attendList = it
            ArrayList(it.keys.toList())
        }

    override fun getAttendMemberList(date: String) = attendList?.get(date) ?: arrayListOf()

    override fun dispose() {
        compositeDisposable.dispose()
    }
}