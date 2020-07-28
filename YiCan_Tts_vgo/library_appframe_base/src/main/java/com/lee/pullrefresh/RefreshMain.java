package com.lee.pullrefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gdswww.library.R;

public class RefreshMain extends Activity{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_main);
        
        findViewById(R.id.listview).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RefreshMain.this, PullRefreshListViewActivity.class));
			}
		});
        findViewById(R.id.webview).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RefreshMain.this, PullRefreshWebViewActivity.class));
			}
		});
        findViewById(R.id.scrollview).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RefreshMain.this, PullRefreshScrollViewActivity.class));
			}
		});
    }
    

}
