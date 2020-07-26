package com.automation.www.activity;

import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.automation.www.R;
import com.automation.www.base.BaseActivity;

/**
 * 开卡结果
 * Created by Administrator on 2018/1/13.
 */

public class OpenCardResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvRechargeResult, mTvRechargePrompt, mTvRechargeDetermine;
    private ImageView mIvRechargeResult;
    private TitleFragment mTitleFragment;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_recharge_result);
        findid();
        if (getIntent().getStringExtra("PayResult").equals("1")) {
            mTvRechargeResult.setText("购买成功");
            mIvRechargeResult.setImageResource(R.mipmap.icon_wancheng);
            SetSpannableStringContent("* 请留意出货口");
        } else {
            mTvRechargeResult.setText("购买失败");
            mIvRechargeResult.setImageResource(R.mipmap.icon_shibai);
            SetSpannableStringContent("* 购买失败");
        }
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentMethod, mTitleFragment)
                .commit();

    }

    private void findid() {
        mTvRechargeResult = (TextView) findViewById(R.id.mTvRechargeResult);
        mTvRechargePrompt = (TextView) findViewById(R.id.mTvRechargePrompt);
        mTvRechargeDetermine = (TextView) findViewById(R.id.mTvRechargeDetermine);
        mIvRechargeResult = (ImageView) findViewById(R.id.mIvRechargeResult);

    }

    @Override
    public void onclick() {
        mTvRechargeDetermine.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvRechargeDetermine:
                finish();
                break;
        }
    }

    private void SetSpannableStringContent(String Content) {
        SpannableString spanString = new SpannableString(Content);
        ForegroundColorSpan mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(OpenCardResultActivity.this, R.color.colorRedda0517));
        spanString.setSpan(mForegroundColorSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvRechargePrompt.setText(spanString);
    }

}
