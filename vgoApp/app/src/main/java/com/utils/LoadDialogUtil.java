package com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import java.lang.ref.WeakReference;


/**
 * author： guojun
 * date：   2017/12/20
 * desc：   网络请求工具类
 */
public class LoadDialogUtil {

    // 进度条
    private static CutscenesProgress cutscenesProgress = null;
    // activity
    private static WeakReference<Activity> mActivity;

    /**
     * 调起进度
     */
    public static void showDialog(Activity activity) {
        if (activity == null) {
            return;
        }
        mActivity = new WeakReference<>(activity);
        showCutscenes(true, null);
    }

    /**
     * 调起进度
     */
    public static void showDialog(WeakReference<Activity> mAct) {
        if (mAct == null) {
            return;
        }
        mActivity = mAct;
        showCutscenes(true, null);
    }

    /**
     * 调起进度
     */
    public static void showDialog(Activity activity, boolean cancelable) {
        if (activity == null) {
            return;
        }
        mActivity = new WeakReference<>(activity);
        showCutscenes(cancelable, null);
    }


    /**
     * 调起进度
     */
    public static void showDialog(Activity activity, boolean cancelable, DialogInterface.OnCancelListener listener) {
        if (activity == null) {
            return;
        }
        mActivity = new WeakReference<>(activity);
        showCutscenes(cancelable, listener);
    }

    /**
     * 调起进度
     */
    private static void showCutscenes(boolean cancelable, DialogInterface.OnCancelListener listener) {
        try {
            if (mActivity != null
                    && mActivity.get() != null
                    && !mActivity.get().isFinishing()) {
                if (null == cutscenesProgress) {
                    cutscenesProgress = init(mActivity.get(), cancelable, listener);
                    cutscenesProgress.show();
                } else {
                    if (!cutscenesProgress.isShowing()) {
                        cutscenesProgress.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放加载dialog
     */
    public static void dismissDialog() {
        if (mActivity.get() != null && !mActivity.get().isFinishing()) {
            if (cutscenesProgress != null && cutscenesProgress.isShowing()) {
                cutscenesProgress.dismiss();
                cutscenesProgress = null;
            }
        }
    }

    /**
     * @param cancelable 该dialog是否可以cancel
     */
    private static CutscenesProgress init(Context context, boolean cancelable, DialogInterface.OnCancelListener listener) {
        CutscenesProgress dialog = CutscenesProgress.createDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(listener);
        return dialog;
    }

}
