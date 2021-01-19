package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.shqiansha.ui.R
import com.shqiansha.ui.UIConfig
import kotlinx.android.synthetic.main.dialog_confirm.*

class ConfirmationDialog : DialogFragment() {
    var title: CharSequence = ""
    var message: CharSequence = ""
    var hint: CharSequence = ""
    var confirmationText: CharSequence = "确定"
    var cancelText: CharSequence = "取消"
    var messageGravity = Gravity.CENTER
    var titleGravity = Gravity.CENTER
    var hideCancel = false
    var autoDismiss = true

    private var onClickConfirmationListener: OnClickConfirmationListener? = null
    private var onClickCancelListener: OnClickCancelListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.BaseDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_confirm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initStyle()
        initView()
    }

    private fun initView() {
        tvDialogTitle.text = title
        tvDialogMessage.text = message
        tvDialogConfirm.text = confirmationText
        tvDialogCancel.text = cancelText
        tvDialogConfirm.setOnClickListener {
            onClickConfirmationListener?.onClick(it)
            if(autoDismiss) dismiss()
        }
        tvDialogCancel.setOnClickListener {
            onClickCancelListener?.onClick(it)
            if(autoDismiss) dismiss()
        }
    }

    private fun initStyle() {
        tvDialogConfirm.setBackgroundResource(UIConfig.dialog_confirm_background)
        tvDialogCancel.setBackgroundResource(UIConfig.dialog_cancel_background)
        tvDialogTitle.gravity = titleGravity
        tvDialogMessage.gravity = messageGravity
        if (title.isEmpty()) tvDialogTitle.visibility = View.GONE
        if (message.isEmpty()) tvDialogMessage.visibility = View.GONE
        if (hint.isEmpty()) tvDialogHint.visibility = View.GONE
        if (hideCancel) tvDialogCancel.visibility = View.GONE
    }

    fun show(manager: FragmentManager){
        show(manager,"ConfirmationDialog")
    }

    fun setOnClickConfirmationListener(listener: (view: View) -> Unit) {
        onClickConfirmationListener = object : OnClickConfirmationListener {
            override fun onClick(view: View) {
                listener.invoke(view)
            }
        }
    }

    fun setOnClickCancelListener(listener: (view: View) -> Unit){
        onClickCancelListener = object : OnClickCancelListener {
            override fun onClick(view: View) {
                listener.invoke(view)
            }
        }
    }

    interface OnClickConfirmationListener {
        fun onClick(view: View)
    }

    interface OnClickCancelListener {
        fun onClick(view: View)
    }
}