# CodeDemo
代码库代码

目录结构：
TeemoDemo为Demo项目的主项目，其他文件夹作为lib add到TeemoDemo项目中

每个功能的使用说明采用以下格式隔开：
Function Description Start
/************Dynamic GridView***********************/

/************Dynamic GridView***********************/
Function Description End


=====================================================================================================================
Function Description Start
/************ImageLoader***********************/
1、ImageLoader特点：
 ①多线程下载图片，图片可以来源于网络，文件系统，项目文件夹assets中以及drawable中等
 ②支持随意的匹配ImageLoader，例如线程池，图片下载器，内存缓存策略，硬盘缓存策略，图片显示选项以及其他的一些配置
 ③支持图片的内存缓存，文件系统缓存货值SD卡缓存
 ④支持图片下载过程的监听
 ⑤支持控件ImageView的大小对Bitmap进行裁剪，减少Bitmap占用过多的内存
 ⑥较好的控制图片的加载过程。例如暂停图片加载，重新开始加载图片，一般使用在ListView，GridView中，滑动过程中暂停加载图片，停止滑动的时候开始加载图片。
 ⑦提供较慢的网络下对图片进行加载
 
2、使用介绍：
	ImageLoaderConfiguration是图片加载器ImageLoader的配置参数。可以通过如下方式得到ImageLoaderConfiguration.
	ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
	
	//所有配置选项
	ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
	.memoryCacheExtraOptions(480, 800)// default = device screen dimensions
	.disCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
	.taskExecutor(...)
	.taskExecutorForCacheImages(...)
	.threadPoolSize(3)//default
	.threadPriority(Thread.Norm_PRIORITY - 1)// default
	.tasksProcessingOrder(QueueProcessingType.FIFO)// default
	.denyCacheImageMultipleSizesInMemory()
	.memorycache(new LruMemoryCache(2 * 1024 * 1024))
	.memoryCacheSize(2 * 1024 * 1024)
	.memoryCacheSizePercentage(13)// default
	.diskCache(new UnlimitedDiscCache(cacheDir))// default
	.diskCacheSize(50 * 1024 * 1024)
	.diskCacheFileCount(100)
	.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//default
	.imageDownloader(new BaseImageDownloader(context))
	.imageDecoder(new BaseImageDecoder())
	.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
	.writeDebugLogs()
	.build();

3、ImageLoader加载图片的方法
	displayImage(), loadImage(), loadImageSync()
	Example:
	ImageLoader.getInstance().loadImage(imageUrl, new ImageLoadingListener(){
		@Override  
         public void onLoadingStarted(String imageUri, View view) {  
                  
         }  

        @Override  
		public void onLoadingFailed(String imageUri, View view,  
				FailReason failReason) {  
			  
		}  
		  
		@Override  
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {  
			mImageView.setImageBitmap(loadedImage);  
		}  
		  
		@Override  
		public void onLoadingCancelled(String imageUri, View view) {  
			  
		}  
	});
	Maybe:
	ImageLoader.getInstance.loadImage(imageUrl, new SimpleImageLoadingListener(){
		@Override  
		public void onLoadingComplete(String imageUri, View view,  
				Bitmap loadedImage) {  
			super.onLoadingComplete(imageUri, view, loadedImage);  
			mImageView.setImageBitmap(loadedImage);  
		}  
	});
	
	如果需要设置Image的大小，需要初始化一个ImageSize的对象。
	Example：
	ImageSize mImageSize = new ImageSize(100, 100);
	ImageLoader.getInstance().loadImage(imageUrl, mImageSize, new SimpleImageLoadingListener(){
		@Override  
		public void onLoadingComplete(String imageUri, View view,  
				Bitmap loadedImage) {  
			super.onLoadingComplete(imageUri, view, loadedImage);  
			mImageView.setImageBitmap(loadedImage);  
		} 
	});
	
	使用DisplayImageOptions设置显示的图片：
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_stub)// resource or drawable
	.showImageForEmptyUri(R.drawalbe.ic_empty)// resource or drawable
	.showImageOnFail(R.drawable.ic_error)// resource or drawable
	.resetViewBeforeLoading(false)// default
	.delayBeforLoading(1000)
	.cacheInMemory(false)// default
	.cacheOnDisk(false)// default
	.preProcessor(...)
	.postProcessor(...)
	.extraForDownloader(...)
	.considerExifParams(false)// default
	.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// default
	.bitmapConfig(Bitmap.Config.ARGB_8888)// default
	.decodingOptions(...)
	.displayer(new SimpleBitmapDisplayer())// default
	.handler(new Handler())// default
	.build();
	即代码做如下修改：
	ImageSize mImageSize = new ImageSize(100, 100);
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.butmapConfig(Bitmap.Config.RGB_565)
	.build();
	ImageLoader.getInstance.loadImage(imageUrl, mImageSize, options, new SimpleImageLoadingListener() {
		@Override  
		public void onLoadingComplete(String imageUri, View view,  
				Bitmap loadedImage) {  
			super.onLoadingComplete(imageUri, view, loadedImage);  
			mImageView.setImageBitmap(loadedImage);  
		} 
	});
	DisplayImageOptions中有些方法是无效的，例如：showImageOnLoading,showImageForEmptyUri等
	
	
	使用displayImage()加载图片：
	DisplayImageOptions options = new DisplayImageOptions.Builer()
	.showImageOnLoading(R.drawable.ic_stub)
	.showImageOnFail(R.drawable.ic_error)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.build();
	ImageLoader.getInstance.displayImage(imageUrl, mImageView, options);
	
	
	使用ImageLoader加载本地文件。包括File，asset,drawable,contentProvider
	Example:
	String imagePath = "/mnt/sdcard/image.png";
	String imageUrl = Scheme.FILE.wrap(imagePath);
	imageLoader.displayImage(imageUrl, mImageView, options);
	其他：
	//图片来源于Content Provider
	String contentPrividerUrl = "content://media/external/audio/albumart/13";
	//图片来源于Assets
	String assetsUrl = Scheme.ASSETS.wrap("image.png");
	//图片来源于Drawable
	String drawableUrl = Scheme.DRAWABLE.wrap("R.drawable.img");
	
	
	GridView,ListView加载图片：
	listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFiling));
	
	
	
	OOM Error:
	如果该框架在使用的时候经常出现OOM的Error，可做如下修改：
	①减少线程池中线程的个数，在ImageLoaderConfigu中配置(一般在1-5)
	②在DisplayImageOptions选项中配置BitmapConfig为Bitmap.Config.RGB_565,因为默认值是ARGB_8888,使用RGB_565会比使用ARGB_8888少消耗两倍的内存
	
	③在ImageLoaderConfiguration中配置图片的内存缓存为memoryCache(new WwakMemoryCache())或者不使用内存缓存
	④在DisplayImageOptions选项中设置.imageScaleType(ImageScaleType.IN_SAMPLE_INT)或者.imageScaleType(ImageScaleType.EXACTLY)
/************ImageLoader***********************/
Function Description End
