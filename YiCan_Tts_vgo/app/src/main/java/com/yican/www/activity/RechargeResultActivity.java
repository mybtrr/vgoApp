package com.yican.www.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yican.www.R;
import com.yican.www.base.BaseActivity;

/**
 * 充值结果
 * Created by Administrator on 2018/1/13.
 */

public class RechargeResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvRechargeResult, mTvRechargeDetermine;
    private ImageView mIvRechargeResult;
    private TitleFragment mTitleFragment;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_recharge_result);
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentMethod, mTitleFragment)
                .commit();
        if (getIntent().getStringExtra("PayResult").equals("1")) {
            mTvRechargeResult.setText("充值成功");
            mIvRechargeResult.setImageResource(R.mipmap.icon_wancheng);
        } else {
            mTvRechargeResult.setText("充值失败");
            mIvRechargeResult.setImageResource(R.mipmap.icon_shibai);
        }
    }

    private void findid() {
        mTvRechargeResult = (TextView) findViewById(R.id.mTvRechargeResult);
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
}
