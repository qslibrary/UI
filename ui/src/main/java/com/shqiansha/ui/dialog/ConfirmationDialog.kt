package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.shqiansha.ui.R
import com.shqiansha.ui.UIConfig
import com.shqiansha.ui.databinding.DialogConfirmBinding

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

    private var binding: DialogConfirmBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogConfirmBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStyle()
        initView()
    }

    private fun initView() {
        binding?.run {
            tvDialogTitle.text = title
            tvDialogMessage.text = message
            tvDialogConfirm.text = confirmationText
            tvDialogCancel.text = cancelText
            tvDialogConfirm.setOnClickListener {
                onClickConfirmationListener?.onClick(it)
                if (autoDismiss) dismiss()
            }
            tvDialogCancel.setOnClickListener {
                onClickCancelListener?.onClick(it)
                if (autoDismiss) dismiss()
            }
        }
    }

    private fun initStyle() {
        val context = context ?: return
        binding?.run {
            ContextCompat.getDrawable(context, R.drawable.ui_shape_confirm)?.let {
                it.setTint(UIConfig.colorPrimary)
                tvDialogConfirm.background = it
            }
            tvDialogTitle.gravity = titleGravity
            tvDialogMessage.gravity = messageGravity
            if (title.isEmpty()) tvDialogTitle.visibility = View.GONE
            if (message.isEmpty()) tvDialogMessage.visibility = View.GONE
            if (hint.isEmpty()) tvDialogHint.visibility = View.GONE
            if (hideCancel) tvDialogCancel.visibility = View.GONE
        }
    }

    fun show(manager: FragmentManager) {
        show(manager, "ConfirmationDialog")
    }

    fun setOnClickCancelListener(listener: OnClickCancelListener) =
        apply { onClickCancelListener = listener }

    fun setOnClickConfirmationListener(listener: OnClickConfirmationListener) = apply {
        onClickConfirmationListener = listener
    }

    fun interface OnClickConfirmationListener {
        fun onClick(view: View)
    }

    fun interface OnClickCancelListener {
        fun onClick(view: View)
    }
}