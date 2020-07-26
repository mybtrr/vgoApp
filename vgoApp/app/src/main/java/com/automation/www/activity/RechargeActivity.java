package com.automation.www.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.automation.www.R;
import com.automation.www.base.BaseActivity;
import com.automation.www.util.HttpConnect;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 充值
 * Created by Administrator on 2018/1/13.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvRechargePrompt, mTvRechargeReturn;
    private ImageView mIvRechargeCode;
    private TitleFragment mTitleFragment;
    private final Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 发送指令
            switch (msg.what) {
                case 0:
                    getRechargeResult();//查询支付是否成功
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void initUI() {
        setContentView(R.layout.activity_recharge);
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
        SpannableString spanString = new SpannableString("* 请先把充值卡放到感应区");
        ForegroundColorSpan mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(RechargeActivity.this, R.color.colorRedda0517));
        spanString.setSpan(mForegroundColorSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvRechargePrompt.setText(spanString);

    }

    @Override
    public void onclick() {
        mTvRechargeReturn.setOnClickListener(this);
        mIvRechargeCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvRechargeReturn:
                finish();
                break;
            case R.id.mIvRechargeCode:

                break;
        }
    }

//    public void getdata() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("code", HttpConnect.CODE);
//        map.put("type", getIntent().getStringExtra("PayMode"));
//        map.put("card", "201801181405286297");
//        map.put("amount", "10");
//        Log.e("object", map + "");
//        String url = HttpConnect.URL + "card_refill";
//        new HttpConnect(aq).Connect(url, map,
//                new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject object,
//                                         AjaxStatus status) {
//                        super.callback(url, object, status);
//                        if (object != null) {
//                            if ("1".equals(object.optString("result"))) {
//                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "1"));
//                            } else {
//                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "0"));
//                            }
//                        }
//                    }
//                }
//
//        );
//    }

    private void getdata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("type", getIntent().getStringExtra("PayMode"));
        String url = HttpConnect.URL + "order_pay";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        if (object != null) {
                            Glide.with(RechargeActivity.this).load(object.optString("data")).into(mIvRechargeCode);
                            timer.schedule(task, 0, 5000);  //原来是5000  20190801
//                            if ("1".equals(object.optString("result"))) {
//                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "1"));
//                            } else {
//                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "0"));
//                            }
                            if ("1".equals(object.optString("result"))) {
                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "1"));
                            } else {
                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "0"));
                            }
                        }
                    }
                }

        );
    }

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
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                startActivity(new Intent(RechargeActivity.this, RechargeResultActivity.class).putExtra("PayResult", "1"));
                                timer.cancel();
                            }
                        }
                    }
                }

        );
    }

}
