package cn.wsy.wechatslide.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity 堆栈管理
 * Created by wsy on 2016/3/4.
 */
public class ActivityStackManager {

    private static Stack activityStack;
    private static ActivityStackManager activityStackManager;

    //单例模式创建
    public static ActivityStackManager getInstance() {
        if (activityStackManager == null) {
            synchronized (ActivityStackManager.class) {
                if (activityStackManager == null) {
                    activityStackManager = new ActivityStackManager();
                }
            }
        }
        return activityStackManager;
    }

    private Stack checkActivityStack() {
        if (activityStack == null) {
            activityStack = new Stack();
            return activityStack;
        }
        return activityStack;
    }

    //进栈
    public void addStack(Activity activity) {
        if (activity != null) {
            checkActivityStack().add(activity);
        }
    }

    //出栈
    public void removeStack(Activity activity) {
        if (activity != null) {
            if (checkActivityStack().contains(activity)) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        }
    }

    //获取当前栈顶
    public Activity getStackTop() {
        return (Activity) checkActivityStack().lastElement();
    }

    public int getStackSize(){
        return checkActivityStack().size();
    }

    //获取当前栈顶下面那个Activity
    public Activity getNextStack() {
        Activity activity = null;
        if (activityStack.size() >1){
            activity = (Activity) checkActivityStack().get(activityStack.size() - 2);
        }
        return activity;
    }


}
