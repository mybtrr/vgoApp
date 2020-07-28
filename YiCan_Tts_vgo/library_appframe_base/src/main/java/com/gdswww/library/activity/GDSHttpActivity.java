package com.gdswww.library.activity;

import java.util.Map;

import android.app.ProgressDialog;

import com.gdswww.library.http.AQRequest;
import com.gdswww.library.http.HttpRequest;
import com.gdswww.library.http.HttpRequest.RequestCallback;
import com.gdswww.library.toolkit.NetUtil;
import com.loopj.android.http.RequestParams;

/**
 * @ClassName: HttpBaseActivity
 * @Description: 网络请求Activity基类
 * @author shihuanzhang 2335946896@qq_com
 */
public abstract class GDSHttpActivity extends GDSBaseActivity {
	
	public boolean hasNetWork() {
		return NetUtil.hasInternet(context);
	}
	
	public void doRequestByHttp(String httpUrl, final ProgressDialog pd,
			final RequestCallback callback) {
		if (hasNetWork()) {
			HttpRequest.doRequest(httpUrl, pd, callback);
		} else {
			networkError();
		}
	}
	
	public void doRequestByHttp(String httpUrl, RequestParams params,
			final ProgressDialog pd, final RequestCallback callback) {
		if (hasNetWork()) {
			HttpRequest.doRequest(httpUrl,params,pd,callback);
		} else {
			networkError();
		}
	}
	public void doRequestByHttp(String httpUrl, final RequestCallback callback) {
		if (hasNetWork()) {
			HttpRequest.doRequest(httpUrl, callback);
		} else {
			networkError();
		}
	}
	public void doRequestByHttp(String httpUrl, RequestParams params,
			final RequestCallback callback) {
		if (hasNetWork()) {
			HttpRequest.doRequest(httpUrl, params, callback);
		} else {
			networkError();
		}
	}

	public void doRequestAQuery(String httpUrl, final ProgressDialog pd,
			final AQRequest.RequestCallback callback) {
		if (hasNetWork()) {
			AQRequest.doRequest(aq, httpUrl, pd, callback);
		} else {
			networkError();
		}
	}
	public void doRequestAQuery(String httpUrl,final AQRequest.RequestCallback callback) {
		if (hasNetWork()) {
			AQRequest.doRequest(aq, httpUrl,callback);
		} else {
			networkError();
		}
	}
	public void doRequestAQuery(String httpUrl,Map<String, Object> params, final ProgressDialog pd,
			final AQRequest.RequestCallback callback) {
		if (hasNetWork()) {
			AQRequest.doRequest(aq, httpUrl,params,pd,callback);
		} else {
			networkError();
		}
	}

	/**
	  * @Title: networkError 
	  * @Description: 网络错误时调用
	 */
	public abstract void networkError();
}
