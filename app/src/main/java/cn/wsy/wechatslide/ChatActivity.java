package cn.wsy.wechatslide;

import android.os.Bundle;

import cn.wsy.wechatslide.interfaces.SlideMoveInterface;
import cn.wsy.wechatslide.utils.ActivityStackManager;

/**
 * Created by wsy on 2016/3/4.
 */
public class ChatActivity extends SlideActivity implements SlideMoveInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity,true);
        setSlideMoveListener(this);
    }

    @Override
    public void moving(int dx) {
        SlideActivity activity = (SlideActivity) ActivityStackManager.getInstance().getNextStack();
        activity.activityMoving(dx);
    }

    @Override
    public void resetPostion() {
        SlideActivity activity = (SlideActivity) ActivityStackManager.getInstance().getNextStack();
        activity.activityReseting();
    }

    @Override
    public void resumePostion() {
        SlideActivity activity = (SlideActivity) ActivityStackManager.getInstance().getNextStack();
        activity.activityResumeing();
    }
}
