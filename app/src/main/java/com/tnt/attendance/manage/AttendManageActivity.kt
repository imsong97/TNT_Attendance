package com.tnt.attendance.manage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.tnt.attendance.R
import com.tnt.attendance.attendancemain.AttendancePresenter
import com.tnt.attendance.attendancemain.recylcerview.MemberListAdapter
import com.tnt.attendance.databinding.ActivityManageBinding
import com.tnt.attendance.manage.dialog.CalendarDialog
import com.tnt.attendance.manage.recylcerview.MemberSelectAdapter
import com.tnt.attendance_data.entity.ClubMember
import com.tnt.commonlibrary.RxBus
import com.tnt.commonlibrary.RxEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlin.collections.ArrayList

class AttendManageActivity : AppCompatActivity(), AttendManageContract.View {

    private lateinit var binding: ActivityManageBinding
    private lateinit var presenter: AttendManageContract.Presenter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var selectedDate: CalendarDay
    private var memberSelectAdapter: MemberSelectAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AttendManageActivity, R.layout.activity_manage)
        presenter = AttendManagePresenter(this@AttendManageActivity)

        title = getString(com.tnt.commonlibrary.R.string.title_attend_manage)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_manage, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.btn_regist) {
            memberSelectAdapter?.getSelectedList()?.let {
                if (it.isEmpty()) {
                    showErrorDialog("인원을 선택하세요.") // TODO string
                    return false
                }
                showProgressbar()
                val attendMap = hashMapOf<String, ArrayList<String>>().apply {
                    this[selectedDate.day.toString()] = it
                }
                presenter.setAttendMember(selectedDate.year.toString(), selectedDate.month.toString(), attendMap)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        CalendarDialog(this@AttendManageActivity, null).show()

        binding.recyclerView.apply {
            this.setHasFixedSize(true)
            this.layoutManager = GridLayoutManager(this@AttendManageActivity, 2)
        }

        binding.btnCalendarDialog.setOnClickListener {
            CalendarDialog(this@AttendManageActivity, null).show()
        }

        RxBus.listen(RxEvent.SelectedDate::class.java)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showProgressbar()
                presenter.getMemberList()
                selectedDate = it.date
                binding.txtSelectedDate.text = "${selectedDate.year}년 ${selectedDate.month}월 ${selectedDate.day}일" // TODO 요일?
            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun setAdapter(list: ArrayList<ClubMember>) {
        hideProgressbar()
        if (list.isEmpty()) {
            showErrorDialog("")// TODO dialog
            return
        }
        if (memberSelectAdapter == null) {
            memberSelectAdapter = MemberSelectAdapter(list)
            binding.recyclerView.adapter = memberSelectAdapter
        } else {
            memberSelectAdapter?.setData(list)
            memberSelectAdapter?.notifyDataSetChanged()
        }
    }

    override fun successAttendMember() {
        Toast.makeText(this@AttendManageActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show() // TODO String
        finish()
    }

    override fun showErrorDialog(msg: String) {
        hideProgressbar()
    }

    private fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}