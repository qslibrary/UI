package com.shqiansha.ui.popup

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shqiansha.ui.R
import com.shqiansha.ui.UIConfig

class OptionPopup(
    bindView: View,
    val values: Array<String>,
    val default: String = "",
    val maxWidth: Int = 0
) : BasePopup(R.layout.popup_option, bindView) {

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
        val rvOption = view.findViewById<RecyclerView>(R.id.rvOption)
        if (maxWidth > 0) {
            rvOption.layoutParams.width = maxWidth
            width = maxWidth
        }
        adapter.default = default
        rvOption.adapter = adapter
        adapter.onItemClickListener = OptionHolder.OnItemClickListener { view, position ->
            onOptionSelectListener?.onSelect(adapter.data[position])
            dismiss()
        }
        adapter.updateData(values.toList())
    }

    fun setItemGravity(gravity: Int) = apply { adapter.textGravity = gravity }
    fun setItemTextColor(color: Int) = apply { adapter.textColor = color }

    fun setOnOptionSelectListener(listener: OnOptionSelectListener) =
        apply { this.onOptionSelectListener = listener }

    class OptionAdapter : RecyclerView.Adapter<OptionHolder>() {
        var textGravity = Gravity.START
        var textColor = Color.parseColor("#333333")
        val data = arrayListOf<String>()
        var default = ""
        var onItemClickListener: OptionHolder.OnItemClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            return OptionHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_popup_option, parent, false), onItemClickListener
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: OptionHolder, position: Int) {
            val backgroundRes =
                if (position == data.size - 1) R.drawable.ui_selector_bg_ffffff_f5f5f5_10_bottom else R.drawable.ui_selector_bg_ffffff_f5f5f5
            val item = data[position]
            holder.tvOption.run {
                text = item
                setBackgroundResource(backgroundRes)
                gravity = textGravity
                if (item == default) {
                    setTextColor(UIConfig.colorPrimary)
                } else {
                    setTextColor(textColor)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updateData(list: List<String>) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
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
        fun onSelect(value: String)
    }
}