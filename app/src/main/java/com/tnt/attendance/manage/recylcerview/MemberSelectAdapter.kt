package com.tnt.attendance.manage.recylcerview

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnt.attendance.R
import com.tnt.attendance.databinding.ItemMemeberSelectCellBinding
import com.tnt.attendance_data.entity.ClubMember

class MemberSelectAdapter(
    private var memberList: ArrayList<ClubMember>
) : RecyclerView.Adapter<MemberSelectAdapter.ViewHolder>() {

    private var context: Context? = null
    private val selectedMemberList by lazy { arrayListOf<String>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding: ItemMemeberSelectCellBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_memeber_select_cell, parent, false)
        return ViewHolder(binding, context!!).apply {
            binding.checkbox.setOnCheckedChangeListener { _, isCheck ->
                if (isCheck) {
                    selectedMemberList.add(binding.checkbox.text.toString())
                } else {
                    selectedMemberList.remove(binding.checkbox.text.toString())
                }
                println(selectedMemberList)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setName(memberList[position].name, memberList[position].isDormancy)
    }

    override fun getItemCount(): Int = memberList.size

    fun setData(list: ArrayList<ClubMember>) {
        memberList = list
    }

    fun getSelectedList() = selectedMemberList

    class ViewHolder(
        private val binding: ItemMemeberSelectCellBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setName(name: String, isDormancy: Boolean) {
            SpannableString(name).apply {
                val color = if (isDormancy) {
                    ContextCompat.getColor(context, com.tnt.commonlibrary.R.color.color_757575)
                } else {
                    ContextCompat.getColor(context, com.tnt.commonlibrary.R.color.color_212121)
                }
                this.setSpan(ForegroundColorSpan(color), 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }.also {
                binding.checkbox.text = it
            }

            binding.checkbox.isEnabled = !isDormancy
        }
    }
}