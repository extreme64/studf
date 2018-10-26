package com.example.mastermind.praktikumandroid;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mastermind on 27-Jun-18.
 */

public class OnSwipeListener implements View.OnTouchListener {


    private final GestureDetector getstDetec;

    public OnSwipeListener(Context context)
    {
        getstDetec = new GestureDetector(context, new GestureListener());

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return getstDetec.onTouchEvent(event);
    }


    public void onSwRight() {
        Log.i("swipe", "onSwRight(");
    }

    public void onSwLeft() {
        Log.i("swipe", "onSwLeft(");
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener
    {

        private static final int DIST_TRESH = 90;
        private static final int VELO_TRESH = 90;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float dX = e2.getX() - e1.getX();
            float dY = e2.getY() - e1.getY();

            if(Math.abs(dX) > Math.abs(dY) && Math.abs(dX) > DIST_TRESH && Math.abs(velocityX) > VELO_TRESH)
            {
                if(dX > 0)
                    onSwRight();
                else
                    onSwLeft();
                return true;
            }
            return false;
        }
    }

}
