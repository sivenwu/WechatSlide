package cn.wsy.wechatslide.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import cn.wsy.wechatslide.R;
import cn.wsy.wechatslide.interfaces.SlideMoveInterface;

/**
 * 滑动退出-仿IOS双向滑动
 * Created by wsy on 2016/3/4.
 */
public class SlideQuitView extends LinearLayout {

    /**
     * slide control
     */
    private ViewDragHelper mDragHelper;

    /**
     * All of the parentView
     */
    private View parentView;

    /**
     * left shadow
     */
    private Drawable mLeftShadow;

    /**
     * Current position for Edge
     */
    private int currentEdge;

    /**
     * Current position X
     */
    private int currentX;

    /**
     * The distance of View when moving
     */
    private int moveDistance;

    /**
     * This is original point of parentView ;
     */
    private Point originalPoint = new Point();

    /**
     * Whether to use slide quie
     */
    private boolean isSlideQuit = false;

    /**
     * Whether the view is moving
     */
    private boolean isMoving = false;

    /**
     * one by action doing here
     */
    private boolean isAvoidMoreAction = false;

    /**
     * quit position
     */
    private int QUIT_POSITION = 0;

    /**
     * shadow width
     */
    private final int SHADOW_WIDTH = 50;

    /**
     * MOVED_DISTANCE
     */
    private final int MOVED_RATE = 2;

    /**
     * MOVED_RESUME_DISTANCE
     */
    private int MOVED_RESUME_DISTANCE = 0;

    /**
     * Location Scope when view are moving
     */
    private final int LOCATION_SCOPE = 10;

    /**
     * Interface for view are moving
     */
    private SlideMoveInterface moveInterface;

    /**
     * Context here
     */
    private Context context;


    public SlideQuitView(Context context) {
        super(context);
        init(context);
    }

    public SlideQuitView(Context context, boolean isSlideQuit) {
        super(context);
        this.isSlideQuit = isSlideQuit;
        init(context);
    }


    public SlideQuitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mDragHelper = ViewDragHelper.create(this, 1.0f, new MyViewDragHelperCallBack());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mLeftShadow = getResources().getDrawable(R.drawable.left_shadow);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (parentView == null) {
            parentView = getChildAt(0);
            originalPoint.x = (int) parentView.getX();
            originalPoint.y = (int) parentView.getY();
            QUIT_POSITION = getWidth() / 3;
            MOVED_RESUME_DISTANCE = getWidth() / 4;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        parentView = getChildAt(0);
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

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);//开启自动滑动
            invalidate();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    private void drawShadow(Canvas canvas) {
        canvas.save();
        mLeftShadow.setBounds(moveDistance - SHADOW_WIDTH, 0, moveDistance, getHeight());
        mLeftShadow.draw(canvas);
        canvas.restore();
    }


    public void slideViewMoving(int dx) {
        if (dx == 0) {
        } else if (dx < 0) {
            offsetLeftAndRight(dx / MOVED_RATE);
        } else {
            if (!isAvoidMoreAction) {
                setX(-(MOVED_RESUME_DISTANCE));
                isAvoidMoreAction = !isAvoidMoreAction;
            }
            int x = (int) getX();
            if (x >= -LOCATION_SCOPE && x <= LOCATION_SCOPE) {
                isMoving = false;
                setX(0);
                return;
            }
            offsetLeftAndRight(dx / MOVED_RATE);
        }
        invalidate();
    }

    public void slideViewReseting() {
        isAvoidMoreAction = false;
        setX(0);
    }

    public void slideViewResumeing() {
        isAvoidMoreAction = false;
        setX(-(MOVED_RESUME_DISTANCE));
    }

    public void setMovingListener(SlideMoveInterface slideMoveInterface) {
        this.moveInterface = slideMoveInterface;
    }

    private class MyViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            return false;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            currentEdge = edgeFlags;
            mDragHelper.captureChildView(parentView, pointerId);

        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (isSlideQuit) {
                if (left <0) left = 0;
                currentX = left;
                return left;
            } else {
                if (isMoving) {//正在移动
                    currentX = left;
                    return left;
                }
                return 0;
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (currentEdge == ViewDragHelper.EDGE_LEFT) {//touch left
                moveDistance = left;
                if (moveInterface != null) {
                    moveInterface.moving(dx);
                }
                invalidate();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (currentEdge == ViewDragHelper.EDGE_LEFT) {
                if (isSlideQuit) {
                    if (currentX > QUIT_POSITION) {
                        if (moveInterface != null) moveInterface.resetPostion();
                        ((Activity) context).finish();
                        // ((Activity) context).overridePendingTransition(0, 0);
                    } else {
                        //回到原点
                        mDragHelper.settleCapturedViewAt(originalPoint.x, originalPoint.y);
                        if (moveInterface != null) moveInterface.resumePostion();
                    }
                } else {
                    mDragHelper.settleCapturedViewAt(originalPoint.x, originalPoint.y);
                }
                invalidate();
            }
        }
    }

}
