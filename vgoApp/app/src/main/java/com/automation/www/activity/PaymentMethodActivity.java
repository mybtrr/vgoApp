package com.automation.www.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.os.Message;
import com.automation.www.R;
import com.automation.www.adapter.PaymentMethodAdapter;
import com.automation.www.base.BaseActivity;
import com.automation.www.bean.PaymentMethodJavaBean;

import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 支付方式
 * Created by Administrator on 2018/1/6.
 */

public class PaymentMethodActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvPaymentMethodReturn;
    private RecyclerView RvPaymentMethod;
    private PaymentMethodAdapter mPaymentMethodAdapter;
    private ArrayList<PaymentMethodJavaBean> list;
    private int[] img = {R.mipmap.icon_wxzf, R.mipmap.icon_zfb, R.mipmap.icon_yly, R.mipmap.icon_czk};
    private String content[] = {"微信支付", "支付宝支付", "银联云闪付", "充值卡支付"};
    private String paymentType = "";
    private TitleFragment mTitleFragment;
    private final Timer timer = new Timer();
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
                handler.sendEmptyMessage(0); //每隔2秒，定时发送 handler 消息
            }
        }
    };
    @Override
    public void initUI() {
        //timer.schedule(task,10000);//安排在指定延迟后执行指定的任务
        timer.schedule(task, 0, 1500);

        setContentView(R.layout.activity_payment_method);
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlPaymentMethod, mTitleFragment)
                .commit();
        list = getdata();
        mPaymentMethodAdapter = new PaymentMethodAdapter(PaymentMethodActivity.this, list);
        RvPaymentMethod.setAdapter(mPaymentMethodAdapter);
        GridLayoutManager mgr = new GridLayoutManager(PaymentMethodActivity.this, 2);
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
                        paymentType = "1";
                        timer.cancel();
                        startActivity(new Intent(PaymentMethodActivity.this, PaymentScavengingActivity.class)
                                .putExtra("paytype", paymentType)
                                .putExtra("PayMode", "1")
                                .putExtra("shopList", getIntent().getSerializableExtra("shopList")));
                        break;
                    case 1:
                        paymentType = "2";
                        timer.cancel();
                        startActivity(new Intent(PaymentMethodActivity.this, PaymentScavengingActivity.class)
                                .putExtra("paytype", paymentType)
                                .putExtra("PayMode", "1")
                                .putExtra("shopList", getIntent().getSerializableExtra("shopList")));
                        break;
                    case 2:
                        timer.cancel();
                        Toast.makeText(PaymentMethodActivity.this, "此功能暂未开通", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        timer.cancel();
                        startActivity(new Intent(PaymentMethodActivity.this, CardPayActivity.class));
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvPaymentMethodReturn:
			    timer.cancel();
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    Toast.makeText(PaymentMethodActivity .this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    Toast.makeText(PaymentMethodActivity .this, "请尽快选择", Toast.LENGTH_SHORT).show();
                    break;
                case 12:
                    Toast.makeText(PaymentMethodActivity .this, "超时了！！", Toast.LENGTH_SHORT).show();
                    timer.cancel();
					
                    startActivity(new Intent(PaymentMethodActivity.this, AdvertisementActivity.class));
					
                    //Intent intent = new Intent(PaymentMethodActivity.this, HomeActivity.class);  //跳转到首页
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //startActivity(intent);
                    break;
            }
        }
    };

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
