package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shqiansha.ui.R

open class BaseBottomDialog : BottomSheetDialogFragment() {
    private var dim = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            if (dim) R.style.BaseDialog else R.style.BaseDialogNotDim
        )
    }

    override fun onStart() {
        super.onStart()
        view?.let {
            it.post { moveToTop() }
        }
    }

    /**
     * disable dim style
     * note:use before onCreate
     */
    fun disableDim() {
        dim = false
    }

    /**
     * scroll to max height if over the default height
     */
    private fun moveToTop() {
        view?.let {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(it.parent as View)
            behavior.peekHeight = it.measuredHeight
        }
    }
}