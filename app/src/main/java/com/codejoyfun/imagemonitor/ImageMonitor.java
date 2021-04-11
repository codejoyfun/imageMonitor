package com.codejoyfun.imagemonitor;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageMonitor {

    private static final int MAX_ALARM_IMAGE_SIZE = 2 * 1024 * 1024;//2MB
    private static final int MAX_ALARM_MULTIPLE = 2;//2倍
    private static final String TAG = "ImageMonitor";

    private static volatile boolean isInit = false;
    private int maxAlarmImageSize;
    private int maxAlarmMultiple;
    private Action debugAction;
    private Action releaseAction;

    private static ImageMonitor INSTANCE;

    public static boolean isInit() {
        return isInit;
    }

    public static void init(Config config) {
        if (isInit) {
            Log.e(TAG, "ImageMonitor already init!");
            return;
        }
        isInit = true;
        INSTANCE = new ImageMonitor(config);
    }

    public static int maxAlarmImageSize() {
        return INSTANCE.maxAlarmImageSize;
    }

    public static int maxAlarmMultiple() {
        return INSTANCE.maxAlarmMultiple;
    }

    public static void debugAction(String threadStack, int drawableWidth, int drawableHeight, int imageSize, Drawable drawable) {
        if (INSTANCE.debugAction != null) {
            INSTANCE.debugAction.run(threadStack, drawableWidth, drawableHeight, imageSize, drawable);
        }
    }

    public static void releaseAction(String threadStack, int drawableWidth, int drawableHeight, int imageSize, Drawable drawable) {
        if (INSTANCE.releaseAction != null) {
            INSTANCE.releaseAction.run(threadStack, drawableWidth, drawableHeight, imageSize, drawable);
        }
    }


    private ImageMonitor(Config config) {
        this.maxAlarmImageSize = config.maxAlarmImageSize;
        this.maxAlarmMultiple = config.maxAlarmMultiple;
        this.debugAction = config.debugAction;
        this.releaseAction = config.releaseAction;
    }

    static class Config {
        private static int maxAlarmImageSize = MAX_ALARM_IMAGE_SIZE;
        private static int maxAlarmMultiple = MAX_ALARM_MULTIPLE;
        private static Action debugAction;
        private static Action releaseAction;

        public Config maxAlarmImageSize(int maxAlarmImageSize) {
            this.maxAlarmImageSize = maxAlarmImageSize;
            return this;
        }

        public Config maxAlarmMultiple(int maxAlarmMultiple) {
            this.maxAlarmMultiple = maxAlarmMultiple;
            return this;
        }

        public Config debugAction(Action debugAction) {
            this.debugAction = debugAction;
            return this;
        }

        public Config releaseAction(Action releaseAction) {
            this.releaseAction = releaseAction;
            return this;
        }
    }

    public interface Action {
        void run(String threadStack, int drawableWidth, int drawableHeight, int imageSize, Drawable drawable);
    }
}
