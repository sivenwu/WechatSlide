package cn.wsy.wechatslide.views;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.wsy.wechatslide.interfaces.DragChangeListener;

/**
 * Created by wsy on 2016/3/7.
 */
public class DragLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private MyViewDragHelperCallBack callBack;
    private Context mContext;

    //View
    /**
     * 抽屉布局ViewGroup
     **/
    private ViewGroup dragView;
    /**
     * 主布局布局ViewGroup
     **/
    private ViewGroup mainParent;

    /**
     * 拖动距离
     */
    private int dragDistance = 0;

    /**
     * ViewGroup 宽度
     */
    private int viewWidth = 0;

    /**
     * ViewGroup高度
     */
    private int viewHeight = 0;

    /**
     * 主布局移动距离
     */
    private int mainMoveDx = 0;

    /**
     * 抽屉布局移动距离
     */
    private int dragMoveDx = 0;

    /**
     * 滑动监听
     */
    private DragChangeListener listener;

    public DragLayout(Context context) {
        super(context);
        init(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setOnDragChangListener(DragChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getViewGroup();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        dragDistance = (int) (viewWidth * 0.8f);
    }

    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);//开启自动滑动
            invalidate();
        }
    }

    private void init(Context context) {
        this.mContext = context;
        callBack = new MyViewDragHelperCallBack();
        mDragHelper = ViewDragHelper.create(this, 1.0f, callBack);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private void getViewGroup() {
        if (dragView == null && mainParent == null) {
            dragView = (ViewGroup) getChildAt(0);
            mainParent = (ViewGroup) getChildAt(1);
        }
    }

    private int updateLeft(int left) {
        int newleft = 0;
        if (left < 0) {
            newleft = 0;
        } else if (left > dragDistance) {
            newleft = dragDistance;
        } else {
            newleft = left;
        }
        return newleft;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    private void openDragView() {
        int newLeft = dragDistance;
        if (mDragHelper.smoothSlideViewTo(mainParent, newLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            mainParent.layout(newLeft, 0, newLeft + viewWidth, 0 + viewHeight);
        }
    }

    private void closeDragView() {
        int newLeft = 0;
        if (mDragHelper.smoothSlideViewTo(mainParent, newLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            mainParent.layout(newLeft, 0, newLeft + viewWidth, 0 + viewHeight);
        }
    }

    private class MyViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //限制主布局不能像左移动 并且不能超过左边抽屉布局的宽度
            getViewGroup();
            if (child == mainParent) {
                left = updateLeft(left);
            }
            return left;
        }

        /**
         * 看笔记 Layout
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            int newLeft = left;
            //拖动如果是抽屉布局，强制性回到原来位置
            if (changedView == dragView) {
                newLeft = mainParent.getLeft() + dx;//dx滑动距离 getleft相对父布局距离
                newLeft = updateLeft(newLeft);
                //当左面板移动之后，在强制放回去
                dragView.layout(0, 0, 0 + viewWidth, 0 + viewHeight);//l t r b
                mainParent.layout(newLeft, 0, newLeft + viewWidth, 0 + viewHeight);
                dragMoveDx = dx;
                mainMoveDx = 0;
            } else if (changedView == mainParent) {
                mainMoveDx = dx;
                dragMoveDx = 0;
            }
            if (listener != null) {
                int changeLeft = mainParent.getLeft();
                changeLeft = (255 * changeLeft)/dragDistance;
                listener.setOnDragChangeListener(255-changeLeft);
            }
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //     Log.i("wusy", " "+xvel);//横向速率
            if (xvel > 0 && mainMoveDx > 0 && dragMoveDx == 0) {
                openDragView();
            } else if (xvel < 0 && dragMoveDx < 0 && mainMoveDx == 0) {
                closeDragView();
            } else if (xvel < 0 && mainMoveDx < 0 && dragMoveDx == 0) {
                closeDragView();
            } else if (mainMoveDx > 0 && dragMoveDx == 0) {
                openDragView();
            } else if (dragMoveDx > 0 && mainMoveDx == 0) {
                openDragView();
            } else if (mainMoveDx < 0 && dragMoveDx == 0 && mainParent.getLeft() > getWidth() / 2) {
                openDragView();
            } else if (dragMoveDx < 0 && mainMoveDx == 0 && mainParent.getLeft() > getWidth() / 2) {
                openDragView();
            } else {
                closeDragView();
                 Log.i("wusy", "other:" + xvel + " " + mainMoveDx + " " + dragMoveDx);
            }

        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }
    }


}
