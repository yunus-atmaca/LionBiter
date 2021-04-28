package com.lionbiterclacclac.utils;

import android.view.MotionEvent;
import android.view.View;

public class TouchListener implements View.OnTouchListener {

    private onTouchListener listener;

    public TouchListener(onTouchListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.listener.onPressed();
                return true;
            case MotionEvent.ACTION_UP:
                this.listener.onReleased();
                return true;
        }
        return false;
    }

    public  interface  onTouchListener{
        void onPressed();
        void onReleased();
    }

}
