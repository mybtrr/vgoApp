package com.automation.www.mvp.model;

import com.androidquery.AQuery;

/**
 * Created by Administrator on 2018/1/6.
 */

public interface RequestBanner {
    void Request(String code,String type, RequestBannerResuit RequestResuit, AQuery aq);
}
