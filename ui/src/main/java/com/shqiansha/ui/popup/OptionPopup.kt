package com.shqiansha.ui.popup

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shqiansha.ui.R
import com.shqiansha.ui.dialog.OptionDialog


class OptionPopup(bindView: View, val values: Array<String>, val maxWidth: Int = 0) :
    BasePopup(R.layout.popup_option, bindView) {
    private val view by lazy { contentView }
    private val adapter = OptionAdapter()
    private var onOptionSelectListener: OnOptionSelectListener? = null

    init {
        initState()
        initView()
    }

    private fun initState() {
        setAutoShowDarkBackground(false)
    }

    private fun initView() {
        val rvOption = view.findViewById<RecyclerView>(R.id.rvOption)
        if (maxWidth > 0) {
            rvOption.layoutParams.width = maxWidth
            width = maxWidth
        }
        rvOption.adapter = adapter
        adapter.onItemClickListener = object : OptionHolder.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                onOptionSelectListener?.onSelect(adapter.data[position])
                dismiss()
            }
        }
        adapter.updateData(values.toList())
    }

    fun setItemGravity(gravity: Int) {
        adapter.gravity = gravity
    }

    fun setItemTextColor(color: Int) {
        adapter.color = color
    }

    fun setOnOptionSelectListener(listener: OnOptionSelectListener): OptionPopup {
        this.onOptionSelectListener = listener
        return this
    }

    fun setOnOptionSelectListener(listener: (value: String) -> Unit) {
        this.onOptionSelectListener = object : OnOptionSelectListener {
            override fun onSelect(value: String) {
                listener.invoke(value)
            }
        }
    }

    class OptionAdapter : RecyclerView.Adapter<OptionHolder>() {
        var gravity = Gravity.CENTER
        var color = Color.parseColor("#333333")

        val data = arrayListOf<String>()
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
                if (position == data.size - 1) R.drawable.selector_bg_ffffff_f5f5f5_10_bottom else R.drawable.selector_bg_ffffff_f5f5f5
            val item = data[position]
            holder.tvOption.text = item
            holder.tvOption.setBackgroundResource(backgroundRes)
            holder.tvOption.gravity = gravity
            holder.tvOption.setTextColor(color)
        }

        fun updateData(list: List<String>) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }
    }

    class OptionHolder(view: View, private val onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvOption = view.findViewById<TextView>(R.id.tvOption)

        init {
            tvOption.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.let { onItemClickListener?.onClick(it, adapterPosition) }
        }

        interface OnItemClickListener {
            fun onClick(view: View, position: Int)
        }
    }

    interface OnOptionSelectListener {
        fun onSelect(value: String)
    }
}