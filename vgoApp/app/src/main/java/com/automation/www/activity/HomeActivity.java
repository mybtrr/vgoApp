package com.automation.www.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import android.graphics.PointF;

import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.utils.LoadDialogUtil;

import com.automation.www.R;
import com.automation.www.base.LongRunningService;
import com.automation.www.adapter.HomeAdapter;
import com.automation.www.adapter.HomeShopAdapter;
import com.automation.www.animation.TopView;
import com.automation.www.base.BaseActivity;
import com.automation.www.bean.PaymentMethodJavaBean;
import com.automation.www.util.HttpConnect;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Timer;
import java.util.TimerTask;

//mTtHomeCancel 主页中的返回键
//RvHomeShop    我的订单
//mTvHomeFinish 购物车
//mTvPurchaseImmediately 立即购买

/**
 * 主页
 * Created by Administrator on 2018/1/12.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTtHomeCancel, mTvPurchaseImmediately, mTvHomeFinish;
    private RecyclerView RvHome, RvHomeShop;
    private HomeAdapter mHomeAdapter;
    private ArrayList<PaymentMethodJavaBean> list;
    private PaymentMethodJavaBean mPaymentMethodJavaBean;
    private ArrayList<PaymentMethodJavaBean> shopList;
    private TextView mTvHomeMoney;
    private String ShopId = "";
    private TopView topView;
    private HomeShopAdapter mHomeShopAdapter;
    private BigDecimal mAllPrice;
    
    private final Timer timer = new Timer();
    private short tim1;  //16位
	private TimerTask task = new TimerTask() { //定时时间在后面的程序里设定，如 “timer.schedule(task, 0, 1000);”
        @Override
        public void run() {
            if(++tim1 > 200) {
                handler.sendEmptyMessage(1); 
            }
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_home);
        Log.i("DemoLog", "ThreadID: " + Thread.currentThread().getId());  //主线程ID
        Log.i("DemoLog", "beforetest startService");
        //连续启动Service
        Intent intent1 = new Intent(this,LongRunningService.class);
        startService(intent1);
        Intent intent2 = new Intent(this,LongRunningService.class);
        startService(intent2);
        Intent intent3 = new Intent(this,LongRunningService.class);
        startService(intent3);
        //停止Service
        Intent intent4 = new Intent(this,LongRunningService.class);
        stopService(intent4);
        //再次启动Service
        Intent intent5 = new Intent(this,LongRunningService.class);
        startService(intent5);
        Intent intent6 = new Intent(this,LongRunningService.class);
        startService(intent6);
        Intent intent7 = new Intent(this,LongRunningService.class);
        startService(intent7);
        Log.i("DemoLog", "aftertest startService");
         */
		timer.schedule(task, 0, 1000);

		Intent intent = new Intent(this, LongRunningService.class);
		startService(intent);
        //主活动要首先有一次启动服务的操作;
		Intent i = new Intent(this, LongRunningService.class);
		startService(i);//启动LongRunningService
        /*
			在程序中调用：context.startService() 
			会触发执行Service生命周期中的onCreate()、onStartCommand()回调方法，
			此时服务就开始正式运行；
			StartService的生命周期为：onCreate --> onStartCommand(可多次调用) --> onDestroy。

		Intent intent = new Intent();
        intent.setClass(this, LongRunningService.class);
        intent.putExtra("sendData", "通过intent保存MainActivity组件中要传递的数据");
        startService(intent);//启动RemoteService服务
*/
	}

    @Override
    public void initUI() {
        setContentView(R.layout.activity_home);
        findid();
        list = new ArrayList<>();
        shopList = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(HomeActivity.this, list);
        RvHome.setAdapter(mHomeAdapter);
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(
                6, 5, PagerGridLayoutManager.HORIZONTAL);
        RvHome.setLayoutManager(layoutManager);
        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(RvHome);
        getdata();
        mHomeShopAdapter = new HomeShopAdapter(HomeActivity.this, shopList);
        RvHomeShop.setAdapter(mHomeShopAdapter);
        GridLayoutManager shop = new GridLayoutManager(HomeActivity.this, 3); //购物车里最多3个
        RvHomeShop.setLayoutManager(shop);
    }

    private void findid() {
        // 动画相关
        topView = TopView.attach2Window(this);
        mTtHomeCancel = (TextView) findViewById(R.id.mTtHomeCancel);
        mTvHomeFinish = (TextView) findViewById(R.id.mTvHomeFinish);
        RvHome = (RecyclerView) findViewById(R.id.RvHome);
        RvHomeShop = (RecyclerView) findViewById(R.id.RvHomeShop);
        mTvHomeMoney = (TextView) findViewById(R.id.mTvHomeMoney);
        mTvPurchaseImmediately = (TextView) findViewById(R.id.mTvPurchaseImmediately);
    }

    @Override
    public void onclick() {
        mTtHomeCancel.setOnClickListener(this);
        mTvPurchaseImmediately.setOnClickListener(this);
        mTvHomeFinish.setOnClickListener(this);
        mHomeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position, View view) {
                    if (shopList.size() > 2) {
                        Toast.makeText(HomeActivity.this, "最多只能买三样", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addGoods(list.get(position).getId(), view, position); //
                }
        });
        mHomeShopAdapter.setOnItemClickListener(new HomeShopAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {

            }
            @Override
            public void onItemDelected(int position) {
                for (int i = 0; i < shopList.size(); i++) {
                    if (i == position) {
                        mAllPrice = mAllPrice.subtract(new BigDecimal(shopList.get(i).getTotal_price()));//价格
                    }
                }
                mTvHomeMoney.setText("¥" + mAllPrice.toString());
                shopList.remove(position);
                mHomeShopAdapter.notifyDataSetChanged();
            }
        });
    }
	
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mTtHomeCancel:  //返回键 结束进程
                timer.cancel();
                //startActivity(new Intent(HomeActivity.this, AdvertisementActivity.class));
                finish(); //返回前一页
                // 原理 ：android是使用栈来管理Activity的，也就是先进后出的原则。我们每打开一个Activity，就会被压入栈顶，于是调用finish()方法或返回键都会被栈顶弹出（等待系统回收）。这样当一Activity被finish掉，上一个未finish掉的Activity就会被激活，就这样。。
                break;
            case R.id.mTvPurchaseImmediately: //立即购买
                ShopId = "";
                for (int i = 0; i < shopList.size(); i++) {
                    ShopId = shopList.get(i).getId() + "," + ShopId;
                }
                if (ShopId.length() > 0) {
                    ShopId = ShopId.substring(0, ShopId.length() - 1);
                    shoppingSubmit(0);
                } else {
                    Toast.makeText(HomeActivity.this, "商品不能空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mTvHomeFinish:  //购物车
                ShopId = "";
                for (int i = 0; i < shopList.size(); i++) {
                    ShopId = shopList.get(i).getId() + "," + ShopId;
                }
                if (ShopId.length() > 0) {
                    ShopId = ShopId.substring(0, ShopId.length() - 1);
                    shoppingSubmit(1);
                } else {
                    Toast.makeText(HomeActivity.this, "购物车不能空", Toast.LENGTH_SHORT).show();
                }
                break;		
        }
    }

    // 在特定的时刻为我们广播一个指定的Intent ,在这里做接收
    public class AlarmReceiver extends BroadcastReceiver {
		//static final String ACTION = "android.intent.action.BOOT_COMPLETED";
        @Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "收到定时广播", 1).show();
			Intent i = new Intent(context, LongRunningService.class);
			context.startService(i);
        }
    }

	
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    timer.cancel();
                    //finish();//跳转到广告页
                    startActivity(new Intent(HomeActivity.this, AdvertisementActivity.class));
                    break;
            }
        }
    };

    //选择商品
    public void addGoods(String goodsId, final View view, final int position) { 
        LoadDialogUtil.showDialog(this);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("pid", goodsId);   //sid 换做 pid 20190805
        String url = HttpConnect.URL + "Choosing_good";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        LoadDialogUtil.dismissDialog();
                        Log.e("object", object + "");   //提示信息
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                              JSONArray data=  object.optJSONArray("data");
                               JSONObject jsonObject= data.optJSONObject(0);
							   /*
                               if( jsonObject.optString("state").equals("1")){
                                   animation(view, list.get(position).getImgs());
                                   PaymentMethodJavaBean mPaymentMethodJavaBean = new PaymentMethodJavaBean();
                                   mPaymentMethodJavaBean.setContent(list.get(position).getContent());
                                   mPaymentMethodJavaBean.setTotal_price(list.get(position).getTotal_price());
                                   mPaymentMethodJavaBean.setId(list.get(position).getId());
                                   mPaymentMethodJavaBean.setWeight(list.get(position).getWeight());
                                   mPaymentMethodJavaBean.setMoney(list.get(position).getMoney());
                                   mPaymentMethodJavaBean.setData(list.get(position).getData());
                                   shopList.add(mPaymentMethodJavaBean);
                                   mHomeShopAdapter.notifyDataSetChanged();
                                   BigDecimal price = new BigDecimal(0);
                                   for (int i = 0; i < shopList.size(); i++) {
                                       price = price.add(new BigDecimal(shopList.get(i).getTotal_price()));
                                   }
                                   mAllPrice = price;
                                   mTvHomeMoney.setText("¥" + price.toString());
                               } else{
                                   Toast.makeText(HomeActivity.this, "该商品无库存", Toast.LENGTH_SHORT).show();
                               }
                                */
							   if( jsonObject.optString("state").equals("0")){
                                   Toast.makeText(HomeActivity.this, "该商品无库存", Toast.LENGTH_SHORT).show();
                               } else{
                                   animation(view, list.get(position).getImgs());
                                   PaymentMethodJavaBean mPaymentMethodJavaBean = new PaymentMethodJavaBean();
                                   mPaymentMethodJavaBean.setContent(list.get(position).getContent());
                                   mPaymentMethodJavaBean.setTotal_price(list.get(position).getTotal_price());
                                   mPaymentMethodJavaBean.setId(jsonObject.optString("state"));
                                   mPaymentMethodJavaBean.setWeight(list.get(position).getWeight());
                                   mPaymentMethodJavaBean.setMoney(list.get(position).getMoney());
                                   mPaymentMethodJavaBean.setData(list.get(position).getData());
                                   shopList.add(mPaymentMethodJavaBean);
                                   mHomeShopAdapter.notifyDataSetChanged();
                                   BigDecimal price = new BigDecimal(0);
                                   for (int i = 0; i < shopList.size(); i++) {
                                       price = price.add(new BigDecimal(shopList.get(i).getTotal_price()));
                                   }
                                   mAllPrice = price;
                                   mTvHomeMoney.setText("¥" + price.toString());
                               }

                            } else {
                                Toast.makeText(HomeActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
    }

    public void getdata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        String url = HttpConnect.URL + "index";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                Log.e("aaa", object + "");
                                JSONArray jsonArray = object.optJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    mPaymentMethodJavaBean = new PaymentMethodJavaBean();
                                    JSONObject mJosnObject = jsonArray.optJSONObject(i);
                                    mPaymentMethodJavaBean.setMoney(mJosnObject.optString("price"));
                                    mPaymentMethodJavaBean.setTotal_price(mJosnObject.optString("total_price"));
                                    mPaymentMethodJavaBean.setContent(mJosnObject.optString("name"));
                                    mPaymentMethodJavaBean.setImgs(mJosnObject.optString("img"));
                                    mPaymentMethodJavaBean.setId(mJosnObject.optString("pid"));    //这里也换做 pid
                                    mPaymentMethodJavaBean.setWeight(mJosnObject.optString("weight"));
                                    mPaymentMethodJavaBean.setState(mJosnObject.optString("state"));
                                    mPaymentMethodJavaBean.setData(new byte[]{(byte) 0xEF, 0x01, 0x02, 0x02, (byte) (i + 1), (byte) (0x01 + 0x02 + 0x02 + i + 1), (byte) 0xFE});  // 出货位置串口指令信息
                                    list.add(mPaymentMethodJavaBean);
                                }
                                mHomeAdapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                    }
                }

        );
    }

    /**
     * NumberFormat is the abstract base class for all number formats.
     * This class provides the interface for formatting and parsing numbers.
     *
     * @param d
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String formatDouble(float d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);
        return nf.format(d);
    }

    //提交购物车
    public void shoppingSubmit(final int type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE);
        map.put("sid", ShopId);
        String url = HttpConnect.URL + "shoppingve_submit";
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                                if (type == 1) {
                                    timer.cancel();
                                    //跳转页面 订单详情
                                    startActivity(new Intent(HomeActivity.this, OrderDetailsActivity.class));
                                } else {
                                    timer.cancel();
                                    //跳转页面 支付方式
                                    startActivity(new Intent(HomeActivity.this, PaymentMethodActivity.class)
                                            .putExtra("shopList", shopList));
                                }
                            } else {
                            }
                        }
                    }
                }

        );
    }

    /**
     * 菜品移动动画
     */
    private int count;

    public void animation(View view, String url) {
//        添加确定的订单到  我的订单
        int[] start = new int[]{0, 0};
        // 起点
        view.getLocationInWindow(start);
        int[] end = new int[]{0, 0};
        // 终点 我的订单
        RvHomeShop.getLocationInWindow(end);
        PointF p0 = new PointF(start[0], start[1]); // 起点
        PointF p1 = new PointF(start[0], start[1] - 1400); // 第二点
        PointF p2 = new PointF(end[0], end[1] - 210); // 终点
        // TODO
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheEnabled(false);
        count++;
        TopView.AnimationInfo animationInfo = new TopView.AnimationInfo.Builder().callback(
                new TopView.AnimationCallback() {
                    @Override
                    public void animationEnd() {
                        // 更新显示数字

                    }
                })
                .resId(url)
                .p0(p0)
                .p1(p1)
                .p2(p2)
                .create();
        topView.add(animationInfo, url);
    }
}
 /*
            slideView = findViewById(R.id.slide_view1);
        slideView.setOnClickListener(new Listener.OnMenuClickListener() {
            @Override
            public void onClick(int id) {
                switch(id){

                    case R.id.menu_a:
                        Util.toast("点击 删除");
                        break;
                    case R.id.menu_b:
                        Util.toast("点击 设为未读");
                        break;
                    case R.id.sure_delete:
                        Util.toast("点击 确认删除");
                        break;
                    case R.id.long_press:
                        Util.toast("长按");
                        VibratorLib.vibrateShort();
                        break;
                    case R.id.double_click:
                        Util.toast("双击");
                        break;
                }
            }
        });
*/
