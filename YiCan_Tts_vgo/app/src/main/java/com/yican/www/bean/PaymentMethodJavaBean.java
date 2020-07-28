package com.yican.www.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/17.
 */

public class PaymentMethodJavaBean implements Serializable {
    int img;
    String content;
    String money;
    String imgs;
    String id;
    String weight;
    String total_price;
    String state;
    private byte[] data;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getImgs() {
        return imgs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
