package com.yican.www.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yican.www.R;
import com.yican.www.util.GlideImageLoader;
import com.yican.www.util.HttpConnect;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class TitleFragment extends Fragment implements OnBannerClickListener {
    List<View> views = new ArrayList<>();
    private Banner banner;
    private List<String> imglist;
    private AQuery mAq;

    public TitleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_title, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAq = new AQuery(getActivity());
        findid();
        getData();
    }

    private void findid() {
        banner = (Banner) getActivity().findViewById(R.id.banners);
    }


	//存放图片的标题
    private String[]  titles = new String[]{
        "巩俐不低俗，我就不能低俗", 
        "扑树又回来啦！再唱经典老歌引万人大合唱", 
        "揭秘北京电影如何升级", 
        "乐视网TV版大派送", 
        "热血屌丝的反杀"
        };

    public void SetBannerData() {
        if (imglist != null) {
            //设置banner样式
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //  设置轮播样式（默认为CIRCLE_INDICATOR）	不显示指示器和标题
            banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            banner.setOnBannerClickListener(this);
            //设置图片集合
            banner.setImages(imglist);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.ZoomOut);			
            //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.setViewPagerIsScroll(false);
            banner.start();
        }
    }

    @Override
    public void OnBannerClick(int position) {  //添加点击事件

    }
    
	//获取广告图片
    private void getData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpConnect.CODE); 
        map.put("type", "2");
        String url = HttpConnect.URL + "advertising_list";
        new HttpConnect(mAq).Connect(url, map,
                new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        Log.e("object", object + "");
                        if (object != null) {
                            if ("1".equals(object.optString("result"))) {
                               //# Log.e("aaaaaaaaaaaaaaaaaaaaa", object + "");
                                Log.e("aaa", object + "");
                                JSONArray jsonArray = object.optJSONArray("data");
                                imglist = new ArrayList<>();
                                for (int img = 0; img < jsonArray.length(); img++) {
                                    imglist.add(jsonArray.optString(img));
                                }
                                SetBannerData();
                            } else {
                            }
                        }
                    }
                }

        );
    }


}
