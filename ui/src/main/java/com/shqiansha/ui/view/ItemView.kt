package com.shqiansha.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.shqiansha.ui.R
import com.shqiansha.ui.util.SizeUtils

class ItemView : FrameLayout {

    lateinit var tvLeft: TextView
    lateinit var tvRight: TextView
    lateinit var ivRight: ImageView

    companion object {
        private val LEFT_TEXT_COLOR_DEFAULT = Color.parseColor("#333333")
        private val RIGHT_TEXT_COLOR_DEFAULT = Color.parseColor("#999999")
    }

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
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.ItemView)
        val leftText = typedArray.getString(R.styleable.ItemView_leftText) ?: ""
        val leftTextSize = typedArray.getDimension(R.styleable.ItemView_leftTextSize, 14f)
        val leftTextColor =
            typedArray.getColor(R.styleable.ItemView_leftTextColor, LEFT_TEXT_COLOR_DEFAULT)

        var rightText = typedArray.getString(R.styleable.ItemView_rightText) ?: ""
        val rightTextSize = typedArray.getDimension(R.styleable.ItemView_rightTextSize, 14f)
        val rightTextColor =
            typedArray.getColor(R.styleable.ItemView_rightTextColor, RIGHT_TEXT_COLOR_DEFAULT)

        val rightImage =
            typedArray.getResourceId(R.styleable.ItemView_rightImage, R.drawable.ic_arrow_right_12)
        val rightImageSize = typedArray.getDimension(R.styleable.ItemView_rightImageSize, 12f)

        val showRightText = typedArray.getBoolean(R.styleable.ItemView_showRightText, true)
        val showRightImage = typedArray.getBoolean(R.styleable.ItemView_showRightImage, true)
        val showTopLine = typedArray.getBoolean(R.styleable.ItemView_showTopLine, false)
        val showBottomLine = typedArray.getBoolean(R.styleable.ItemView_showBottomLine, false)

        val linePadding = typedArray.getDimension(R.styleable.ItemView_linePadding, 0f)
        var lineStartPadding = typedArray.getDimension(R.styleable.ItemView_lineStartPadding, 0f)
        var lineEndPadding = typedArray.getDimension(R.styleable.ItemView_lineEndPadding, 0f)
        typedArray.recycle()


        val view = LayoutInflater.from(context).inflate(R.layout.view_item, this, false)
        tvLeft = view.findViewById(R.id.tvItemLeft)
        tvLeft.text = leftText
        tvLeft.textSize = leftTextSize
        tvLeft.setTextColor(leftTextColor)
        tvRight = view.findViewById(R.id.tvItemRight)
        if (!showRightText) rightText = ""
        tvRight.text = rightText
        tvRight.textSize = rightTextSize
        tvRight.setTextColor(rightTextColor)

        ivRight = view.findViewById(R.id.ivItemRight)

        if (showRightImage) {
            ivRight.visibility = View.VISIBLE
            ivRight.setImageResource(rightImage)
            val size = SizeUtils.dp2px(context, rightImageSize)
            ivRight.maxWidth = size
            ivRight.maxHeight = size
        } else {
            ivRight.visibility = View.GONE
        }

        val vTop = view.findViewById<View>(R.id.vTop)
        val vBottom = view.findViewById<View>(R.id.vBottom)

        vTop.visibility = if (showTopLine) View.VISIBLE else View.GONE
        vBottom.visibility = if (showBottomLine) View.VISIBLE else View.GONE

        if (lineStartPadding == 0f) lineStartPadding = linePadding
        if (lineEndPadding == 0f) lineEndPadding = linePadding
        val topLineParams = vTop.layoutParams as LinearLayout.LayoutParams
        topLineParams.marginStart = lineStartPadding.toInt()
        topLineParams.marginEnd = lineEndPadding.toInt()

        val bottomLineParams = vBottom.layoutParams as LinearLayout.LayoutParams
        bottomLineParams.marginStart = lineStartPadding.toInt()
        bottomLineParams.marginEnd = lineEndPadding.toInt()
        addView(view)

        val backgroundValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            backgroundValue,
            true
        )
        foreground = AppCompatResources.getDrawable(context, backgroundValue.resourceId)
    }
}