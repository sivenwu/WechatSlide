package cn.wsy.wechatslide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.wsy.wechatslide.interfaces.SlideMoveInterface;
import cn.wsy.wechatslide.utils.ActivityStackManager;
import cn.wsy.wechatslide.views.SlideQuitView;

/**
 * slideQuit base activity
 * Created by wsy on 2016/3/4.
 */
public class SlideActivity extends AppCompatActivity {

    private SlideQuitView slideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addStack(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        slideView = new SlideQuitView(this);
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        childView.setLayoutParams(params);
        slideView.addView(childView);
        super.setContentView(slideView);
    }

    public void setContentView(int layoutResID, boolean isSlideQuit) {
        slideView = new SlideQuitView(this, isSlideQuit);
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        childView.setLayoutParams(params);
        slideView.addView(childView);
        super.setContentView(slideView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SlideActivity nextActivity = (SlideActivity) ActivityStackManager.getInstance().getNextStack();
        if (nextActivity !=null )
        nextActivity.activityReseting();
        ActivityStackManager.getInstance().removeStack(this);
    }

    public void activityMoving(int dx) {
        slideView.slideViewMoving(dx);
    }

    public void activityReseting() {
        slideView.slideViewReseting();
    }

    public void activityResumeing() {
        slideView.slideViewResumeing();
    }

    public void setSlideMoveListener(SlideMoveInterface slideMoveInterface) {
        slideView.setMovingListener(slideMoveInterface);
    }


}
