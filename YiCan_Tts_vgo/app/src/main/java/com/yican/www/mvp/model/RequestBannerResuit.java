package com.yican.www.mvp.model;


import com.yican.www.bean.AdvertisementBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/6.
 */

public interface RequestBannerResuit {
    void Succes(AdvertisementRequestBean AdvertisementRequestBean, ArrayList<AdvertisementBean> list);

    void Failed();
}
