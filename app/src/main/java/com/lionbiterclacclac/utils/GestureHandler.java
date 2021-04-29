package com.lionbiterclacclac.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GestureHandler implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private OnTouchListener listener;

    private boolean isUp;

    public GestureHandler(Context context, OnTouchListener listener) {
        this.listener = listener;
        this.isUp = false;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                isUp = false;
                listener.onDown();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent event) {
               if(!isUp){
                   listener.onLongPress();
               }
                super.onLongPress(event);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            isUp = true;
            this.listener.onUp();
            return true;
        }

        return gestureDetector.onTouchEvent(motionEvent);
    }

    public interface OnTouchListener {
        void onDown();

        void onUp();

        void onLongPress();
    }
}
