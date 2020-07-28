package com.yican.www.util;import com.androidquery.AQuery;import com.androidquery.callback.AjaxCallback;import com.androidquery.callback.AjaxStatus;import org.json.JSONObject;import java.util.HashMap;import java.util.Map;/**  连接网站*/public class HttpConnect {    //public static final String URL = "http://www.yican.shop/index.php/home/index/";    public static final String URL = "http://shop.app024.cn/index.php/home/index/"; //静态常量,设置接口网址    //public static final String URL = "http://192.168.1.101/index.php/home/index/";    private AQuery aq;    //public static String CODE = "0119060100010610000011908010113512345678"; // 机器号:UIJOZP43G4    public static String CODE = "0c:8c:24:7c:1a:76";    //public static String CODE = NetUtil.getMACAddress("wlan0");//MAC eth0   读取MAC作为机器码的唯一标识    public HttpConnect(AQuery aq) {        this.aq = aq;    }    /**     * @param url     * @param map     * @param callback     */    public void ConnectTD(String url, Map<String, String> map,                          final AjaxCallback<JSONObject> callback) {        aq.ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {            @Override            public void callback(String url, JSONObject object,                                 AjaxStatus status) {                if (object == null) {                } else {                    callback.callback(url, object, status);                }            }        });    }    /**     * @param url     * @param map     * @param callback     */    public void Connect(String url, HashMap<String, Object> map,                        final AjaxCallback<JSONObject> callback) {        aq.ajax(url, map, JSONObject.class,                new AjaxCallback<JSONObject>() {                    @Override                    public void callback(String url, JSONObject object,                                         AjaxStatus status) {                        if (object == null) {                        } else {                            callback.callback(url, object, status);                        }                    }                });    }    /**     * @param url     * @param callback     */    public void Connect(String url, final AjaxCallback<JSONObject> callback) {        aq.ajax(url, JSONObject.class,                new AjaxCallback<JSONObject>() {                    @Override                    public void callback(String url, JSONObject object,                                         AjaxStatus status) {                        if (object == null) {                        } else {                            callback.callback(url, object, status);                        }                    }                });    }}