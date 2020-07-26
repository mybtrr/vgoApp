package com.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.automation.www.R;


/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/19 16:04
 * <p/>
 * Description: 网络请求等待动画
 */
public class CutscenesProgress extends Dialog {

    private CutscenesProgress(Context context, int theme) {
        super(context, theme);
    }

    public static CutscenesProgress createDialog(Context context) {
        CutscenesProgress cutscenesProgress = new CutscenesProgress(context, R.style.CustomDialog);
        cutscenesProgress.setContentView(R.layout.layout_loading);
        cutscenesProgress.setCanceledOnTouchOutside(false);
        return cutscenesProgress;
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
    }
}
