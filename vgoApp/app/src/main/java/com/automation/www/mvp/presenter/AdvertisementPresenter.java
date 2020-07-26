package com.automation.www.mvp.presenter;

import com.androidquery.AQuery;
import com.automation.www.bean.AdvertisementBean;
import com.automation.www.mvp.model.AdvertisementModel;
import com.automation.www.mvp.model.AdvertisementRequestBean;
import com.automation.www.mvp.model.RequestBannerResuit;
import com.automation.www.mvp.view.AdvertisementView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/6.
 */

public class AdvertisementPresenter {

    private AdvertisementModel mdvertisementModel;
    private AdvertisementView mAdvertisementView;
    private AQuery mAq;

    public AdvertisementPresenter(AdvertisementView advertisementView, AQuery aq) {
        this.mAdvertisementView = advertisementView;
        this.mdvertisementModel = new AdvertisementModel(); //见 mvp/model/AdvertisementModel()
        this.mAq = aq;
    }

    public void getBanner() {
        mdvertisementModel.Request(mAdvertisementView.getcode(), mAdvertisementView.gettype(), new RequestBannerResuit() {
            @Override
            public void Succes(AdvertisementRequestBean AdvertisementRequestBean, ArrayList<AdvertisementBean> list) {
                mAdvertisementView.success(AdvertisementRequestBean, list);
            }

            @Override
            public void Failed() {
                mAdvertisementView.failed(); //list=mAdvertisementView:// 可视信息
            }
        }, mAq);

    }
}
