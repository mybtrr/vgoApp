package com.automation.www.base;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import android.widget.Toast;

import com.androidquery.AQuery;
import com.automation.www.R;
import com.automation.www.activity.CardPaymentMethodActivity;
import com.automation.www.activity.HomeActivity;
import com.automation.www.activity.MachineStoppageActivity;
import com.automation.www.activity.QueryActivity;
import com.automation.www.bean.AdvertisementBean;
import com.automation.www.mvp.model.AdvertisementRequestBean;
import com.automation.www.mvp.presenter.AdvertisementPresenter;
import com.automation.www.mvp.view.AdvertisementView;
import com.automation.www.util.LoadBanner;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/1/6.
 */
public abstract class BaseActivity extends FragmentActivity {
    public AQuery aq;
    
    //20190728
    // 都是static声明的变量，避免被实例化多次；因为整个app只需要一个计时任务就可以了。
    private static Timer mTimer; // 计时器，每1秒执行一次任务
    private static TimerTask mTimerTask; // 计时任务，判断是否未操作时间到达5s
    private static long mLastActionTime; // 上一次操作时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new AQuery(this);
        initUI();
        onclick();
    }

    public abstract void initUI();
    public abstract void onclick();
    private RecyclerView.LayoutManager mLayoutManager;

    protected void setLayoutManager(RecyclerView mRv) {
        mLayoutManager = new LinearLayoutManager(BaseActivity.this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRv.setLayoutManager(mLayoutManager);
    }

    /**
     * 广告
     * Created by Administrator on 2018/1/6. 
     */

    public static class AdvertisementActivity extends BaseActivity implements View.OnClickListener, AdvertisementView {
        private AdvertisementPresenter mAdvertisementPresenter;  //见 mvp/presenter/AdvertisementPresenter
        private Banner mBanner;
        private List<String> images;
        private LoadBanner loadBanner;
        private TextView mTvAdmin, mTvOrder, mTvOpenCard, mTvRecharge, mTvquery, mTvGz;
        private VideoView videoView;

        @Override
        public void initUI() {
            setContentView(R.layout.activity_advertisement);
            findid();
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.test);//播放当前工程中的视频的代码
            videoView.setVideoURI(videoUri); //videoUri:视频来源地址（目录）

			/*
			注：package name是包名com.automation.www，R.raw.tianyi是视频的文件名，
			    视频放在res下的raw文件夹下了，会自动在R.java中生成。
				android中文件名有要求，必须是有效的文件名，包括小写字母和数字，其余的都不行，注意！
				
				videoView.setVideoURI(Uri.parse("file:///sdcard/video/test.3gp"));//这是播放SD卡或内存里的视频。
				videoView.setVideoURI(Uri.parse("http://f3.3g.56.com/15/15/JGfMspPbHtzoqpzseFTPGUsKCEqMXFTW_smooth.3gp"));//网络视频 
			*/
			
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
			//播放不播放视频取决于layout\activity_advertisement中：
			//    <VideoView     中最后一行“ android:visibility="gone"/> ”的位置
            mAdvertisementPresenter = new AdvertisementPresenter(this, aq);
            mAdvertisementPresenter.getBanner(); //见 mvp/presenter/AdvertisementPresenter
            loadBanner = new LoadBanner();
            images = new ArrayList<>();
        }

        private void findid() {
            mBanner = (Banner) findViewById(R.id.mBanner);
            mTvOrder = (TextView) findViewById(R.id.mTvOrder);
            mTvOpenCard = (TextView) findViewById(R.id.mTvOpenCard);
            mTvRecharge = (TextView) findViewById(R.id.mTvRecharge);
            mTvquery = (TextView) findViewById(R.id.mTvquery);
            mTvGz = (TextView) findViewById(R.id.mTvGz);
            mTvAdmin = (TextView) findViewById(R.id.mTvAdmin);
            videoView = findViewById(R.id.video_view); //视频
        }

        @Override
        public void onclick() {
            mBanner.setOnClickListener(this);
            mTvOrder.setOnClickListener(this);
            mTvOpenCard.setOnClickListener(this);
            mTvRecharge.setOnClickListener(this);
            mTvquery.setOnClickListener(this);
            mTvGz.setOnClickListener(this);
            mTvAdmin.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.mBanner:
                    startactivity(intent, HomeActivity.class);
                    break;
                case R.id.mTvOrder:
                    startactivity(intent, HomeActivity.class);
                    break;
                case R.id.mTvOpenCard:
				    Toast.makeText(AdvertisementActivity.this, "此功能暂未开通", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(AdvertisementActivity.this, CardPaymentMethodActivity.class).putExtra("type", "0"));
                    break;
                case R.id.mTvRecharge:
				    Toast.makeText(AdvertisementActivity.this, "此功能暂未开通", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(AdvertisementActivity.this, CardPaymentMethodActivity.class).putExtra("type", "1"));
                    break;
                case R.id.mTvquery:
				    Toast.makeText(AdvertisementActivity.this, "此功能暂未开通", Toast.LENGTH_SHORT).show();
                    //startactivity(intent, QueryActivity.class);
                    break;
                case R.id.mTvGz:
                    startactivity(intent, MachineStoppageActivity.class);
                    break;
                case R.id.mTvAdmin:
    //                startactivity(intent, MachineStoppageActivity.class);
                    break;
            }
        }

        private void startactivity(Intent intent, Class ac) {
            intent = new Intent(AdvertisementActivity.this, ac);
            startActivity(intent);
        }

        @Override //@Override 的作用是：如果想重写父类的方法，系统可以帮你检查方法的正确性。
        public String getcode() {
            //return "UIJOZP43G4";//这个可作为初次使用（调用广告页）时使用，调取机器码,识别商户//20190725
            return "0c:8c:24:7c:1a:76";
            //return "0119060100010610000011908010113512345678";
        }
        @Override
        public String gettype() {
            return "1";
        }
        @Override
        public void success(AdvertisementRequestBean advertisementRequestBean, ArrayList<AdvertisementBean> list) {
            for (int i = 0; i < list.size(); i++) {
                images.add(list.get(i).getImg());
            }
            loadBanner.LoadBanner(mBanner, images);
        }
        @Override
        public void failed() {
            Log.e("test", "失败");
        }  //Log.e ：Logcat视窗下有红色的效果:
    }
}
