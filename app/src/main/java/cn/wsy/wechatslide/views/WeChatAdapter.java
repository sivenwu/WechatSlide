package cn.wsy.wechatslide.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.wsy.wechatslide.R;


/**
 * Created by wsy on 2016/3/4.
 */
public class WeChatAdapter extends BaseAdapter{

    private List<String> datas = new ArrayList<>();
    private Context mContext;

    public WeChatAdapter(List<String> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_view_layout,null);
        return convertView;
    }
}
