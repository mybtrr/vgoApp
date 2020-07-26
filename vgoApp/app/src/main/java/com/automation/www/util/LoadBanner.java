package com.automation.www.util;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.List;



public class LoadBanner {
    public void LoadBanner(Banner banner, List<String> images) {
        //设置banner样式
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Accordion);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(6000);
        //  设置轮播样式（默认为CIRCLE_INDICATOR）	不显示指示器和标题
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
//        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置是否允许手动滑动轮播图 （默认true）
        banner.setViewPagerIsScroll(false);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}
