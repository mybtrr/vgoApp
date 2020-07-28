package com.yican.www.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.FalconException;
import com.utils.SerialPortUtils;
import com.yican.www.R;
import com.yican.www.base.BaseActivity;
import com.yican.www.bean.PaymentMethodJavaBean;
import com.yican.www.util.BaiduTtsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;

/**
 * 付款完成
 * Created by Administrator on 2018/1/16.
 */

public class PaymentCompletionActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvPaymentCompletionResult;
    private FrameLayout mFlPaymentCompletionMethod;
    private TextView mTvPaymentCompletionResult, mTvPaymentCompletionPrompt, mTvPaymentCompletionDetermine;
    private TitleFragment mTitleFragment;
    private SerialPortUtils serialPortUtils = new SerialPortUtils();
    private SerialPort serialPort;
    private ArrayList<PaymentMethodJavaBean> shopList;
    private int i = 0;
    private final Timer timer = new Timer();
    private int add;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (i > shopList.size() - 1) {
                serialPortUtils.closeSerialPort();
                timer.cancel();
                startActivity(new Intent(PaymentCompletionActivity.this, AdvertisementActivity.class));
                finish();
            } else {
				
                for (int j = 0; j < shopList.size(); j++) {
                    if (j == i) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = shopList.get(j).getData();
                        handler.sendMessage(message);
                        i++;
                        break;
                    }
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj instanceof byte[]) {
                        send((byte[]) msg.obj);
                    }
                    break;
                case 1:
                    Toast.makeText(PaymentCompletionActivity.this, "机器故障，您可以申请退款", Toast.LENGTH_SHORT).show();
                    serialPortUtils.closeSerialPort();
                    timer.cancel();
                    startActivity(new Intent(PaymentCompletionActivity.this, AdvertisementActivity.class));
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void openSerial() {
        shopList = (ArrayList<PaymentMethodJavaBean>) getIntent().getSerializableExtra("shopList");
        serialPort = serialPortUtils.openSerialPort("/dev/ttyS3");
        if (serialPort == null) {
            Toast.makeText(this, "串口打开失败", Toast.LENGTH_SHORT).show();
            return;
        }
        serialPortUtils.setOnDataReceiveListener(new SerialPortUtils.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                if (add < 3) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                add++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if (Arrays.asList(buffer).contains(03)) {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void send(byte[] a) {
        try {
            serialPortUtils.doActionSend(a);
        } catch (FalconException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initUI() {
        setContentView(R.layout.activity_payment_completion);
        //openSerial(); //打开串口//下载到手机时注释掉这一行。20190725
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentCompletionMethod, mTitleFragment)
                .commit();
        if (getIntent().getStringExtra("payresult").equals("1")) {
            BaiduTtsUtil.getInstance().speak("支支付完成!请留意出货口");
            settext("支付完成!", "* 请留意出货口");
            mIvPaymentCompletionResult.setImageResource(R.mipmap.icon_wancheng);
        } else {
            settext("支付失败!", "* 您的支付失败了");
            mIvPaymentCompletionResult.setImageResource(R.mipmap.icon_shibai);
        }
        if (shopList.size() > 0) {
            try {
                timer.schedule(task, 500, 8000); 
            } catch (Exception e) {
                Toast.makeText(this, "启动异常", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "商品列表为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void findid() {
        mIvPaymentCompletionResult = (ImageView) findViewById(R.id.mIvPaymentCompletionResult);
        mFlPaymentCompletionMethod = (FrameLayout) findViewById(R.id.mFlPaymentCompletionMethod);
        mTvPaymentCompletionResult = (TextView) findViewById(R.id.mTvPaymentCompletionResult);
        mTvPaymentCompletionPrompt = (TextView) findViewById(R.id.mTvPaymentCompletionPrompt);
        mTvPaymentCompletionDetermine = (TextView) findViewById(R.id.mTvPaymentCompletionDetermine);
    }

    private void settext(String Result, String Prompt) {
        mTvPaymentCompletionResult.setText(Result);
        SpannableString spanString = new SpannableString(Prompt);
        ForegroundColorSpan mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(PaymentCompletionActivity.this, R.color.colorRedda0517));
        spanString.setSpan(mForegroundColorSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvPaymentCompletionPrompt.setText(spanString);
    }


    @Override
    public void onclick() {
        mTvPaymentCompletionDetermine.setOnClickListener(this); 
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvPaymentCompletionDetermine:
                finish(); 
                break;
        }

    }

	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortUtils.closeSerialPort();
    }
	
}
