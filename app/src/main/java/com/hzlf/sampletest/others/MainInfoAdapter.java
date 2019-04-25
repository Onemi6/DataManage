package com.hzlf.sampletest.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.model.MainInfo;

import java.util.List;

public class MainInfoAdapter extends ArrayAdapter<MainInfo> {
    private int resourceId;

    public MainInfoAdapter(Context context, int resource, List<MainInfo> objects) {
        super(context, resource, objects);
        // TODO 自动生成的构造函数存根
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainInfo mainInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.mainInfo_num = view.findViewById(R.id.mainInfo_num);
            viewHolder.mainInfo_status = view.findViewById(R.id.maininfo_status);
            viewHolder.mainInfo_no = view.findViewById(R.id.maininfo_no);
            viewHolder.mainInfo_number = view.findViewById(R.id.maininfo_number);
            viewHolder.mainInfo_addTime = view.findViewById(R.id.maininfo_addtime);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mainInfo_num.setText("" + (position + 1));
        viewHolder.mainInfo_status.setText(mainInfo.getStatus());
        viewHolder.mainInfo_no.setText(mainInfo.getNo());
        viewHolder.mainInfo_number.setText(mainInfo.getNumber());
        viewHolder.mainInfo_addTime.setText(mainInfo.getAddtime());
        return view;
    }

    class ViewHolder {
        TextView mainInfo_num;
        TextView mainInfo_status;
        TextView mainInfo_no;
        TextView mainInfo_number;
        TextView mainInfo_addTime;
    }
}