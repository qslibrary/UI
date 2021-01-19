package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.shqiansha.ui.R
import kotlinx.android.synthetic.main.dialog_option.*

/**
 * 底部选项dialog
 */
open class OptionDialog : BaseBottomDialog() {
    private val adapter = OptionAdapter()
    private var onOptionSelectListener: OnOptionSelectListener? = null
    private var mTag = "optionDialog"
    private var values: Array<String> = arrayOf()
    private var title: String = "请选择"
    private var mFragmentManager: FragmentManager? = null
    var autoDismiss = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_option, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        tvDialogCancel.setOnClickListener { dismiss() }
        tvDialogTitle.text = title
        rvOption.adapter = adapter
        onOptionSelectListener?.let {
            adapter.onItemClickListener = object : OptionHolder.OnItemClickListener {
                override fun onClick(view: View, position: Int) {
                    onOptionSelectListener?.onSelect(adapter.data[position])
                    if (autoDismiss) dismiss()
                }
            }
        }
        adapter.updateData(values.toList())
    }

    fun setTag(tag: String): OptionDialog {
        mTag = tag
        return this
    }

    fun setValues(values: Array<String>): OptionDialog {
        this.values = values
        return this
    }

    fun setTitle(title: String): OptionDialog {
        this.title = title
        return this
    }

    fun setFragmentManager(fragmentManager: FragmentManager): OptionDialog {
        this.mFragmentManager = fragmentManager
        return this
    }

    fun setOnOptionSelectListener(listener: OnOptionSelectListener): OptionDialog {
        this.onOptionSelectListener = listener
        return this
    }

    fun setOnOptionSelectListener(listener: (value:String)->Unit){
        this.onOptionSelectListener=object :OnOptionSelectListener{
            override fun onSelect(value: String) {
                listener.invoke(value)
            }
        }
    }

    fun show(manager: FragmentManager) {
        show(manager, mTag)
    }

    fun show(manager: FragmentManager, values: Array<String>) {
        this.values = values
        show(manager)
    }

    fun show() {
        mFragmentManager?.let {
            show(it)
        }
    }

    fun show(listener: OnOptionSelectListener) {
        setOnOptionSelectListener(listener)
        show()
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
                if (position == data.size - 1) R.drawable.selector_bg_ffffff_f5f5f5_10_bottom else R.drawable.selector_bg_ffffff_f5f5f5
            val item = data[position]
            holder.tvOption.text = item
            holder.tvOption.setBackgroundResource(backgroundRes)

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