package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shqiansha.ui.UIConfig
import com.shqiansha.ui.databinding.DialogPickerBinding
import com.shqiansha.ui.view.wheel.WheelAdapter

class PickerDialog : BaseBottomDialog {
    constructor(values: Array<String>, selectedValue: String? = null) {
        adapter = PickerAdapter(values.toList())
        this.selectedPosition = values.indexOf(selectedValue)
    }

    constructor(values: Array<String>, selectedPosition: Int) {
        adapter = PickerAdapter(values.toList())
        this.selectedPosition = selectedPosition
    }

    private val adapter: PickerAdapter
    var selectedPosition = 0
    var confirmationText: CharSequence = "确定"
    var cancelText: CharSequence = "取消"
    var title: CharSequence = ""

    /**
     * 点击确定和取消是否自动消失
     */
    var autoDismiss = true

    /**
     * 是否循环滚动
     */
    var cyclic = false

    private var binding: DialogPickerBinding? = null

    private var onClickConfirmationListener: OnClickConfirmationListener? = null
    private var onClickCancelListener: OnClickCancelListener? = null

    init {
        disableScroll()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPickerBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding?.run {
            wvPicker.adapter = adapter
            wvPicker.setCyclic(cyclic)
            wvPicker.currentItem = selectedPosition
            tvDialogCancel.text = cancelText
            tvDialogConfirm.text = confirmationText
            tvDialogConfirm.setTextColor(UIConfig.colorPrimary)
            tvDialogTitle.text = title

            tvDialogConfirm.setOnClickListener {
                val position = wvPicker.currentItem
                onClickConfirmationListener?.onClick(position, adapter.getItem(position))
                if (autoDismiss) dismiss()
            }
            tvDialogCancel.setOnClickListener {
                onClickCancelListener?.onClick()
                if (autoDismiss) dismiss()
            }
        }
    }

    fun setOnClickConfirmationListener(listener: OnClickConfirmationListener) = apply {
        onClickConfirmationListener = listener
    }

    fun setOnClickCancelListener(listener: OnClickCancelListener) = apply {
        onClickCancelListener = listener
    }

    fun interface OnClickConfirmationListener {
        fun onClick(position: Int, value: String)
    }

    fun interface OnClickCancelListener {
        fun onClick()
    }

    class PickerAdapter(private val data: List<String>) : WheelAdapter<String> {
        override fun indexOf(o: String?): Int {
            return data.indexOf(o)
        }

        override fun getItemsCount(): Int {
            return data.size
        }

        override fun getItem(index: Int): String {
            return data[index]
        }
    }
}