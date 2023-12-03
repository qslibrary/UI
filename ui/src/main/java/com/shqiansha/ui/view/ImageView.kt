package com.shqiansha.ui.view

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.shqiansha.ui.R

class ImageView : ShapeableImageView {
    //上左、上右、下右、下左
    private var radii = floatArrayOf(0f, 0f, 0f, 0f)

    //左、上、右、下
    private var padding = intArrayOf(0, 0, 0, 0)
    private var url: String? = null

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
        val a = context.obtainStyledAttributes(attr, R.styleable.ImageView)
        url = a.getString(R.styleable.ImageView_url)

        val radius = a.getDimension(R.styleable.ImageView_radius, 0f)
        radii[0] = a.getDimension(R.styleable.ImageView_radius_topLeft, radius)
        radii[1] = a.getDimension(R.styleable.ImageView_radius_topRight, radius)
        radii[2] = a.getDimension(R.styleable.ImageView_radius_bottomRight, radius)
        radii[3] = a.getDimension(R.styleable.ImageView_radius_bottomLeft, radius)
        strokeWidth = a.getDimension(R.styleable.ImageView_borderSize, 0f)
        strokeColor = a.getColorStateList(R.styleable.ImageView_borderColor)
        padding[0] = paddingLeft
        padding[1] = paddingTop
        padding[2] = paddingRight
        padding[3] = paddingBottom
        load()
        a.recycle()
    }

    private fun load() {
        url?.let { Glide.with(this).load(it).centerCrop().into(this) }
        val shapeBuilder = ShapeAppearanceModel.builder()
        shapeBuilder.setTopLeftCorner(CornerFamily.ROUNDED, radii[0])
        shapeBuilder.setTopRightCorner(CornerFamily.ROUNDED, radii[1])
        shapeBuilder.setBottomRightCorner(CornerFamily.ROUNDED, radii[2])
        shapeBuilder.setBottomLeftCorner(CornerFamily.ROUNDED, radii[3])
        shapeAppearanceModel = shapeBuilder.build()
        val borderSize = (strokeWidth / 2).toInt()
        setPadding(
            padding[0] + borderSize, padding[1] + borderSize,
            padding[2] + borderSize, padding[3] + borderSize
        )
    }
}