package com.yican.www.activity;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.utils.FalconException;
import com.utils.InstructionsTool;
import com.utils.SerialPortUtils;
import com.yican.www.R;
import com.yican.www.base.BaseActivity;
import com.yican.www.util.BaiduTtsUtil;
import com.yican.www.util.HttpConnect;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;


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
    //private final Timer timer = new Timer();
    private boolean isPay;

    private final Timer timer1 = new Timer();
    private final Timer timer2 = new Timer();
    private final Timer timer3 = new Timer();

    private short count;  //16位
    private TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            ++count;
            if(count == 10) {
                handler.sendEmptyMessage(1);
            }

            if(count == 20) {
                handler.sendEmptyMessage(2);
            }

            if(count >= 30) {
                handler.sendEmptyMessage(3);
            }
            //BaiduTtsUtil.getInstance().speak("计时工作1+" + count);
        }
    };
    private TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            ++count;
            if(count >= 20) {
                handler.sendEmptyMessage(11);
            }
            //BaiduTtsUtil.getInstance().speak("计时工作2+" + count);
        }
    };
    private TimerTask task3 = new TimerTask() {
        @Override
        public void run() {
            ++count;
            if(count >= 20) {
                handler.sendEmptyMessage(12);
            }
            //BaiduTtsUtil.getInstance().speak("计时工作3+" + count);
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getRechargeResult();
                    break;
                case 10:
                    Toast.makeText(PaymentScavengingActivity.this, "机器故障", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    BaiduTtsUtil.getInstance().speak("请扫码");
                    Toast.makeText(PaymentScavengingActivity .this, "请扫码", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    BaiduTtsUtil.getInstance().speak("请尽快扫码");
                    Toast.makeText(PaymentScavengingActivity .this, "请尽快扫码", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    BaiduTtsUtil.getInstance().speak("超时了");
                    Toast.makeText(PaymentScavengingActivity .this, "超时了！！", Toast.LENGTH_SHORT).show();
                    timer1.cancel();
					//finish(); //返回前一页
					startActivity(new Intent(PaymentScavengingActivity.this, AdvertisementActivity.class)); //20190830
                    break;
                case 11:
                    BaiduTtsUtil.getInstance().speak("通信故障");
                    timer2.cancel();
                    Toast.makeText(PaymentScavengingActivity .this, "通信故障", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentScavengingActivity.this, AdvertisementActivity.class)); //20200104
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
        timer1.schedule(task1, 0, 1500);
        //openSerial();//下载到手机时注释掉这一行。20190725
        orderPay();//下载到手机时增加这一行。不开串口直接获取二维码 20190725
        //send(InstructionsTool.TRY_CONNECTION);//下载到手机时注释掉这一行。20190725

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
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(buffer[buffer.length - 1] + "");
                Log.e("buffer", buffer[buffer.length - 1] + "");
                if (!isPay) {
                    isPay = true;
                    if (stringBuffer.toString().equals("00")) {
                        handler.sendEmptyMessage(10);
                    } else {
                        orderPay();  
                   }
                }
            }
        });
    }

    private void send(byte[] a){
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
            case R.id.mTvPaymentScavengingReturn:
                timer1.cancel();
                finish();
                break;
            case R.id.mIvRechargeCode:
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
                                Glide.with(PaymentScavengingActivity.this).load(object.optString("data")).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mIvRechargeCode);
                    
                                timer2.schedule(task2, 0, 1500);
                            }else {
                                Toast.makeText(PaymentScavengingActivity.this, "网络故障,请稍后再试。", Toast.LENGTH_SHORT).show();
                                timer1.cancel();
                                finish();
                            }
                        } else {
                            Toast.makeText(PaymentScavengingActivity.this, "网络故障,请稍后再试。", Toast.LENGTH_SHORT).show();
                            timer1.cancel();
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
						Log.e("aaaaaaaaaaaaaaaaaaaaa", object + ""); 
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                timer1.cancel();
                                startCompletionAct(); 
                            }
                        }
                    }
                }
        );
    }

    private void startCompletionAct() {
        serialPortUtils.closeSerialPort();
        startActivity(new Intent(PaymentScavengingActivity.this, PaymentCompletionActivity.class)
                .putExtra("payresult", "1")
                .putExtra("shopList", getIntent().getSerializableExtra("shopList")));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortUtils.closeSerialPort();
    }

}
