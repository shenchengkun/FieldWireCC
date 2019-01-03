package com.example.cheng.fieldwirecc.View.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MyImageLoader {

    public static final String TAG = "MyImageLoader";

    private static MyImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;

    private LinkedList<Runnable> mTaskQueue;

    private Thread mBackstageThread;
    private Handler mBackstageThreadHandler;

    private Semaphore mBackstageThreadSemaphore;
    private Semaphore mBackstageThreadHandlerSemaphore = new Semaphore(0);

    private static final Object syncObject = new Object();

    public enum QueueType {FIFO, LIFO}

    private QueueType mType = QueueType.LIFO;

    private boolean isDiskCacheEnable = true;

    // UI Thread
    private Handler mUIHandler;

    public static MyImageLoader getInstance(int threadCount, QueueType type) {
        if (mInstance == null) {
            synchronized (syncObject) {
                if (mInstance == null) {
                    mInstance = new MyImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    public static MyImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (syncObject) {
                if (mInstance == null) {
                    mInstance = new MyImageLoader(DEFAULT_THREAD_COUNT, QueueType.LIFO);
                }
            }
        }
        return mInstance;
    }

    private MyImageLoader(int threadCount, QueueType type) {
        init(threadCount, type);
    }

    private void init(int threadCount, QueueType type) {
        initBackThread();

        // get the max available memory
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;

        // the size of bitmap can not over cacheMemory
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        // create thread pool that
        // reuses a fixed number of threads operating off a shared unbounded queue.
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;
        mBackstageThreadSemaphore = new Semaphore(threadCount);
    }

    private void initBackThread() {
        mBackstageThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mBackstageThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mThreadPool.execute(getTask());
                        try {
                            mBackstageThreadSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mBackstageThreadHandlerSemaphore.release();
                Looper.loop();
            }
        };
        mBackstageThread.start();
    }

    public void loadImage(String path, final ImageView imageView, boolean isFromNet) {
        imageView.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView iv = holder.imageView;
                    String path2 = holder.path;
                    if (iv.getTag().toString().equals(path2)) {
                        iv.setImageBitmap(bm);
                    }
                }
            };
        }

        Bitmap bitmap = getBitmapFromLruCache(path);
        if (bitmap != null) {
            refreshBitmap(path, imageView, bitmap);
        } else {
            addTask(buildTask(path, imageView, isFromNet));
        }
    }

    private void refreshBitmap(String path, final ImageView imageView, Bitmap bitmap) {
        Message msg = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bitmap;
        holder.path = path;
        holder.imageView = imageView;
        msg.obj = holder;
        mUIHandler.sendMessage(msg);
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if (mBackstageThreadHandler == null)
                mBackstageThreadHandlerSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mBackstageThreadHandler.sendEmptyMessage(0x110);
    }

    private Runnable getTask() {
        if (mType == QueueType.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == QueueType.LIFO) {
            return mTaskQueue.removeLast();
        }
        return null;
    }

    private Runnable buildTask(final String path, final ImageView imageView, final boolean isFromNet) {
        return new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                if (isFromNet) {
                    File file = getDiskCacheDir(imageView.getContext(), md5(path));
                    if (file.exists()) {
                        bitmap = loadImageFromLocal(file.getAbsolutePath(), imageView);
                        if (bitmap == null)
                            Log.d(TAG, "load image failed from local: " + path);
                    } else {
                        if (isDiskCacheEnable) {
                            boolean downloadState = DownloadImgUtils.downloadImageByUrl(path, file);
                            if (downloadState) {
                                bitmap = loadImageFromLocal(file.getAbsolutePath(), imageView);
                            }
                            if (bitmap == null)
                                Log.d(TAG, "download image failed to diskcache(" + path + ")");
                        } else {
                            bitmap = DownloadImgUtils.downloadImageByUrl(path, imageView);
                            if (bitmap == null)
                                Log.d(TAG, "download image failed to memory(" + path + ")");
                        }
                    }
                } else {
                    bitmap = loadImageFromLocal(path, imageView);
                }
                addBitmapToLruCache(path, bitmap);
                refreshBitmap(path, imageView, bitmap);
                mBackstageThreadSemaphore.release();
            }
        };
    }

    private Bitmap loadImageFromLocal(final String path, final ImageView imageView) {
        Bitmap bitmap = null;
        ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
        bitmap = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
        return bitmap;
    }

    protected void addBitmapToLruCache(String path, Bitmap bitmap) {
        if (getBitmapFromLruCache(path) == null) {
            if (bitmap != null)
                mLruCache.put(path, bitmap);
        }
    }


    protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        if (null == bitmap)
            Log.d(TAG, "options.inSampleSize = " + options.inSampleSize + ", " + path);
        return bitmap;
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (false && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    public String md5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex02(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }

        return sb.toString();

    }

    private class ImageBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
