package com.codejoyfun.imagemonitor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MonitorImageView extends ImageView implements MessageQueue.IdleHandler {

    private static final String IMAGE_TAG = "image";
    private static final String BACKGROUND_TAG = "background";
    private static final String TAG = "MonitorImageView";

    private String threadStack;

    public MonitorImageView(Context context) {
        super(context);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        addMonitor();
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        addMonitor();
    }

    private void addMonitor() {
        if (!ImageMonitor.isInit()) {
            Log.e(TAG, "ImageMonitor need init!");
            return;
        }
        threadStack = getThreadStack();
        Looper.myQueue().removeIdleHandler(this);
        Looper.myQueue().addIdleHandler(this);
    }

    private static String getThreadStack() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            sb.append(stackTraceElement.toString()).append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public boolean queueIdle() {
        try {
            Drawable drawable = getDrawable();
            Drawable background = getBackground();
            if (drawable != null) {
                check(drawable, IMAGE_TAG);
            }

            if (background != null) {
                check(background, BACKGROUND_TAG);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void check(Drawable drawable, String tag) {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        // 大小告警判断
        int imageSize = calculateImageSize(drawable);
        if (imageSize > ImageMonitor.maxAlarmImageSize()) {
            Log.e(TAG, "image load wrongfully," + tag + "size -> " + imageSize);
            dealWarning(drawableWidth, drawableHeight, imageSize, drawable);
        }
        // 宽高告警判断
        if (ImageMonitor.maxAlarmMultiple() * viewWidth < drawableWidth) {
            Log.e(TAG, "image load wrongfully, width of widget -> " + viewWidth + " , " + tag + "width of drawable -> " + drawableWidth);
            dealWarning(drawableWidth, drawableHeight, imageSize, drawable);
        }
        if (ImageMonitor.maxAlarmMultiple() * viewHeight < drawableHeight) {
            Log.e(TAG, "image load wrongfully, height of widget -> " + viewHeight + " , " + tag + "height of drawable -> " + drawableHeight);
            dealWarning(drawableWidth, drawableHeight, imageSize, drawable);
        }
    }

    private void dealWarning(int drawableWidth, int drawableHeight, int imageSize, Drawable drawable) {
        ImageMonitor.dealWarning(threadStack, drawableWidth, drawableHeight, imageSize, drawable);
    }

    private int calculateImageSize(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return bitmap.getByteCount();
        }
        int pixelSize = drawable.getOpacity() != PixelFormat.OPAQUE ? 4 : 2;
        return pixelSize * drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight();
    }


}
