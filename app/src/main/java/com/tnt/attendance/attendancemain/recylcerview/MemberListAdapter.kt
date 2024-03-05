package com.tnt.attendance.attendancemain.recylcerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnt.attendance.R
import com.tnt.attendance.databinding.ItemAttendMemberListBinding

class MemberListAdapter(
    private var list: ArrayList<String>
) : RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAttendMemberListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_attend_member_list, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setText(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setMemberList(list: ArrayList<String>) {
        this.list = list
    }

    class ViewHolder(
        private val binding: ItemAttendMemberListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setText(name: String) {
            binding.name.text = name
        }
    }
}