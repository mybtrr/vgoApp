package com.automation.www.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.automation.www.R;
import com.automation.www.base.BaseActivity;
import com.automation.www.util.HttpConnect;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.utils.FalconException;
import com.utils.InstructionsTool;
import com.utils.SerialPortUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;

//解决Error:null value in entry: incrementalFolder=null
//将project最外层的.gradle删除（应该是红色的那个）。重新编译一下就可以了。

//mTvPaymentMethodReturn 付款方式页中的返回键
//mTvPaymentScavengingReturn 扫描页中的返回键
//mTvPaymentCompletionDetermine 付款完成页中的返回键

/**
 * 扫描支付
 * Created by Administrator on 2018/1/13.
 */

public class PaymentScavengingActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvPaymentScavengingReturn;
    private ImageView mIvRechargeCode;
    private TitleFragment mTitleFragment;
    private SerialPortUtils serialPortUtils = new SerialPortUtils();
    private SerialPort serialPort;
    private final Timer timer = new Timer();
    private boolean isPay;
    private short count;  //16位
    private TimerTask task = new TimerTask() { //定时时间在后面的程序里设定，如 “timer.schedule(task, 0, 1000);”
        @Override
        public void run() {
            ++count;
            if(count == 10) {
                handler.sendEmptyMessage(10);
            }
            if(count == 40) {
                handler.sendEmptyMessage(11);
            }
			if(count > 50) {
                handler.sendEmptyMessage(12);
			}
            else {
                handler.sendEmptyMessage(0); //每隔1秒，定时发送 handler 消息
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:      //定时发送消息的指令，在这里接收  
                    getRechargeResult();//查询支付是否成功 
                    break;
                case 1:      //串口不通处理
                    Toast.makeText(PaymentScavengingActivity.this, "机器故障", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    Toast.makeText(PaymentScavengingActivity .this, "请扫码", Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    Toast.makeText(PaymentScavengingActivity .this, "请尽快扫码", Toast.LENGTH_SHORT).show();
                    break;
                case 12:
                    Toast.makeText(PaymentScavengingActivity .this, "超时了！！", Toast.LENGTH_SHORT).show();
                    timer.cancel();
					//finish(); //返回前一页
					startActivity(new Intent(PaymentScavengingActivity.this, AdvertisementActivity.class)); //20190830
                    break;
            }
        }
    };

    @Override
    public void initUI() {
        setContentView(R.layout.activity_payment_scavenging);
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentScavenging, mTitleFragment)
                .commit();
        findid();
        openSerial();//下载到手机时注释掉这一行。20190725	
		//orderPay();//下载到手机时增加这一行。不开串口直接获取二维码 20190725
        send(InstructionsTool.TRY_CONNECTION);//下载到手机时注释掉这一行。20190725
    }

    private void openSerial() {
        serialPort = serialPortUtils.openSerialPort("/dev/ttyS3");
        if (serialPort == null) {
            Toast.makeText(this, "串口打开失败", Toast.LENGTH_SHORT).show();
            return;
        }

        //串口数据监听事件
        serialPortUtils.setOnDataReceiveListener(new SerialPortUtils.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                //在线程中直接操作UI会报异常：ViewRootImpl$CalledFromWrongThreadException
                //解决方法：handler
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(buffer[buffer.length - 1] + "");
                Log.e("buffer", buffer[buffer.length - 1] + "");
                if (!isPay) {
                    isPay = true;
                    if (stringBuffer.toString().equals("00")) {
                        handler.sendEmptyMessage(1); //这里使用了handler方法传递信息（串口未接通）
                    } else {
                        //orderPay();   //不通过支付直接付货：可用于测试机器。20190818-OK！**************************************
						//timer.cancel();  //清除任务队列中的所有任务
						startCompletionAct();  //直接跳转页面
                   }
                }
            }
        });
    }

    private void send(byte[] a){
	//try...catch 可以测试代码中的错误。try 部分包含需要运行的代码，而 catch 部分包含错误发生时运行的代码。
        try {
            serialPortUtils.doActionSend(a);
        } catch (FalconException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findid() {
        mTvPaymentScavengingReturn = (TextView) findViewById(R.id.mTvPaymentScavengingReturn); //返回键
        mIvRechargeCode = (ImageView) findViewById(R.id.mIvRechargeCode); //二维码
    }

    @Override
    public void onclick() {
        mIvRechargeCode.setOnClickListener(this);
        mTvPaymentScavengingReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvPaymentScavengingReturn: //按返回健回到上一页
                finish();
                break;
            case R.id.mIvRechargeCode:
//                startCompletionAct();  //跳转页面 //20190818
                break;
        }
    }

    /**
     * 订单支付接口
     */
    public void orderPay() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("type", getIntent().getStringExtra("paytype"));
       // Log.e("map", map + "");
        String url = HttpConnect.URL + "order_pay";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        Log.e("object", object + "");
                        super.callback(url, object, status);
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                //加载二维码
                                Glide.with(PaymentScavengingActivity.this).load(object.optString("data")).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mIvRechargeCode);
                                /*
                                (1)Timer.schedule(TimerTask task,Date time)安排在制定的时间执行指定的任务。
                                (2)Timer.schedule(TimerTask task,Date firstTime ,long period)安排指定的任务在指定的时间开始进行重复的固定延迟执行．
                                (3)Timer.schedule(TimerTask task,long delay)安排在指定延迟后执行指定的任务．
                                (4)Timer.schedule(TimerTask task,long delay,long period)安排指定的任务从指定的延迟后开始进行重复的固定延迟执行．
                                (5)Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)安排指定的任务在指定的时间开始进行重复的固定速率执行．
                                (6)Timer.scheduleAtFixedRate(TimerTask task,long delay,long period)安排指定的任务在指定的延迟后开始进行重复的固定速率执行．
                                 */
                                timer.schedule(task, 0, 1500); //原来是5000  20190801
                            }else {
                                Toast.makeText(PaymentScavengingActivity.this, "网络故障,请稍后再试。", Toast.LENGTH_SHORT).show();
                                timer.cancel();
                                finish(); 
                            }
                        } else {
                            Toast.makeText(PaymentScavengingActivity.this, "网络故障,请稍后再试。", Toast.LENGTH_SHORT).show();
                            timer.cancel();
                            finish(); 
                        }
                    }
                }

        );
		
		
    }
    //查询支付是否成功
    private void getRechargeResult() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("type", getIntent().getStringExtra("PayMode"));
        String url = HttpConnect.URL + "pay_results";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
						Log.e("aaaaaaaaaaaaaaaaaaaaa", object + ""); //在消息查看中显示成功或者是失败
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                timer.cancel();  //清除任务队列中的所有任务
                                startCompletionAct();  //去跳转页面
                            }
                        }
                    }
                }
        );
    }

    private void startCompletionAct() {
        serialPortUtils.closeSerialPort();
		//跳转页面
        startActivity(new Intent(PaymentScavengingActivity.this, PaymentCompletionActivity.class)
                .putExtra("payresult", "1")
                .putExtra("shopList", getIntent().getSerializableExtra("shopList")));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortUtils.closeSerialPort();
    }

            /*
                getLooper().quit();来退出这个线程，其实原理很简单，就是改变在消息循环里面标志位，退出整个while循环，使线程执行完毕。
            */
}
