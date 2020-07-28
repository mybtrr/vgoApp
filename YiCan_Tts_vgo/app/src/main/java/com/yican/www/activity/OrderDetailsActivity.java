package com.yican.www.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yican.www.R;
import com.yican.www.adapter.OrderDetailsAdapter;
import com.yican.www.base.BaseActivity;
import com.yican.www.bean.PaymentMethodJavaBean;
import com.yican.www.util.HttpConnect;
import com.yican.www.util.MyListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 订单详情
 * Created by Administrator on 2018/1/6.
 */

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvAllMoney, mTvDetailsNumber, mTvOrderDetailsPay, mTvOrderDetailsReturn;
    private TitleFragment mTitleFragment;
    private Animation anim;
    private LinearLayout mLlOrder;
    private MyListView mLvOrderDetails;
    private OrderDetailsAdapter mOrderDetailsAdapter;
    private ArrayList<PaymentMethodJavaBean> list;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_order_details);
        findid();
        mTitleFragment = new TitleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mFlOrderDetails, mTitleFragment)
                .commit();
        confirmOrder();
        anim = new TranslateAnimation(0.0f, 0.0f, -1000, 0);
        startAnimation();
        list = new ArrayList<>();
        mOrderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, list);
        mLvOrderDetails.setAdapter(mOrderDetailsAdapter);
    }

    private void findid() {
        mTvAllMoney = (TextView) findViewById(R.id.mTvAllMoney);
        mTvDetailsNumber = (TextView) findViewById(R.id.mTvDetailsNumber);
        mTvOrderDetailsPay = (TextView) findViewById(R.id.mTvOrderDetailsPay);
        mTvOrderDetailsReturn = (TextView) findViewById(R.id.mTvOrderDetailsReturn);
        mLlOrder = (LinearLayout) findViewById(R.id.mLlOrder);
        mLvOrderDetails = (MyListView) findViewById(R.id.mLvOrderDetails);
    }

    @Override
    public void onclick() {
        mTvOrderDetailsPay.setOnClickListener(this);
        mTvOrderDetailsReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTvOrderDetailsPay:
                startActivity(new Intent(OrderDetailsActivity.this, PaymentMethodActivity.class));
                break;
            case R.id.mTvOrderDetailsReturn:
                finish();
                break;
        }
    }

    /**
     * 确认订单接口 -请求接口
     */
    public void confirmOrder() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        String url = HttpConnect.URL + "confirm_order";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                JSONObject dataObject = object.optJSONObject("data");
                                mTvAllMoney.setText(dataObject.optString("total") + "元");
                                mTvDetailsNumber.setText("共" + dataObject.optString("num") + "件");
                                JSONArray mJsonArray = dataObject.optJSONArray("shop");
                                for (int i = 0; i < mJsonArray.length(); i++) {
                                    JSONObject mJsonObject = mJsonArray.optJSONObject(i);
                                    PaymentMethodJavaBean mPaymentMethodJavaBean = new PaymentMethodJavaBean();
                                    mPaymentMethodJavaBean.setContent(mJsonObject.optString("name"));
                                    mPaymentMethodJavaBean.setId(mJsonObject.optString("id"));
                                    mPaymentMethodJavaBean.setMoney(mJsonObject.optString("price"));
                                    mPaymentMethodJavaBean.setTotal_price(mJsonObject.optString("total_price"));
                                    mPaymentMethodJavaBean.setWeight(mJsonObject.optString("weight"));
                                    list.add(mPaymentMethodJavaBean);
                                }
                                mOrderDetailsAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                    }
                }

        );
    }

    /**
     * 清空购物车接口
     */
    public void shoppingEmpty() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        String url = HttpConnect.URL + "shoppingve_empty";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {

                            } else {
                                Toast.makeText(OrderDetailsActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        anim.cancel();
        shoppingEmpty();
    }

    /**
     * 启动打印订单动画
     */
    private void startAnimation() {
        anim.setDuration(1000);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                mLlOrder.clearAnimation();
            }
        });
        mLlOrder.startAnimation(anim);
    }
}
