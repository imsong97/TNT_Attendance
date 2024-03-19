package com.tnt.attendance.attendancemain.recylcerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnt.attendance.R
import com.tnt.attendance.databinding.ItemAttendRankingCellBinding

class RankingListAdapter(
    private var listMap: Map<String, ArrayList<Int>>
) : RecyclerView.Adapter<RankingListAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding: ItemAttendRankingCellBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_attend_ranking_cell, parent, false)
        return ViewHolder(binding, context!!).apply {
	    }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = listMap.keys.toList()[position]
        holder.setData(position + 1, key, listMap[key]?.size ?: 0)
    }

    override fun getItemCount(): Int = listMap.size

    fun setData(data: Map<String, ArrayList<Int>>){
        listMap = data
    }

    class ViewHolder(private val binding: ItemAttendRankingCellBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun setData(index: Int, name: String, attendCount: Int) {
            binding.index.text = index.toString()
            binding.name.text = name
            binding.count.text = "${attendCount}회"
        }
    }
}