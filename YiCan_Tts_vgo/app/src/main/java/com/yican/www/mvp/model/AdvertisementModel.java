package com.yican.www.mvp.model;


import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yican.www.bean.AdvertisementBean;
import com.yican.www.util.HttpConnect;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/6.   aaa
 */

public class AdvertisementModel implements RequestBanner {
    private AdvertisementRequestBean mAdvertisementRequestBean;

    @Override
    public void Request(String code, String type, final RequestBannerResuit RequestResuit, AQuery aq) {
        HashMap<String, Object> map = new HashMap<>(); //Map是个接口,HashMap是它的实现类
        map.put("code", code); // 保存 code
        map.put("type", "1"); // 保存 type
        String url = HttpConnect.URL + "advertising_list"; //连接:http://www.yican.xyz/index.php/home/index/advertising_list
        new HttpConnect(aq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {    //callback:回调函数，是一个通过函数指针调用的函数。
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {

                                Log.e("aaa", object + "");
                                JSONArray jsonArray = object.optJSONArray("data"); // 返回一个JSONArray对象
                                mAdvertisementRequestBean = new AdvertisementRequestBean();
                                ArrayList<AdvertisementBean> list = new ArrayList<>(); //List和ArrayList的区别 List是一个接口,而ListArray是一个类。
                                for (int img = 0; img < jsonArray.length(); img++) {
                                    AdvertisementBean mAdvertisementBean = new AdvertisementBean();
                                    mAdvertisementBean.setImg(jsonArray.optString(img));
                                    list.add(mAdvertisementBean); //添加

                                }

                                RequestResuit.Succes(mAdvertisementRequestBean, list);
                            } else {
                                RequestResuit.Failed(); //日志记录：失败
                            }
                        }
                    }
                }

        );
    }
}
