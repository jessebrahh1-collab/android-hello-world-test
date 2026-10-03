package com.krustyk.cokbotfarm;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BotAccessibilityService extends AccessibilityService {

    public static BotAccessibilityService instance;

    private WindowManager windowManager;
    private View bubble;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        instance = this;
        windowManager =
                (WindowManager) getSystemService(WINDOW_SERVICE);

        LogStore.add(this, "Accessibility service ready");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Screen recognition will be connected here
        // during CoK calibration.
    }

    @Override
    public void onInterrupt() {
        LogStore.add(this, "Accessibility interrupted");
    }

    public boolean running() {
        return getSharedPreferences("engine", 0)
                .getBoolean("running", false);
    }

    public void stopNow() {
        getSharedPreferences("engine", 0)
                .edit()
                .putBoolean("running", false)
                .apply();

        LogStore.add(this, "EMERGENCY STOP");
    }

    public boolean tap(float x, float y) {

        if (!running()) {
            return false;
        }

        Path path = new Path();
        path.moveTo(x, y);

        GestureDescription.Builder builder =
                new GestureDescription.Builder();

        builder.addStroke(
                new GestureDescription.StrokeDescription(
                        path,
                        0,
                        75));

        return dispatchGesture(
                builder.build(),
                null,
                null);
    }

    public boolean swipe(
            float x1,
            float y1,
            float x2,
            float y2,
            long duration) {

        if (!running()) {
            return false;
        }

        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);

        GestureDescription.Builder builder =
                new GestureDescription.Builder();

        builder.addStroke(
                new GestureDescription.StrokeDescription(
                        path,
                        0,
                        Math.max(100, duration)));

        return dispatchGesture(
                builder.build(),
                null,
                null);
    }

    public void toggleBubble() {

        if (bubble != null) {
            windowManager.removeView(bubble);
            bubble = null;
            return;
        }

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(6, 6, 6, 6);

        TextView status = new TextView(this);
        status.setText(running() ? "RUNNING" : "PAUSED");
        status.setTextSize(12);

        box.addView(status);

        Button stop = new Button(this);
        stop.setText("STOP");

        stop.setOnClickListener(v -> {
            stopNow();
            status.setText("PAUSED");
        });

        box.addView(stop);

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        180,
                        160,
                        WindowManager.LayoutParams
                                .TYPE_ACCESSIBILITY_OVERLAY,
                        WindowManager.LayoutParams
                                .FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 8;
        params.y = 180;

        windowManager.addView(box, params);
        bubble = box;
    }

    @Override
    public void onDestroy() {

        if (bubble != null && windowManager != null) {
            windowManager.removeView(bubble);
        }

        bubble = null;
        instance = null;

        super.onDestroy();
    }
}
