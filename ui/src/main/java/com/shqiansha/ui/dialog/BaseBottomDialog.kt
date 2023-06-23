package com.shqiansha.ui.dialog

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shqiansha.ui.R

open class BaseBottomDialog : BottomSheetDialogFragment() {
    private var dim = true
    private var scrollable=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NO_TITLE,
            if (dim) R.style.BaseDialog else R.style.BaseDialogNotDim
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.AnimBottomDialog
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
     * disable scroll
     * note:use before onStart
     */
    fun disableScroll(){
        scrollable=false
    }

    /**
     * scroll to max height if over the default height
     */
    private fun moveToTop() {
        view?.let {
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(it.parent as View)
            behavior.peekHeight = it.measuredHeight
            if(!scrollable) behavior.isHideable=false
        }
    }
}