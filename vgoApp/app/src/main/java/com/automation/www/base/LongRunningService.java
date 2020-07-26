package com.automation.www.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;
import android.util.Log;
import java.util.*;
import com.automation.www.activity.HomeActivity.AlarmReceiver;

public class LongRunningService extends Service {
    
    @Override
   public void onCreate() {
       //Log.i("DemoLog","TestService -> onCreate, Thread ID:" + Thread.currentThread().getId());
       super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Log.i("DemoLog","TestService -> onBind, Thread ID: " +Thread.currentThread().getId());
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.i("DemoLog","TestService -> onDestroy, Thread ID: " +Thread.currentThread().getId());

        /*
        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
        */
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("DemoLog","TestService -> onStartCommand, startId: " + startId + ",Thread ID: " + Thread.currentThread().getId());
        //return START_STICKY;
        new Thread(new Runnable() { //此处的new Runnable( )并没有实例化了一个接口，切记切记！！！！
            @Override
            public void run() {
            while(true)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"  " + new Date());
                // 打印日志模拟耗时操作。
                Log.e("buffer",  new Date().toString() + " ---");
            }
            }
        }).start();  //重启
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //读者可以修改此处的Minutes从而改变提醒间隔时间
        //此处是设置每隔90分钟启动一次
        //这是90分钟的毫秒数
        int Minutes = 10*1000;
        //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
        //此处设置开启AlarmReceiver这个Service
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
        //manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10*1000+ SystemClock.elapsedRealtime(), 20*1000, pi);//重复
        //使用Toast提示用户(屏幕显示“AlarmSet Finish”)
        //Toast.makeText(this, "AlarmSet Finish", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);

    }


}
