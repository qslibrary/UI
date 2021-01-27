package com.shqiansha.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import com.shqiansha.ui.R
import com.shqiansha.ui.util.SizeUtils
import kotlinx.android.synthetic.main.view_item.view.*

class ItemView : FrameLayout {
    var leftText = ""
    var leftTextSize = 14f
    var leftTextColor: Int = 0


    var rightText = ""
    var rightTextSize = 14f
    var rightTextColor: Int = RIGHT_TEXT_COLOR_DEFAULT

    var rightImage = 0
    var rightImageSize = 0f

    var showRightText = false
    var showRightImage = true
    var showTopLine = false
    var showBottomLine = false
    var linePadding = 0f
    var lineStartPadding = 0f
    var lineEndPadding = 0f



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
        initView()
    }

    private fun initAttr(attr: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.ItemView)
        leftText = typedArray.getString(R.styleable.ItemView_leftText) ?: ""
        leftTextSize = typedArray.getDimension(R.styleable.ItemView_leftTextSize, 14f)
        leftTextColor =
            typedArray.getColor(R.styleable.ItemView_leftTextColor, LEFT_TEXT_COLOR_DEFAULT)

        rightText = typedArray.getString(R.styleable.ItemView_rightText) ?: ""
        rightTextSize = typedArray.getDimension(R.styleable.ItemView_rightTextSize, 14f)
        rightTextColor =
            typedArray.getColor(R.styleable.ItemView_rightTextColor, RIGHT_TEXT_COLOR_DEFAULT)

        rightImage =
            typedArray.getResourceId(R.styleable.ItemView_rightImage, R.drawable.ic_arrow_right_12)
        rightImageSize = typedArray.getDimension(R.styleable.ItemView_rightImageSize, 12f)

        showRightText = typedArray.getBoolean(R.styleable.ItemView_showRightText, true)
        showRightImage = typedArray.getBoolean(R.styleable.ItemView_showRightImage, true)
        showTopLine = typedArray.getBoolean(R.styleable.ItemView_showTopLine, false)
        showBottomLine = typedArray.getBoolean(R.styleable.ItemView_showBottomLine, false)

        linePadding = typedArray.getDimension(R.styleable.ItemView_linePadding, 0f)
        lineStartPadding = typedArray.getDimension(R.styleable.ItemView_lineStartPadding, 0f)
        lineEndPadding = typedArray.getDimension(R.styleable.ItemView_lineEndPadding, 0f)
        typedArray.recycle()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_item, this, false)
        val tvLeft = view.findViewById<TextView>(R.id.tvItemLeft)
        tvLeft.text = leftText
        tvLeft.textSize = leftTextSize
        tvLeft.setTextColor(leftTextColor)
        val tvRight = view.findViewById<TextView>(R.id.tvItemRight)
        if (!showRightText) rightText = ""
        tvRight.text = rightText
        tvRight.textSize = rightTextSize
        tvRight.setTextColor(rightTextColor)

        val ivRight = view.findViewById<ImageView>(R.id.ivItemRight)

        if (showRightImage) {
            ivRight.visibility = View.VISIBLE
            ivRight.setImageResource(rightImage)
            val size = SizeUtils.dp2px(context, rightImageSize)
            ivRight.maxWidth = size
            ivRight.maxHeight = size
        } else {
            ivRight.visibility = View.GONE
        }

        val vTop=view.findViewById<View>(R.id.vTop)
        val vBottom=view.findViewById<View>(R.id.vBottom)

        vTop.visibility=if(showTopLine) View.VISIBLE else View.GONE
        vBottom.visibility=if(showBottomLine) View.VISIBLE else View.GONE

        if(lineStartPadding==0f)lineStartPadding=linePadding
        if(lineEndPadding==0f) lineEndPadding=linePadding
        val topLineParams= vTop.layoutParams as LinearLayout.LayoutParams
        topLineParams.marginStart=lineStartPadding.toInt()
        topLineParams.marginEnd=lineEndPadding.toInt()

        val bottomLineParams= vBottom.layoutParams as LinearLayout.LayoutParams
        bottomLineParams.marginStart=lineStartPadding.toInt()
        bottomLineParams.marginEnd=lineEndPadding.toInt()
        addView(view)
    }

    fun getLeftTextView():TextView{
        return tvItemLeft
    }

    fun getRightTextView():TextView{
        return tvItemRight
    }
}