package com.yican.www.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yican.www.base.BaseActivity;

/**
 * Created by Administrator on 2018/1/23.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) { //收到广播 
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, BaseActivity.AdvertisementActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
