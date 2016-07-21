package cn.wsy.wechatslide.interfaces;

/**
 * 滑动监控状态接口
 * Created by wsy on 2016/3/4.
 */
public interface SlideMoveInterface {

    /**
     * 上一层Activity正在移动的时候回调
     *
     * @param dx
     */
    public void moving(int dx);

    /**
     * 上一层Activity销毁的时候回调
     */
    public void resetPostion();

    /**
     * 上一层Activity没有超过一半的时候回调
     */
    public void resumePostion();

}
