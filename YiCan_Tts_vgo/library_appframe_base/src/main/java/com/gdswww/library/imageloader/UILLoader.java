package com.gdswww.library.imageloader;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
  * @ClassName: UILLoader 
  * @Description: ImageView Loading 图片加载
  * @author shihuanzhang 2335946896@qq_com
  * @date 2015年5月21日
 */
public class UILLoader {
	private static UILLoader instance;
	static Context context;
	private static ImageLoader im;
	private UILLoader(){}
	/**
	 * 
	  * @Title: getInstance 
	  * @Description: 初始化对象 
	  * @param c 上下文对象
	  * @param cacheDir 缓存目录
	 */
	@SuppressWarnings("unused")
	public static UILLoader getInstance(Context c,String cacheDir){
		if(instance==null){
			context = c;
			instance = new UILLoader(); 
			if (false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
			}
			im = ImageLoader.getInstance();
			initImageLoader(c,cacheDir);
		}
		return instance;
	}
	
	
	public static ImageLoader getImageLoaderObject(){
		if(instance == null){
			throw new IllegalStateException("instance is null");
		}
		return im;
	}
	/**
	 * 
	  * @Title: initImageLoader 
	  * @Description: 首次必须初始化ImageLoader对象
	  * @param @param context
	  * @param @param cacheDir 参数描述 
	 */
	private static void initImageLoader(Context context,String cacheDir) {
		/*
		 This configuration tuning is custom. You can tune every option, you may tune some of them,
		 or you can create default configuration by
		 ImageLoaderConfiguration.createDefault(this);
		 method.
		*/
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		//config.memoryCache(new WeakMemoryCache());
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		config.diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(context, cacheDir)));//自定义图片缓存路径
		im.init(config.build());
	}
	
	
	/**
	 * 获取ImageLoader对象
	 */
	public ImageLoader getULObject(){
		return im;
	}
	
	/**
	  * @Title: getOption 
	  * @Description: 构建ImgaeLoader显示配置
	  * @param EmptyUriId 当地址为空的时候显示的图片
	  * @param failId 当加载失败时候显示的图片 
	  * @param loadingId 当加载中时候显示的图片 
	  * @param resetViewBeforeLoading 加载前是否要重设视图
	  * @return DisplayImageOptions
	  * 
	 */
	public DisplayImageOptions getOption(int EmptyUriId,int failId,
			int loadingId,boolean resetViewBeforeLoading){
		return new DisplayImageOptions.Builder()
		.showImageForEmptyUri(EmptyUriId)
		.showImageOnFail(failId)
		.showImageOnLoading(loadingId)
		.resetViewBeforeLoading(resetViewBeforeLoading)
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}
	
	/**
	 * "http://site.com/image.png" // from web
	 * @param uri
	 * @param iv
	 */
	public void web(String uri,ImageView iv){
		im.displayImage(uri, iv);
	}
	
	public void web(String uri,ImageView iv,DisplayImageOptions options){
		im.displayImage(uri, iv,options);
	}
	
	/**
	 * 
	  * @Title: showImage 
	  * @Description: 显示图 片
	  * @param uri 地址
	  * @param iv ImageView对象
	  * @param options 显示配置
	  * @param listener 加载监听器 
	 */
	public void web(String uri,ImageView iv,DisplayImageOptions options,
			SimpleImageLoadingListener listener){
		im.displayImage(uri, iv,options,listener);
	}
	
	
	/**
	 * "file:///mnt/sdcard/image.png" // from SD card <br/>
	   "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
	 * @param filePath
	 * @param iv
	 */
	public void file(String filePath,ImageView iv){
		im.displayImage("file://"+filePath, iv);
	}
	
	
	
	public void file(String filePath,ImageView iv,DisplayImageOptions options){
		im.displayImage("file://"+filePath, iv,options);
	}

	 
	public void file(String filePath,ImageView iv,DisplayImageOptions options,
			SimpleImageLoadingListener listener){
		im.displayImage("file://"+filePath, iv,options,listener);
	}
	
	/**
	 * "drawable://" + R.drawable.img // from drawables (non-9patch images)
	 * @param drawable
	 * @param iv
	 */
	public void drawable(int drawable,ImageView iv){
		im.displayImage("drawable://"+drawable, iv);
	}
	
	
	public void drawable(int drawable,ImageView iv,DisplayImageOptions options){
		im.displayImage("drawable://"+drawable, iv,options);
	}

	 
	public void drawable(int drawable,ImageView iv,DisplayImageOptions options,
			SimpleImageLoadingListener listener){
		im.displayImage("drawable://"+drawable, iv,options,listener);
	}
	/**
	 * "content://media/external/images/media/13" // from content provider
	 * @param contentURI
	 * @param iv
	 */
	public void contentURI(String contentURI,ImageView iv){
		im.displayImage(contentURI, iv);
	}
	
	
	public void contentURI(String contentURI,ImageView iv,DisplayImageOptions options){
		im.displayImage(contentURI, iv,options);
	}

	 
	public void contentURI(String contentURI,ImageView iv,DisplayImageOptions options,
			SimpleImageLoadingListener listener){
		im.displayImage(contentURI, iv,options,listener);
	}
	
	
	
	/**
	 * "assets://image.png" // from assets
	 * @param assetsPath
	 * @param iv
	 */
	public void assets(String assetsPath,ImageView iv){
		im.displayImage(assetsPath, iv);
	}
	
	
	public void assets(String assetsPath,ImageView iv,DisplayImageOptions options){
		im.displayImage(assetsPath, iv,options);
	}

	 
	public void assets(String assetsPath,ImageView iv,DisplayImageOptions options,
			SimpleImageLoadingListener listener){
		im.displayImage(assetsPath, iv,options,listener);
	}
	
	
	
	/**
	 * Load image, decode it to Bitmap and return Bitmap synchronously
	 */
	public Bitmap getBitmapByUri(String imageUri){
		return im.loadImageSync(imageUri);
	}
	
	public Bitmap getBitmapByUri(String imageUri,ImageSize targetSize){
		return im.loadImageSync(imageUri,targetSize);
	}
	
	public Bitmap getBitmapByUri(String imageUri,DisplayImageOptions options){
		return im.loadImageSync(imageUri,options);
	}
	
	public void loadImage(String imageUri,ImageSize targetSize,DisplayImageOptions options
			,SimpleImageLoadingListener listener){
		 im.loadImage(imageUri,targetSize,options,listener);
	}
	
	public void loadImage(String imageUri,DisplayImageOptions options
			,SimpleImageLoadingListener listener){
		 im.loadImage(imageUri,options,listener);
	}
}
