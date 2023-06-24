package com.shqiansha.ui.popup

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shqiansha.ui.R
import com.shqiansha.ui.UIConfig

class TwoLayerOptionPopup(
    bindView: View,
    private val values: Map<String, List<String>>,
    private val selected: String = "",
) : BasePopup(R.layout.popup_option_two_layer, bindView) {

    private val view by lazy { contentView }
    private val adapter = OptionAdapter()
    private val subAdapter = OptionAdapter()
    private var onOptionSelectListener: OnOptionSelectListener? = null

    init {
        initState()
        initView()
        initData()
    }

    private fun initState() {
        setAutoShowDarkBackground(true)
    }

    private fun initView() {
        val rvOption = view.findViewById<RecyclerView>(R.id.rvOption)
        rvOption.itemAnimator = null
        rvOption.adapter = adapter
        rvOption.layoutManager = LinearLayoutManager(view.context)
        adapter.onItemClickListener = OptionHolder.OnItemClickListener { _, position ->
            val subData = values[adapter.getItem(position)] ?: emptyList()
            if (subData.isEmpty()) {
                onOptionSelectListener?.onSelect(adapter.getItem(position))
                dismiss()
            } else {
                adapter.setSelected(position)
                subAdapter.updateData(subData)
            }
        }
        adapter.textGravity = Gravity.CENTER
        val rvSubOption = view.findViewById<RecyclerView>(R.id.rvSubOption)
        rvSubOption.itemAnimator = null
        rvSubOption.adapter = subAdapter
        rvSubOption.layoutManager = LinearLayoutManager(view.context)
        subAdapter.onItemClickListener = OptionHolder.OnItemClickListener { _, position ->
            onOptionSelectListener?.onSelect(subAdapter.getItem(position))
            dismiss()
        }
    }

    private fun initData() {
        adapter.updateData(values.keys.toList())
        if (selected.isEmpty()) return
        for ((i, key) in values.keys.withIndex()) {
            if (key == selected) {
                adapter.setSelected(i)
                view.findViewById<RecyclerView>(R.id.rvOption).scrollToPosition(i)
                return
            } else {
                val items = values[key] ?: emptyList()
                for ((j, item) in items.withIndex()) {
                    if (item == selected) {
                        adapter.setSelected(i)
                        view.findViewById<RecyclerView>(R.id.rvOption).scrollToPosition(i)
                        subAdapter.updateData(items)
                        subAdapter.setSelected(j)
                        view.findViewById<RecyclerView>(R.id.rvSubOption).scrollToPosition(j)
                        return
                    }
                }
            }
        }
    }

    fun setItemTextColor(color: Int) = apply { adapter.color = color }

    fun setOnOptionSelectListener(listener: OnOptionSelectListener) =
        apply { this.onOptionSelectListener = listener }

    class OptionAdapter : RecyclerView.Adapter<OptionHolder>() {
        var color = Color.parseColor("#333333")
        private val data = arrayListOf<String>()
        private val selected = arrayListOf<Boolean>()
        var textGravity = Gravity.START
        var onItemClickListener: OptionHolder.OnItemClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            return OptionHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_popup_option_two_layer, parent, false),
                onItemClickListener
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: OptionHolder, position: Int) {
            val item = data[position]
            val isSelected = selected[position]
            holder.tvOption.run {
                text = item
                if (isSelected) {
                    setTextColor(UIConfig.colorPrimary)
                } else {
                    setTextColor(color)
                }
                gravity = textGravity
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateData(list: List<String>) {
            data.clear()
            data.addAll(list)
            selected.clear()
            for (i in list.indices) {
                selected.add(false)
            }
            notifyDataSetChanged()
        }

        fun getItem(position: Int): String {
            return data[position]
        }

        fun setSelected(position: Int) {
            var old = -1
            for (i in selected.indices) {
                if (selected[i]) {
                    old = i
                    break
                }
            }
            if (old == position) return
            if (old != -1) {
                selected[old] = false
                notifyItemChanged(old)
            }
            selected[position] = true
            notifyItemChanged(position)
        }
    }

    class OptionHolder(view: View, private val onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvOption: TextView = view.findViewById(R.id.tvOption)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.let { onItemClickListener?.onClick(it, adapterPosition) }
        }

        fun interface OnItemClickListener {
            fun onClick(view: View, position: Int)
        }
    }

    fun interface OnOptionSelectListener {
        fun onSelect(data: String)
    }
}