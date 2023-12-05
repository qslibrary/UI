package com.shqiansha.ui.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.shqiansha.ui.R

class EditView : AppCompatEditText {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        initAttr(attr)
    }

    private fun initAttr(attr: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.EditView)
        val borderSize = typedArray.getDimension(R.styleable.EditView_borderSize, 0f).toInt()
        val radius = typedArray.getDimension(R.styleable.EditView_radius, 0f)
        val tlRadius = typedArray.getDimension(R.styleable.EditView_radius_topLeft, radius)
        val trRadius = typedArray.getDimension(R.styleable.EditView_radius_topRight, radius)
        val blRadius = typedArray.getDimension(R.styleable.EditView_radius_bottomLeft, radius)
        val brRadius = typedArray.getDimension(R.styleable.EditView_radius_bottomRight, radius)
        val backgroundColor = typedArray.getColor(R.styleable.EditView_backgroundColor, 0)
        val borderColor = typedArray.getColor(R.styleable.EditView_borderColor, 0)
        val backgroundShape = typedArray.getInt(R.styleable.EditView_backgroundShape, 0)

        val r = floatArrayOf(
            tlRadius, tlRadius, trRadius, trRadius,
            brRadius, brRadius, blRadius, blRadius
        )
        genBackground(r, backgroundColor, borderSize, borderColor, backgroundShape)
        typedArray.recycle()
    }

    private fun genBackground(
        radius: FloatArray,
        color: Int,
        borderSize: Int,
        borderColor: Int,
        shape: Int
    ) {
        val drawable = GradientDrawable()
        if (color != 0) drawable.setColor(color)
        drawable.shape = shape
        if (borderColor != 0) drawable.setStroke(borderSize, borderColor)
        drawable.cornerRadii = radius
        background = drawable
    }
}