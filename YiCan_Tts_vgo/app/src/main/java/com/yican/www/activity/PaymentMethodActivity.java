package com.yican.www.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yican.www.R;
import com.yican.www.adapter.PaymentMethodAdapter;
import com.yican.www.base.BaseActivity;
import com.yican.www.bean.PaymentMethodJavaBean;
import com.yican.www.util.BaiduTtsUtil;

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
                handler.sendEmptyMessage(10);
            }
            //BaiduTtsUtil.getInstance().speak("计时工作2+" + count);
        }
    };
    private TimerTask task3 = new TimerTask() {
        @Override
        public void run() {
            ++count;
            if(count >= 20) {
                handler.sendEmptyMessage(20);
            }
            //BaiduTtsUtil.getInstance().speak("计时工作3+" + count);
        }
    };


    @Override
    public void initUI() {
        count=0;
        timer1.schedule(task1, 0, 1500);
        BaiduTtsUtil.getInstance().speak("请选择支付方式");
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
                        timer1.cancel();
                        count=0;
                        BaiduTtsUtil.getInstance().speak("  请用微信扫描二维码");
                        startActivity(new Intent(PaymentMethodActivity.this, PaymentScavengingActivity.class)
                                .putExtra("paytype", paymentType)
                                .putExtra("PayMode", "1")
                                .putExtra("shopList", getIntent().getSerializableExtra("shopList")));
                        break;
                    case 1:
                        paymentType = "2";
                        timer1.cancel();
                        count=0;
                        BaiduTtsUtil.getInstance().speak("  请用支付宝扫描二维码");
                        startActivity(new Intent(PaymentMethodActivity.this, PaymentScavengingActivity.class)
                                .putExtra("paytype", paymentType)
                                .putExtra("PayMode", "1")
                                .putExtra("shopList", getIntent().getSerializableExtra("shopList")));
                        break;
                    case 2:
                        BaiduTtsUtil.getInstance().speak("  此功能暂未开通");
                        Toast.makeText(PaymentMethodActivity.this, "此功能暂未开通", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        BaiduTtsUtil.getInstance().speak("  此功能暂未开通");
                        //startActivity(new Intent(PaymentMethodActivity.this, CardPayActivity.class));
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvPaymentMethodReturn:
                timer1.cancel();
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {  //定时信息处理
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    BaiduTtsUtil.getInstance().speak("请选择支付方式");
                    Toast.makeText(PaymentMethodActivity .this, "请请选择支付方式", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    BaiduTtsUtil.getInstance().speak("请请尽快选择");
                    Toast.makeText(PaymentMethodActivity .this, "请尽快选择", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    timer1.cancel();
                    count=0;
                    BaiduTtsUtil.getInstance().speak("对对不起，超时了！");
                    Toast.makeText(PaymentMethodActivity .this, "超时了！！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentMethodActivity.this, AdvertisementActivity.class));
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
