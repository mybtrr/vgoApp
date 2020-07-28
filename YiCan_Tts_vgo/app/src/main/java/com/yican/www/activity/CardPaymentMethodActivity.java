package com.yican.www.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yican.www.R;
import com.yican.www.adapter.PaymentMethodAdapter;
import com.yican.www.base.BaseActivity;
import com.yican.www.bean.PaymentMethodJavaBean;

import java.util.ArrayList;

/**
 * 开卡支付方式
 * Created by Administrator on 2018/1/6.
 */

public class CardPaymentMethodActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvPaymentMethodReturn;
    private RecyclerView RvPaymentMethod;
    private PaymentMethodAdapter mPaymentMethodAdapter;
    private ArrayList<PaymentMethodJavaBean> list;
    private int[] img = {R.mipmap.icon_wxzf, R.mipmap.icon_zfb};
    private String content[] = {"微信支付", "支付宝支付"};
    private String PayMode = "";
    private TitleFragment mTitleFragment;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_payment_method);
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentMethod, mTitleFragment)
                .commit();
        list = getdata();
        mPaymentMethodAdapter = new PaymentMethodAdapter(CardPaymentMethodActivity.this, list);
        RvPaymentMethod.setAdapter(mPaymentMethodAdapter);
        GridLayoutManager mgr = new GridLayoutManager(CardPaymentMethodActivity.this, 2);
        RvPaymentMethod.setLayoutManager(mgr);
    }

    private void findid() {
        mTvPaymentMethodReturn = (TextView) findViewById(R.id.mTvPaymentMethodReturn);
        RvPaymentMethod = (RecyclerView) findViewById(R.id.RvPaymentMethod);
    }

    @Override
    public void onclick() {
        mTvPaymentMethodReturn.setOnClickListener(this);
        mPaymentMethodAdapter.setOnItemClickListener(new PaymentMethodAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        PayMode = "1";
                        break;
                    case 1:
                        PayMode = "2";
                        break;

                }
                if (getIntent().getStringExtra("type").equals("0")) {
                    startActivity(new Intent(CardPaymentMethodActivity.this, OpenCardActivity.class).putExtra("PayMode", PayMode));
                } else {
                    startActivity(new Intent(CardPaymentMethodActivity.this, RechargeActivity.class).putExtra("PayMode", PayMode));
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvPaymentMethodReturn:
                finish();
                break;
        }
    }

    public ArrayList<PaymentMethodJavaBean> getdata() {
        ArrayList<PaymentMethodJavaBean> data = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            PaymentMethodJavaBean mPaymentMethodJavaBean = new PaymentMethodJavaBean();
            mPaymentMethodJavaBean.setContent(content[i]);
            mPaymentMethodJavaBean.setImg(img[i]);
            data.add(mPaymentMethodJavaBean);
        }
        return data;
    }
}
