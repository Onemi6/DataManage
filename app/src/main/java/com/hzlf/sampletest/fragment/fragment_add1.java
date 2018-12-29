package com.hzlf.sampletest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.activity.AddActivity;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.model.Info_add;
import com.hzlf.sampletest.model.Info_add1;
import com.hzlf.sampletest.model.Source;
import com.hzlf.sampletest.others.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class fragment_add1 extends Fragment {
    private ArrayAdapter adapter_renwuleibie, adapter_renwulaiyuan;
    private Button btn_next1;
    private List<String> renwulaiyuan = new ArrayList<>(), weituodanweidizhi = new ArrayList<>();
    private List<Source> sources = new ArrayList<>();
    private TextView tv_1, tv_6, tv_7, tv_8, tv_9, tv_10, tv_11;
    private AutoCompleteTextView actv_2;
    private EditText et_3, et_5;
    private Spinner spinner_4;
    private String number;
    private DBManage dbmanage;
    private AddActivity addActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        addActivity = (AddActivity) getActivity();
        number = addActivity.getNumber();
        View view = inflater.inflate(R.layout.add_layout1, container, false);
        dbmanage = new DBManage(getActivity());
        sources = dbmanage.findList_TaskSource();
        for (Source source : sources) {
            renwulaiyuan.add(source.getSOURCE_NAME());
            weituodanweidizhi.add(source.getADDR());
        }
        tv_1 = view.findViewById(R.id.text_chouyangdanbianhao);
        actv_2 = view.findViewById(R.id.text_renwulaiyuan);
        et_3 = view.findViewById(R.id.text_weituodanweidizhi);
        spinner_4 = view.findViewById(R.id.spinner_renwuleibie);
        et_5 = view.findViewById(R.id.text_renwushuxing);
        tv_6 = view.findViewById(R.id.text_danweimingcheng);
        tv_7 = view.findViewById(R.id.text_danweidizhi);
        tv_8 = view.findViewById(R.id.text_lianxiren);
        tv_9 = view.findViewById(R.id.text_dianhua);
        tv_10 = view.findViewById(R.id.text_chuanzhen);
        tv_11 = view.findViewById(R.id.text_youbian);
        adapter_renwuleibie = ArrayAdapter.createFromResource(getActivity(), R.array.array1_3,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_4.setAdapter(adapter_renwuleibie);/*需要一个适配器 初始化数据源--这个数据源去匹配文本框中输入的内容*/
        adapter_renwulaiyuan = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_list_item_1, renwulaiyuan);/* 将adpter与当前AutoCompleteTextView绑定*/
        actv_2.setAdapter(adapter_renwulaiyuan);
        initdata();
        actv_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = renwulaiyuan.indexOf(actv_2.getText().toString());
                et_3.setText(weituodanweidizhi.get(i));
            }
        });
        btn_next1 = view.findViewById(R.id.btn_next1);
        btn_next1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {/* TODO 自动生成的方法存根*/
                if (actv_2.getText().toString().equals(""))
                    ((MyApplication) addActivity.getApplication()).setAdd1(0);
                else ((MyApplication) addActivity.getApplication()).setAdd1(1);
                if (et_5.getText().toString().equals("")) et_5.setText("国抽");
                Info_add1 info_add1 = new Info_add1(tv_1.getText().toString(), actv_2.getText()
                        .toString(), et_3.getText().toString(), spinner_4.getSelectedItem()
                        .toString(), et_5.getText().toString(), tv_6.getText().toString(),
                        tv_7.getText().toString(), tv_8.getText().toString(), tv_9.getText()
                        .toString(), tv_10.getText().toString(), tv_11.getText().toString());
                ((MyApplication) addActivity.getApplication()).setInfoAdd1(info_add1);
                addActivity.nextFragment();
            }
        });
        return view;
    }

    public void initdata() {
        tv_1.setText(number);
        String info_add_str = addActivity.load();
        Gson gson = new Gson();
        Info_add info = gson.fromJson(info_add_str, Info_add.class);
        if (info != null && info.getInfo_add1() != null) {
            actv_2.setText(info.getInfo_add1().getValue2());
            et_3.setText(info.getInfo_add1().getValue3());
            SpinnerAdapter adapter = spinner_4.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
                if (info.getInfo_add1().getValue4().equals(adapter.getItem(i).toString())) {
                    spinner_4.setSelection(i, true);
                    break;
                }
            et_5.setText(info.getInfo_add1().getValue5());
        }
    }

    @Override
    public void onPause() {
        Info_add1 info_add1 = new Info_add1(tv_1.getText().toString(), actv_2.getText().toString
                (), et_3.getText().toString(), spinner_4.getSelectedItem().toString(),
                et_5.getText().toString(), tv_6.getText().toString(), tv_7.getText().toString(),
                tv_8.getText().toString(), tv_9.getText().toString(), tv_10.getText().toString(),
                tv_11.getText().toString());
        ((MyApplication) addActivity.getApplication()).setInfoAdd1(info_add1);
        Info_add info = new Info_add();
        info.setInfo_add1(((MyApplication) addActivity.getApplication()).getInfoAdd1());
        info.setInfo_add2(((MyApplication) addActivity.getApplication()).getInfoAdd2());
        info.setInfo_add3(((MyApplication) addActivity.getApplication()).getInfoAdd3());
        addActivity.save(info);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Info_add1 info_add1 = new Info_add1(tv_1.getText().toString(), actv_2.getText().toString
                (), et_3.getText().toString(), spinner_4.getSelectedItem().toString(),
                et_5.getText().toString(), tv_6.getText().toString(), tv_7.getText().toString(),
                tv_8.getText().toString(), tv_9.getText().toString(), tv_10.getText().toString(),
                tv_11.getText().toString());
        ((MyApplication) addActivity.getApplication()).setInfoAdd1(info_add1);
        Info_add info = new Info_add();
        info.setInfo_add1(((MyApplication) addActivity.getApplication()).getInfoAdd1());
        info.setInfo_add2(((MyApplication) addActivity.getApplication()).getInfoAdd2());
        info.setInfo_add3(((MyApplication) addActivity.getApplication())
                .getInfoAdd3());
        addActivity.save(info);
        super.onDestroy();
    }
}