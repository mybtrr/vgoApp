package com.automation.www.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.utils.FalconException;
import com.utils.InstructionsTool;
import com.utils.SerialPortUtils;

import android_serialport_api.SerialPortFinder;
import android_serialport_api.SerialPort;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * 开卡
 * Created by Administrator on 2018/1/13.
 */

public class OpenCardActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OpenCardActivity";
    private TextView mTvRechargePrompt, mTvRechargeReturn;
    private ImageView mIvRechargeCode;
    private TitleFragment mTitleFragment;
    private SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPortUtils serialPortUtils = new SerialPortUtils();
    private SerialPort serialPort;
    private byte[] mBuffer;
    private Handler handler;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_recharge);
        handler = new Handler();
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentMethod, mTitleFragment)
                .commit();
    }

    private void findid() {
        mTvRechargePrompt = (TextView) findViewById(R.id.mTvRechargePrompt);
        mTvRechargeReturn = (TextView) findViewById(R.id.mTvRechargeReturn);
        mIvRechargeCode = (ImageView) findViewById(R.id.mIvRechargeCode);
        SpannableString spanString = new SpannableString("* 请留意出货口");		
        ForegroundColorSpan mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(OpenCardActivity.this, R.color.colorRedda0517));
        spanString.setSpan(mForegroundColorSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvRechargePrompt.setText(spanString);
        openSerial(); //下载到手机时注释掉这一行。20190725
    }

    private void openSerial(){
        serialPort = serialPortUtils.openSerialPort("/dev/ttyS3");
        if (serialPort == null) {
            Log.e(TAG, "串口打开失败");
            Toast.makeText(OpenCardActivity.this, "串口打开失败", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(OpenCardActivity.this, "串口已打开", Toast.LENGTH_SHORT).show();
        //串口数据监听事件
        serialPortUtils.setOnDataReceiveListener(new SerialPortUtils.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d(TAG, "进入数据监听事件中。。。" + new String(buffer));
                //在线程中直接操作UI会报异常：ViewRootImpl$CalledFromWrongThreadException
                //解决方法：handler
                //
                mBuffer = buffer;
                handler.post(runnable);
            }

            //开线程更新UI
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG,"size：" + String.valueOf(mBuffer.length) + "数据监听：" + new String(mBuffer));
                }
            };
        });
    }

    @Override
    public void onclick() {
        mTvRechargeReturn.setOnClickListener(this);
        mIvRechargeCode.setOnClickListener(this);
        mTvRechargePrompt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvRechargeReturn:
		        finish();
                break;
            case R.id.mIvRechargeCode:
                startActivity(new Intent(OpenCardActivity.this, OpenCardResultActivity.class));
                getdata();
                break;
            case R.id.mTvRechargePrompt:
                send(InstructionsTool.RED);
                break;
        }
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

    public void getdata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("type", getIntent().getStringExtra("PayMode"));
        Log.e("object", map + "");
        String url = HttpConnect.URL + "buy_card";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                startActivity(new Intent(OpenCardActivity.this, OpenCardResultActivity.class).putExtra("PayResult", "1"));
                            } else {
                                startActivity(new Intent(OpenCardActivity.this, OpenCardResultActivity.class).putExtra("PayResult", "0"));
                            }
                        }
                    }
                }

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortUtils.closeSerialPort();
    }
}
