package com.shqiansha.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.shqiansha.ui.R
import com.shqiansha.ui.databinding.DialogOptionBinding

/**
 * 底部选项dialog
 */
open class OptionDialog : BaseBottomDialog() {
    private val adapter = OptionAdapter()
    private var onOptionSelectListener: OnOptionSelectListener? = null
    private var values: Array<String> = arrayOf()
    private var title: String = "请选择"
    var autoDismiss = true
    private var binding: DialogOptionBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogOptionBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding?.run {
            tvDialogCancel.setOnClickListener { dismiss() }
            tvDialogTitle.text = title
            rvOption.adapter = adapter
        }
        onOptionSelectListener?.let {
            adapter.onItemClickListener = OptionHolder.OnItemClickListener { _, position ->
                onOptionSelectListener?.onSelect(adapter.data[position])
                if (autoDismiss) dismiss()
            }
        }
        adapter.updateData(values.toList())
    }

    fun setValues(values: Array<String>) = apply { this.values = values }
    fun setTitle(title: String) = apply { this.title = title }

    fun setOnOptionSelectListener(listener: OnOptionSelectListener) =
        apply { this.onOptionSelectListener = listener }

    fun show(manager: FragmentManager) {
        show(manager, "optionDialog")
    }

    class OptionAdapter : RecyclerView.Adapter<OptionHolder>() {
        val data = arrayListOf<String>()
        var onItemClickListener: OptionHolder.OnItemClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            return OptionHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dialog_option, parent, false), onItemClickListener
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: OptionHolder, position: Int) {
            val backgroundRes =
                if (position == data.size - 1) R.drawable.ui_selector_bg_ffffff_f5f5f5_10_bottom else R.drawable.ui_selector_bg_ffffff_f5f5f5
            val item = data[position]
            holder.tvOption.text = item
            holder.tvOption.setBackgroundResource(backgroundRes)

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
            tvOption.setOnClickListener(this)
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