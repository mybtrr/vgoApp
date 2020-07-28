package com.gdswww.library.fragment;import java.lang.ref.WeakReference;import com.androidquery.AQuery;import com.gdswww.library.dialog.AppProgressDialog;import android.app.Activity;import android.app.ProgressDialog;import android.content.Context;import android.content.Intent;import android.content.res.Resources;import android.content.res.Resources.NotFoundException;import android.os.Bundle;import android.os.Handler;import android.os.Message;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.view.ViewPager;import android.util.Log;import android.view.Gravity;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.webkit.WebView;import android.widget.Button;import android.widget.EditText;import android.widget.ExpandableListView;import android.widget.ImageButton;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.ListView;import android.widget.RadioButton;import android.widget.RadioGroup;import android.widget.RelativeLayout;import android.widget.TextView;import android.widget.Toast;/** * Fragment基类 *  * @author shi */public abstract class BaseFragment extends Fragment {	public MyHandler handler;	private String TAG;	public AQuery aq;	public AppProgressDialog pd;	public Message msg;	public Resources res;	public Context context;	public Activity activity;	public View rootView;	public FragmentManager fragmentManager;	private void createProgressDialog() {		if (pd == null) {			pd = new AppProgressDialog(activity);			pd.setCanceledOnTouchOutside(false);			pd.getWindow().setGravity(Gravity.CENTER);		}	}	public void showProgressDialog(String message, boolean isCancelable) {		createProgressDialog();		pd.setCancelable(isCancelable);		pd.setMessage(message);		pd.show();	}	public void dimissProgressDialog() {		if (pd != null && pd.isShowing()) {			pd.dismiss();		}	}	public ProgressDialog getProgessDialog(String message, boolean isCancel) {		createProgressDialog();		pd.setCancelable(isCancel);		pd.setMessage(message);		return pd;	}	public void cancelProgressDialog() {		if (pd != null && pd.isShowing()) {			pd.cancel();		}	}	static class MyHandler extends Handler {		WeakReference<BaseFragment> mActivity;		MyHandler(BaseFragment activity) {			mActivity = new WeakReference<BaseFragment>(activity);		}		@Override		public void handleMessage(Message msg) {			mActivity.get().undateUI(msg);		}	}	/**	 * 子类实现该方法 返回布局文件id	 */	public abstract int setContentView();	public abstract void undateUI(Message msg);	public abstract void initUI();	public abstract void regUIEvent();	public BaseFragment(FragmentManager fragmentManager) {		this.fragmentManager = fragmentManager;	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		return inflater.inflate(setContentView(), container, false);	}	@Override	public void onActivityCreated(Bundle savedInstanceState) {		// TODO Auto-generated method stub		super.onActivityCreated(savedInstanceState);		context = getActivity().getApplicationContext();		activity = getActivity();		rootView = getView();		TAG = getClass().getSimpleName();		handler = new MyHandler(this);		msg = handler.obtainMessage();		aq = new AQuery(activity);		res = getResources();		initUI();		regUIEvent();	}	public void printError(String Message) {		if (Message != null) {			Log.e(TAG, Message);		}	}	public String getResStr(int id) {		String str = "";		try {			str = getResources().getString(id);		} catch (NotFoundException e) {			// TODO Auto-generated catch block			e.printStackTrace();			str = "";		}		return str;	}	private Toast toast;	public View findViewById(int id) {		return rootView.findViewById(id);	}	public ExpandableListView getExpandableListView(int id) {		return (ExpandableListView) findViewById(id);	}	public TextView getTextView(int id) {		return (TextView) findViewById(id);	}	public RadioButton getRadioButton(int id) {		return (RadioButton) findViewById(id);	}	public RadioGroup getRadioGroup(int id) {		return (RadioGroup) findViewById(id);	}	public EditText getEditText(int id) {		return (EditText) findViewById(id);	}	public Button getButton(int id) {		return (Button) findViewById(id);	}	public WebView getWebView(int id) {		return (WebView) findViewById(id);	}	public void startActivity(Class<?> cls) {		startActivity(new Intent(context, cls));	}	public ImageButton getImageButton(int id) {		return (ImageButton) findViewById(id);	}	public ImageView getImageView(int id) {		return (ImageView) findViewById(id);	}	public ListView getListView(int id) {		return (ListView) findViewById(id);	}	public ViewPager getViewPager(int id) {		return (ViewPager) findViewById(id);	}	public LinearLayout getLinearLayout(int id) {		return (LinearLayout) findViewById(id);	}	public RelativeLayout getRelativeLayout(int id) {		return (RelativeLayout) findViewById(id);	}	/**	 * 获取文本框的文本	 * 	 * @param e	 * @return	 */	public String getEditTextString(EditText e) {		String str = "";		str = (e == null ? "" : e.getText().toString().trim());		return str;	}	/**	 * 隐藏视图	 * 	 * @param v	 */	public void hideView(View v) {		if (v != null) {			v.setVisibility(View.GONE);		}	}	/**	 * 隐藏视图但它占据了空间	 * 	 * @param v	 */	public void hideViewHasSpace(View v) {		if (v != null) {			v.setVisibility(View.INVISIBLE);		}	}	/**	 * 显示视图	 * 	 * @param v	 */	public void showView(View v) {		if (v != null) {			v.setVisibility(View.VISIBLE);		}	}	/**	 * 激活视图	 * 	 * @param v	 */	public void enableView(View v) {		if (v != null) {			v.setEnabled(false);		}	}	/**	 * 禁用视图	 * 	 * @param v	 */	public void disableView(View v) {		if (v != null) {			v.setEnabled(true);		}	}	/**	 * 显示Toast提示	 * 	 * @param s	 */	public void showToatWithShort(String s) {		if (toast == null) {			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);		}		toast.setDuration(Toast.LENGTH_SHORT);		toast.setText(s);		toast.show();	}	/**	 * 显示Toast提示	 * 	 * @param s	 */	public void showToatWithLong(String s) {		if (toast == null) {			toast = Toast.makeText(context, "", Toast.LENGTH_LONG);		}		toast.setDuration(Toast.LENGTH_LONG);		toast.setText(s);		toast.show();	}	public int getResColor(int colorId) {		int color = -1;		try {			color = getResources().getColor(colorId);		} catch (NotFoundException e) {			// TODO Auto-generated catch block			e.printStackTrace();			color = -1;		}		return color;	}}