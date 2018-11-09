package com.hzlf.sampletest.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.entityclass.MainInfo;

import java.util.List;

public class MaininfoAdapter extends ArrayAdapter<MainInfo> {
    private int resourceId;

    public MaininfoAdapter(Context context, int resource, List<MainInfo> objects) {
        super(context, resource, objects);
        // TODO 自动生成的构造函数存根
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainInfo maininfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.maininfo_status = (TextView) view
                    .findViewById(R.id.maininfo_status);

            viewHolder.maininfo_no = (TextView) view
                    .findViewById(R.id.maininfo_no);
            viewHolder.maininfo_number = (TextView) view
                    .findViewById(R.id.maininfo_number);
            viewHolder.maininfo_addtime = (TextView) view
                    .findViewById(R.id.maininfo_addtime);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.maininfo_status.setText(maininfo.getStatus());
        viewHolder.maininfo_no.setText(maininfo.getNo());
        viewHolder.maininfo_number.setText(maininfo.getNumber());
        viewHolder.maininfo_addtime.setText(maininfo.getAddtime());

        return view;
    }

    class ViewHolder {
        TextView maininfo_status;
        TextView maininfo_no;
        TextView maininfo_number;
        TextView maininfo_addtime;
    }
}