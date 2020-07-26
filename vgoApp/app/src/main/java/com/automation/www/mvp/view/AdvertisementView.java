package com.automation.www.mvp.view;


import com.automation.www.bean.AdvertisementBean;
import com.automation.www.mvp.model.AdvertisementRequestBean;

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
