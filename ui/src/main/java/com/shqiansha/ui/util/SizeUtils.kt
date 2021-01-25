package com.shqiansha.ui.util

import android.content.Context

class SizeUtils {
    companion object{
        fun dp2px(context: Context, dpValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}