package com.example.adityasrivastava.cardview.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class CardSelectionListener implements RecyclerView.OnItemTouchListener{

    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;

    public interface OnItemClickListener {

        public void onItemClick(View view,int position);
    }

    public CardSelectionListener(Context context,OnItemClickListener listener){

        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        }
        );
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }



    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}