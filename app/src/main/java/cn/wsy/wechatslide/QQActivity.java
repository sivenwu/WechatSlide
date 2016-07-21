package cn.wsy.wechatslide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import cn.wsy.wechatslide.interfaces.DragChangeListener;
import cn.wsy.wechatslide.views.DragLayout;

/**
 * Created by wsy on 2016/3/7.
 */
public class QQActivity extends AppCompatActivity{

    private DragLayout dragLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);
        dragLayout = (DragLayout) findViewById(R.id.draglayout);
        imageView = (ImageView) findViewById(R.id.image);

        dragLayout.setOnDragChangListener(new DragChangeListener() {
            @Override
            public void setOnDragChangeListener(int process) {
                imageView.setAlpha(process);
            }
        });
    }
}
