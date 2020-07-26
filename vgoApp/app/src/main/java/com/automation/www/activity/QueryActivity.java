package com.automation.www.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
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

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 查询
 * Created by Administrator on 2018/1/13.
 */

public class QueryActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvRechargeReturn, mTvRechargePrompt;
    private ImageView mIvRechargeCode;
    private TitleFragment mTitleFragment;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_recharge);
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentMethod, mTitleFragment)
                .commit();
        mTvRechargePrompt.setText("请把充值卡放到感应区");
        mIvRechargeCode.setImageResource(R.mipmap.icon_warning);
    }

    private void findid() {
        mTvRechargeReturn = (TextView) findViewById(R.id.mTvRechargeReturn);
        mTvRechargePrompt = (TextView) findViewById(R.id.mTvRechargePrompt);
        mIvRechargeCode = (ImageView) findViewById(R.id.mIvRechargeCode);
    }

    @Override
    public void onclick() {
        mTvRechargeReturn.setOnClickListener(this);
        mTvRechargePrompt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvRechargeReturn:
                finish();
                break;
            case R.id.mTvRechargePrompt:
                getdata();
                break;
        }
    }

    public void getdata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("card", "201801181405286297");
        String url = HttpConnect.URL + "query_amount";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                SpannableString spanString = new SpannableString("查询成功您当前的余额为 : " + object.optString("data"));
                                ForegroundColorSpan mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(QueryActivity.this, R.color.colorRedda0517));
                                spanString.setSpan(mForegroundColorSpan, 13, spanString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                mTvRechargePrompt.setText(spanString);
                            } else {
                                mTvRechargePrompt.setText("查询失败");
                            }
                        }
                    }
                }

        );
    }
}
