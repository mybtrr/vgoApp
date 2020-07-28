package com.yican.www.mvp.view;


import com.yican.www.bean.AdvertisementBean;
import com.yican.www.mvp.model.AdvertisementRequestBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/6.
 */

public interface AdvertisementView {


    String getcode();
    String gettype();

    void success(AdvertisementRequestBean advertisementRequestBean, ArrayList<AdvertisementBean> list);

    void failed();
}
