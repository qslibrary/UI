package com.shqiansha.ui.popup

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shqiansha.ui.R
import com.shqiansha.ui.UIConfig

class MultiOptionPopup(
    bindView: View,
    private val values: List<String>,
    private val selected: List<String> = emptyList(),
) : BasePopup(R.layout.popup_option_multi, bindView) {

    private val view by lazy { contentView }
    private val adapter = OptionAdapter()
    private var onOptionSelectListener: OnOptionSelectListener? = null

    init {
        initState()
        initView()
    }

    private fun initState() {
        setAutoShowDarkBackground(true)
    }

    private fun initView() {
        val tvReset = view.findViewById<View>(R.id.tvReset)
        tvReset.setOnClickListener {
            adapter.clearSelected()
        }
        val tvConfirm = view.findViewById<View>(R.id.tvConfirm)
        ContextCompat.getDrawable(view.context, R.drawable.ui_shape_confirm)?.let {
            it.setTint(UIConfig.colorPrimary)
            tvConfirm.background = it
        }
        tvConfirm.setOnClickListener {
            onOptionSelectListener?.onSelect(adapter.getSelectedData())
            dismiss()
        }
        val rvOption = view.findViewById<RecyclerView>(R.id.rvOption)
        rvOption.itemAnimator = null
        rvOption.adapter = adapter
        rvOption.layoutManager = GridLayoutManager(view.context, 2)
        adapter.onItemClickListener = OptionHolder.OnItemClickListener { _, position ->
            adapter.switchSelected(position)
        }
        adapter.updateData(values, selected)
    }

    fun setItemTextColor(color: Int) = apply { adapter.color = color }

    fun setOnOptionSelectListener(listener: OnOptionSelectListener) =
        apply { this.onOptionSelectListener = listener }

    class OptionAdapter : RecyclerView.Adapter<OptionHolder>() {
        var color = Color.parseColor("#333333")
        private val data = arrayListOf<String>()
        private val selected = arrayListOf<Boolean>()
        var onItemClickListener: OptionHolder.OnItemClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            return OptionHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_popup_option_multi, parent, false), onItemClickListener
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
            }
            holder.ivSelected.visibility = if (isSelected) View.VISIBLE else View.GONE
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateData(list: List<String>, selectedList: List<String>) {
            data.clear()
            data.addAll(list)
            selected.clear()
            for (i in list.indices) {
                var isSelected = false
                for (s in selectedList) {
                    if (data[i] == s) {
                        isSelected = true
                        break
                    }
                }
                selected.add(isSelected)
            }
            notifyDataSetChanged()
        }

        fun switchSelected(position: Int) {
            selected[position] = !selected[position]
            notifyItemChanged(position)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun clearSelected() {
            for (i in selected.indices) selected[i] = false
            notifyDataSetChanged()
        }

        fun getSelectedData(): List<String> {
            val list = mutableListOf<String>()
            for (i in selected.indices) {
                if (selected[i]) {
                    data.getOrNull(i)?.let { list.add(it) }
                }
            }
            return list
        }
    }

    class OptionHolder(view: View, private val onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvOption: TextView = view.findViewById(R.id.tvOption)
        val ivSelected: ImageView = view.findViewById(R.id.ivSelected)

        init {
            view.setOnClickListener(this)
            ContextCompat.getDrawable(view.context, R.drawable.ui_ic_selected)?.let {
                it.setTint(UIConfig.colorPrimary)
                ivSelected.setImageDrawable(it)
            }
        }

        override fun onClick(v: View?) {
            v?.let { onItemClickListener?.onClick(it, adapterPosition) }
        }

        fun interface OnItemClickListener {
            fun onClick(view: View, position: Int)
        }
    }

    fun interface OnOptionSelectListener {
        fun onSelect(data: List<String>)
    }
}