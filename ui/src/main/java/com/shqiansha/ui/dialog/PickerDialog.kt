package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shqiansha.ui.R
import com.shqiansha.ui.view.wheel.WheelAdapter
import kotlinx.android.synthetic.main.dialog_picker.*
import kotlinx.android.synthetic.main.dialog_picker.tvDialogCancel
import kotlinx.android.synthetic.main.dialog_picker.tvDialogConfirm
import kotlinx.android.synthetic.main.dialog_picker.tvDialogTitle

class PickerDialog : BaseBottomDialog {

    constructor(values: Array<String>, selectedValue: String? = null) {
        adapter = PickerAdapter(values.toList())
        this.selectedPosition = values.indexOf(selectedValue)
    }

    constructor(values: Array<String>, selectedPosition: Int) {
        adapter = PickerAdapter(values.toList())
        this.selectedPosition = selectedPosition
    }

    var selectedPosition = 0
    val adapter: PickerAdapter
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
        return inflater.inflate(R.layout.dialog_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        wvPicker.adapter = adapter
        wvPicker.setCyclic(cyclic)

        wvPicker.currentItem = selectedPosition
        tvDialogCancel.text = cancelText
        tvDialogConfirm.text = confirmationText
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

    fun setOnClickConfirmationListener(listener: (position: Int, value: String) -> Unit) {
        onClickConfirmationListener = object : OnClickConfirmationListener {
            override fun onClick(position: Int, value: String) {
                listener.invoke(position, value)
            }
        }
    }

    fun setOnClickCancelListener(listener: () -> Unit) {
        onClickCancelListener = object : OnClickCancelListener {
            override fun onClick() {
                listener.invoke()
            }
        }
    }

    interface OnClickConfirmationListener {
        fun onClick(position: Int, value: String)
    }

    interface OnClickCancelListener {
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