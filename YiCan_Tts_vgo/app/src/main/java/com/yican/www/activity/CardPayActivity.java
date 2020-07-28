package com.yican.www.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yican.www.R;
import com.yican.www.base.BaseActivity;
import com.yican.www.util.HttpConnect;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/21.
 */

public class CardPayActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvCardPayReturn;
    private ImageView mIvCardPay;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_card_pay);
        findid();
    }

    private void findid() {
        mTvCardPayReturn = (TextView) findViewById(R.id.mTvCardPayReturn);
        mIvCardPay = (ImageView) findViewById(R.id.mIvCardPay);
    }

    @Override
    public void onclick() {
        mTvCardPayReturn.setOnClickListener(this);
        mIvCardPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvCardPay:
                orderPay();
                break;
            case R.id.mTvCardPayReturn:
                finish();
                break;
        }
    }

    /**
     * 订单支付接口
     */
    public void orderPay() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("type", "3");
        map.put("card", "201801181405286297");
        Log.e("map", map + "");
        String url = HttpConnect.URL + "order_pay";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                Log.e("object", object + "");
                                startActivity(new Intent(CardPayActivity.this, PaymentCompletionActivity.class).putExtra("payresult", "1"));
                            } else {
                                startActivity(new Intent(CardPayActivity.this, PaymentCompletionActivity.class).putExtra("payresult", "0"));
                            }
                        }
                    }
                }

        );
    }
}
