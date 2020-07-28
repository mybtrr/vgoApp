package com.yican.www.activity;

import android.widget.TextView;

import com.yican.www.R;
import com.yican.www.base.BaseActivity;

/**
 * 机器停工
 * Created by Administrator on 2018/1/16.
 */

public class MachineStoppageActivity extends BaseActivity {
    private TextView mTvFaultCode;
    private TitleFragment mTitleFragment;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_machine_stoppage);
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlMachineStoppage, mTitleFragment)
                .commit();
    }

    private void findid() {
        mTvFaultCode = (TextView) findViewById(R.id.mTvFaultCode);
        mTvFaultCode.setText("（故障代码：0314）");

    }

    @Override
    public void onclick() {

    }
}
