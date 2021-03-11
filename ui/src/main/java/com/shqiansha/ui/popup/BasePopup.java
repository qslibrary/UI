package com.shqiansha.ui.popup;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class BasePopup extends PopupWindow {
    private Context context;
    private View darkView;
    private View bindView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams p;
    private DisplayMetrics point = new DisplayMetrics();
    private OnDismissListener onDismissListener;

    /**
     * 扩展蒙层背景
     * 如果为true，蒙层会扩展到当前popup
     */
    private boolean extendDarkHeight = false;
    /**
     * 是否显示蒙层背景
     */
    private boolean autoShowDarkBackground = true;
    private int backgroundColor = Color.parseColor("#a0000000");

    public BasePopup(View view, View bindView) {
        if (view != null && bindView != null) {
            this.bindView = bindView;
            this.context = view.getContext();
            this.initWindowManager(this.context);
            this.initView(view);
        }
    }

    public BasePopup(int resId, View bindView) {
        if (bindView != null) {
            this.bindView = bindView;
            this.context = bindView.getContext();
            this.initWindowManager(this.context);
            this.initView(LayoutInflater.from(this.context).inflate(resId, (ViewGroup) bindView.getParent(), false));
        }
    }

    private void initView(View view) {
        this.setContentView(view);
        this.setHeight(-2);
        this.setWidth(-1);
        this.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (autoShowDarkBackground) hideDarkView();
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }

            }
        });
        this.setOutsideTouchable(true);
    }

    private void initDarkView() {
        this.darkView = new View(this.context);
        this.darkView.setBackgroundColor(this.backgroundColor);
        this.initDarkViewParams();
    }

    private void initDarkViewParams() {
        int contentHeight = 0;
        if (!this.extendDarkHeight) {
            this.getContentView().measure(0, 0);
            contentHeight = this.getContentView().getMeasuredHeight();
        }

        int[] location = new int[2];
        this.bindView.getLocationOnScreen(location);
        this.p = new WindowManager.LayoutParams();
        this.p.width = -1;
        this.p.height = this.point.heightPixels - location[1] - this.bindView.getMeasuredHeight() - contentHeight;
        this.p.format = -3;
        this.p.type = 1999;
        this.p.softInputMode = 1;
        this.p.verticalMargin = 1.0F;
    }

    private void initWindowManager(Context context) {
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (this.windowManager != null) {
            this.windowManager.getDefaultDisplay().getRealMetrics(this.point);
        }

    }

    private void hideDarkView() {
        if (this.darkView != null) {
            this.windowManager.removeViewImmediate(this.darkView);
        }

    }

    private void showDarkView() {
        if (this.darkView == null) {
            this.initDarkView();
        }

        this.windowManager.addView(this.darkView, this.p);
    }

    public void show() {
        this.showAsDropDown(this.bindView);
        if (autoShowDarkBackground) this.showDarkView();
    }

    public void setExtendDarkHeight(boolean extendDarkHeight) {
        this.extendDarkHeight = extendDarkHeight;
        if (this.p != null) {
            this.initDarkViewParams();
        }

    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if (this.darkView != null) {
            this.darkView.setBackgroundColor(backgroundColor);
        }

    }

    public void setAutoShowDarkBackground(boolean autoShowDarkBackground) {
        this.autoShowDarkBackground = autoShowDarkBackground;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
