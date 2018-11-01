package com.hzlf.sampletest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.activity.AddActivity;
import com.hzlf.sampletest.activity.CaptureActivity;
import com.hzlf.sampletest.activity.MainActivity;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.AAQI;
import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Info_add3;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.others.DatePickerDialog;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.UsedPath;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fragment_add3 extends Fragment {

    private Spinner sp_2, sp_3, sp_4, sp_6, sp_10, sp_18, sp_19, sp_20, sp_21;
    private ArrayAdapter adapter_yangpinleixing, adapter_yangpinlaiyuan,
            adapter_yangpinshuxing, adapter_baozhuangfenlei,
            adapter_riqileixing, adapter_chouyangfangshi,
            adapter_yangpinxingtai, adapter_yangpinbaozhuang,
            adapter_chucuntiaojian;
    private EditText et_1, et_5, et_7, et_8, et_9, et_12, et_13, et_14, et_16, et_22,
            et_23, et_24, et_25, et_26, et_27, et_28, et_29;
    private TextView tv_11, tv_17;
    private RadioGroup radio_15;
    private Button btn_back3, btn_clear3, btn_save, btn_scanning;
    private int mYear, mMonth, mDay;
    private String str_chukou, str_shengchanriqi, str_chouyangriqi, number;
    private DBManage dbmanage;
    private AddActivity addActivity;
    private Info_add info_add_now;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addActivity = (AddActivity) getActivity();
        View view = inflater.inflate(R.layout.add_layout3, container, false);
        dbmanage = new DBManage(addActivity);
        number = addActivity.getNumber();

        /*--------------------------------------------------------------------*/
        et_1 = view.findViewById(R.id.input_yangpinmingcheng);
        sp_2 = view.findViewById(R.id.spinner_yangpinleixing);
        sp_3 = view.findViewById(R.id.spinner_yangpinlaiyuan);
        sp_4 = view.findViewById(R.id.spinner_yangpinshuxing);
        et_5 = view.findViewById(R.id.input_yangpinshangbiao);
        sp_6 = view.findViewById(R.id.spinner_baozhuangfenlei);
        et_7 = view.findViewById(R.id.input_guigexinghao);
        et_8 = view.findViewById(R.id.input_zhiliangdengji);
        et_9 = view.findViewById(R.id.input_yangpintiaoma);
        sp_10 = view.findViewById(R.id.spinner_riqileixing);
        tv_11 = view.findViewById(R.id.text_shengchanriqi);
        et_12 = view.findViewById(R.id.input_baozhiqi);
        et_13 = view.findViewById(R.id.input_chanpinpihao);
        et_14 = view.findViewById(R.id.input_yangpindanjia);
        radio_15 = view.findViewById(R.id.radio_chukou);
        et_16 = view.findViewById(R.id.input_yuanchandi);
        tv_17 = view.findViewById(R.id.text_chouyangriqi);
        sp_18 = view.findViewById(R.id.spinner_chouyangfangshi);
        sp_19 = view.findViewById(R.id.spinner_yangpinxingtai);
        sp_20 = view.findViewById(R.id.spinner_yangpinbaozhuang);
        sp_21 = view.findViewById(R.id.spinner_chucuntiaojian);
        et_22 = view.findViewById(R.id.input_zhixingbiaozhun);
        et_23 = view.findViewById(R.id.input_chouyangshuliangdanwei);
        et_24 = view.findViewById(R.id.input_chouyangjishu);
        et_25 = view.findViewById(R.id.input_beiyangshuliang);
        et_26 = view.findViewById(R.id.input_chouyangshuliang);
        et_27 = view.findViewById(R.id.input_chouyangren);
        et_28 = view.findViewById(R.id.input_yangpinxukezheng);
        et_29 = view.findViewById(R.id.input_beizhu);

        btn_scanning = view.findViewById(R.id.btn_scanning);
        btn_save = view.findViewById(R.id.btn_save);
        btn_clear3 = view.findViewById(R.id.btn_clear3);
        btn_back3 = view.findViewById(R.id.btn_back3);

        /*--------------------------------------------------------------------*/
        adapter_yangpinleixing = ArrayAdapter.createFromResource(getActivity(),
                R.array.array3_2, android.R.layout.simple_spinner_dropdown_item);
        sp_2.setAdapter(adapter_yangpinleixing);

        adapter_yangpinlaiyuan = ArrayAdapter.createFromResource(getActivity(),
                R.array.array3_3, android.R.layout.simple_spinner_dropdown_item);
        sp_3.setAdapter(adapter_yangpinlaiyuan);

        adapter_yangpinshuxing = ArrayAdapter.createFromResource(getActivity(),
                R.array.array3_4, android.R.layout.simple_spinner_dropdown_item);
        sp_4.setAdapter(adapter_yangpinshuxing);

        adapter_baozhuangfenlei = ArrayAdapter.createFromResource(
                getActivity(), R.array.array3_6,
                android.R.layout.simple_spinner_dropdown_item);
        sp_6.setAdapter(adapter_baozhuangfenlei);

        adapter_riqileixing = ArrayAdapter.createFromResource(getActivity(),
                R.array.array3_10, android.R.layout.simple_spinner_dropdown_item);
        sp_10.setAdapter(adapter_riqileixing);

        adapter_chouyangfangshi = ArrayAdapter.createFromResource(
                getActivity(), R.array.array3_18,
                android.R.layout.simple_spinner_dropdown_item);
        sp_18.setAdapter(adapter_chouyangfangshi);

        adapter_yangpinxingtai = ArrayAdapter.createFromResource(getActivity(),
                R.array.array3_19, android.R.layout.simple_spinner_dropdown_item);
        sp_19.setAdapter(adapter_yangpinxingtai);

        adapter_yangpinbaozhuang = ArrayAdapter.createFromResource(
                getActivity(), R.array.array3_20,
                android.R.layout.simple_spinner_dropdown_item);
        sp_20.setAdapter(adapter_yangpinbaozhuang);

        adapter_chucuntiaojian = ArrayAdapter.createFromResource(getActivity(),
                R.array.array3_21, android.R.layout.simple_spinner_dropdown_item);
        sp_21.setAdapter(adapter_chucuntiaojian);
        /*--------------------------------------------------------------------*/

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        str_chouyangriqi = str_shengchanriqi = new SimpleDateFormat(
                "yyyy-MM-dd").format(Calendar.getInstance().getTime());

        tv_11.setText(str_shengchanriqi);
        tv_11.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存
                new DatePickerDialog(addActivity, 0,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker DatePicker,
                                                  int Year, int MonthOfYear, int DayOfMonth) {
                                str_shengchanriqi = Year
                                        + "-"
                                        + String.format("%02d",
                                        (MonthOfYear + 1)) + "-"
                                        + String.format("%02d", DayOfMonth);
                                tv_11.setText(str_shengchanriqi);
                            }
                        }, mYear, mMonth, mDay, true).show();
            }
        });

        tv_17.setText(str_chouyangriqi);
        tv_17.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存
                new DatePickerDialog(addActivity, 0,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker DatePicker,
                                                  int Year, int MonthOfYear, int DayOfMonth) {
                                str_chouyangriqi = Year
                                        + "-"
                                        + String.format("%02d",
                                        (MonthOfYear + 1)) + "-"
                                        + String.format("%02d", DayOfMonth);
                                tv_17.setText(str_chouyangriqi);
                            }
                        }, mYear, mMonth, mDay, true).show();
            }
        });
        initdata();

        btn_scanning.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                Intent intent_scan = new Intent(addActivity,
                        CaptureActivity.class);
                startActivityForResult(intent_scan, 3);
            }
        });

        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根

                if (et_1.getText().toString().equals("")
                        || et_5.getText().toString().equals("")
                        || et_7.getText().toString().equals("")
                        || et_8.getText().toString().equals("")
                        || et_9.getText().toString().equals("")
                        || et_12.getText().toString().equals("")
                        || et_14.getText().toString().equals("")
                        || et_22.getText().toString().equals("")
                        || et_23.getText().toString().equals("")
                        || et_24.getText().toString().equals("")
                        || et_25.getText().toString().equals("")
                        || et_26.getText().toString().equals("")
                        || et_27.getText().toString().equals("")
                        || et_28.getText().toString().equals("")) {
                    ((MyApplication) addActivity.getApplication()).setAdd3(0);
                    Toast.makeText(addActivity, "带*的为必填项", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (et_9.getText().toString().equals("")) {
                        et_9.setText("/");
                    }
                    if (et_13.getText().toString().equals("")) {
                        et_13.setText("/");
                    }
                    if (et_16.getText().toString().equals("")) {
                        et_16.setText("/");
                    }
                    if (et_29.getText().toString().equals("")) {
                        et_29.setText("/");
                    }
                    for (int i = 0; i < radio_15.getChildCount(); i++) {
                        RadioButton r = (RadioButton) radio_15
                                .getChildAt(i);
                        if (r.isChecked()) {
                            str_chukou = r.getText().toString();
                            break;
                        }
                    }
                    Info_add3 info_add3 = new Info_add3(
                            et_1.getText().toString(),
                            sp_2.getSelectedItem().toString(),
                            sp_3.getSelectedItem().toString(),
                            sp_4.getSelectedItem().toString(),
                            et_5.getText().toString(),
                            sp_6.getSelectedItem()
                                    .toString(),
                            et_7.getText().toString(),
                            et_8.getText().toString(),
                            et_9.getText().toString(),
                            sp_10.getSelectedItem().toString(),
                            tv_11.getText().toString(),
                            et_12.getText().toString(),
                            et_13.getText().toString(),
                            et_14.getText().toString(),
                            str_chukou,
                            et_16.getText().toString(),
                            tv_17.getText().toString(),
                            sp_18.getSelectedItem()
                                    .toString(),
                            sp_19.getSelectedItem().toString(),
                            sp_20.getSelectedItem()
                                    .toString(), sp_21
                            .getSelectedItem().toString(), et_22
                            .getText().toString(), et_23.getText()
                            .toString(), et_24.getText()
                            .toString(), et_25.getText()
                            .toString(), et_26.getText()
                            .toString(), et_27.getText()
                            .toString(), et_28.getText().toString(),
                            et_29.getText().toString(), null);
                    ((MyApplication) addActivity.getApplication()).setAdd3(1);
                    ((MyApplication) addActivity.getApplication())
                            .setInfoAdd3(info_add3);
                    if (((MyApplication) addActivity.getApplication())
                            .getAdd1() == 1
                            && ((MyApplication) addActivity.getApplication())
                            .getAdd2() == 1
                            && ((MyApplication) addActivity.getApplication())
                            .getAdd3() == 1) {
                        dbmanage.addInfo(((MyApplication) addActivity
                                        .getApplication()).getNo(),
                                ((MyApplication) addActivity.getApplication())
                                        .getInfoAdd1(),
                                ((MyApplication) addActivity.getApplication())
                                        .getInfoAdd2(),
                                ((MyApplication) addActivity.getApplication())
                                        .getInfoAdd3());

                        dbmanage.updateNumber(number, 1, 0, 0);
                        dbmanage.updateSign(number, 0);

                        Toast.makeText(addActivity, "保存成功!", Toast.LENGTH_SHORT)
                                .show();
                        info_add_now = new Info_add();
                        info_add_now.setInfo_add1(((MyApplication) addActivity
                                .getApplication()).getInfoAdd1());
                        info_add_now.setInfo_add2(((MyApplication) addActivity
                                .getApplication()).getInfoAdd2());
                        info_add_now.setInfo_add3(((MyApplication) addActivity
                                .getApplication()).getInfoAdd3());

                        Intent intent_main = new Intent();
                        intent_main.setClass(addActivity, MainActivity.class);
                        addActivity.finish();// 结束当前活动
                        startActivity(intent_main);
                    } else {
                        Toast.makeText(addActivity, "带*的为必填项",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_clear3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_1.setText("");
                sp_2.setSelection(0);
                sp_3.setSelection(0);
                sp_4.setSelection(0);
                et_5.setText("");
                sp_6.setSelection(0);
                et_7.setText("");
                et_8.setText("");
                et_9.setText("");
                sp_10.setSelection(0);
                tv_11.setText(str_shengchanriqi);
                et_12.setText("");
                et_13.setText("");
                et_14.setText("");
                radio_15.check(R.id.radio_chukou_fou);
                et_16.setText("");
                tv_17.setText(str_chouyangriqi);
                sp_18.setSelection(0);
                sp_19.setSelection(0);
                sp_20.setSelection(0);
                sp_21.setSelection(0);
                et_22.setText("");
                et_23.setText("");
                et_24.setText("");
                et_25.setText("");
                et_26.setText("");
                et_27.setText("");
                et_28.setText("");
                et_29.setText("");
            }
        });

        btn_back3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                addActivity.backFragment();
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        for (int i = 0; i < radio_15.getChildCount(); i++) {
            RadioButton r = (RadioButton) radio_15.getChildAt(i);
            if (r.isChecked()) {
                str_chukou = r.getText().toString();
                break;
            }
        }
        Info_add3 info_add3 = new Info_add3(et_1.getText().toString(),
                sp_2.getSelectedItem().toString(),
                sp_3.getSelectedItem().toString(),
                sp_4.getSelectedItem().toString(), et_5
                .getText().toString(), sp_6
                .getSelectedItem().toString(), et_7.getText()
                .toString(), et_8.getText().toString(), et_9
                .getText().toString(), sp_10.getSelectedItem().toString(),
                tv_11.getText().toString(), et_12
                .getText().toString(), et_13.getText().toString(),
                et_14.getText().toString(), str_chukou, et_16.getText()
                .toString(),
                tv_17.getText().toString(), sp_18.getSelectedItem()
                .toString(), sp_19.getSelectedItem()
                .toString(), sp_20.getSelectedItem()
                .toString(), sp_21.getSelectedItem()
                .toString(), et_22.getText().toString(),
                et_23.getText().toString(), et_24.getText()
                .toString(), et_25.getText().toString(),
                et_26.getText().toString(), et_27.getText()
                .toString(), et_28.getText().toString(),
                et_29.getText().toString(), null);
        ((MyApplication) addActivity.getApplication()).setInfoAdd3(info_add3);
        Info_add info = new Info_add();
        info.setInfo_add1(((MyApplication) addActivity.getApplication())
                .getInfoAdd1());
        info.setInfo_add2(((MyApplication) addActivity.getApplication())
                .getInfoAdd2());
        info.setInfo_add3(((MyApplication) addActivity.getApplication())
                .getInfoAdd3());
        addActivity.save(info);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        for (int i = 0; i < radio_15.getChildCount(); i++) {
            RadioButton r = (RadioButton) radio_15.getChildAt(i);
            if (r.isChecked()) {
                str_chukou = r.getText().toString();
                break;
            }
        }
        Info_add3 info_add3 = new Info_add3(et_1.getText().toString(),
                sp_2.getSelectedItem().toString(),
                sp_3.getSelectedItem().toString(),
                sp_4.getSelectedItem().toString(), et_5
                .getText().toString(), sp_6
                .getSelectedItem().toString(), et_7.getText()
                .toString(), et_8.getText().toString(), et_9
                .getText().toString(), sp_10.getSelectedItem().toString(),
                tv_11.getText().toString(), et_12
                .getText().toString(), et_13.getText().toString(),
                et_14.getText().toString(), str_chukou, et_16.getText()
                .toString(),
                tv_17.getText().toString(), sp_18.getSelectedItem()
                .toString(), sp_19.getSelectedItem()
                .toString(), sp_20.getSelectedItem()
                .toString(), sp_21.getSelectedItem()
                .toString(), et_22.getText().toString(),
                et_23.getText().toString(), et_24.getText()
                .toString(), et_25.getText().toString(),
                et_26.getText().toString(), et_27.getText()
                .toString(), et_28.getText().toString(),
                et_29.getText().toString(), null);
        ((MyApplication) addActivity.getApplication()).setInfoAdd3(info_add3);
        Info_add info = new Info_add();
        info.setInfo_add1(((MyApplication) addActivity.getApplication())
                .getInfoAdd1());
        info.setInfo_add2(((MyApplication) addActivity.getApplication())
                .getInfoAdd2());
        info.setInfo_add3(((MyApplication) addActivity.getApplication())
                .getInfoAdd3());
        addActivity.save(info);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // 这个super不能落下，否则可能回调不了
        if (requestCode == 3) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                et_9.setText(scanResult);
                if (NetworkUtil.checkedNetWork(addActivity)) {
                    new Thread() {
                        @Override
                        public void run() {
                            String result = HttpUtils.getAAQI(UsedPath.api_AAQI
                                    + et_9.getText().toString());
                            //Log.v("result",result);
                            if (result.equals("获取数据失败") || result.equals("")) {
                                Looper.prepare();
                                Toast.makeText(addActivity, "获取商品信息失败",
                                        Toast.LENGTH_SHORT).show();
                            } else if (result.equals("1")) {
                                Looper.prepare();
                                Toast.makeText(addActivity, "当前网络不可用",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Gson gson = new Gson();
                                final AAQI aaqi = gson.fromJson(result,
                                        AAQI.class);
                                //Log.v("result",aaqi.toString());
                                if (aaqi.getStatus().equals("success")) {
                                    et_1.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Log.v("GOODS_NAME",aaqi.getMessage().getGOODS_NAME());
                                            et_1.setText(aaqi.getMessage().getGOODS_NAME());
                                        }
                                    });
                                    et_5.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            et_5.setText(aaqi.getMessage().getTRADEMARK());
                                        }
                                    });
                                    et_8.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            et_8.setText(aaqi.getMessage().getSAMPLE_MODEL());
                                        }
                                    });
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(addActivity, "条码扫描错误，请重试",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }.start();
                } else {
                    Toast.makeText(addActivity, "当前无网络", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    public void initdata() {
        String info_add_str = addActivity.load();
        Gson gson = new Gson();
        Info_add info = gson.fromJson(info_add_str, Info_add.class);
        if (info != null && info.getInfo_add3() != null) {
            et_1.setText(info.getInfo_add3().getValue1());
            SpinnerAdapter adapter1 = sp_2.getAdapter();
            for (int i = 0; i < adapter1.getCount(); i++) {
                if (info.getInfo_add3().getValue2()
                        .equals(adapter1.getItem(i).toString())) {
                    sp_2.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter2 = sp_3.getAdapter();
            for (int i = 0; i < adapter2.getCount(); i++) {
                if (info.getInfo_add3().getValue3()
                        .equals(adapter2.getItem(i).toString())) {
                    sp_3.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter3 = sp_4.getAdapter();
            for (int i = 0; i < adapter3.getCount(); i++) {
                if (info.getInfo_add3().getValue4()
                        .equals(adapter3.getItem(i).toString())) {
                    sp_4.setSelection(i, true);
                    break;
                }
            }
            et_5.setText(info.getInfo_add3().getValue5());
            SpinnerAdapter adapter4 = sp_6.getAdapter();
            for (int i = 0; i < adapter4.getCount(); i++) {
                if (info.getInfo_add3().getValue6()
                        .equals(adapter4.getItem(i).toString())) {
                    sp_6.setSelection(i, true);
                    break;
                }
            }
            et_7.setText(info.getInfo_add3().getValue7());
            et_8.setText(info.getInfo_add3().getValue8());
            et_9.setText(info.getInfo_add3().getValue9());

            SpinnerAdapter adapter5 = sp_10.getAdapter();
            for (int i = 0; i < adapter5.getCount(); i++) {
                if (info.getInfo_add3().getValue10()
                        .equals(adapter5.getItem(i).toString())) {
                    sp_10.setSelection(i, true);
                    break;
                }
            }
            tv_11.setText(info.getInfo_add3().getValue11());
            et_12.setText(info.getInfo_add3().getValue12());
            et_13.setText(info.getInfo_add3().getValue13());
            et_14.setText(info.getInfo_add3().getValue14());
            if (info.getInfo_add3().getValue15().equals("是")) {
                radio_15.check(R.id.radio_chukou_shi);
            } else if (info.getInfo_add3().getValue15().equals("否")) {
                radio_15.check(R.id.radio_chukou_fou);
            }
            et_16.setText(info.getInfo_add3().getValue16());
            tv_17.setText(info.getInfo_add3().getValue17());
            SpinnerAdapter adapter6 = sp_18.getAdapter();
            for (int i = 0; i < adapter6.getCount(); i++) {
                if (info.getInfo_add3().getValue18()
                        .equals(adapter6.getItem(i).toString())) {
                    sp_18.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter7 = sp_19.getAdapter();
            for (int i = 0; i < adapter7.getCount(); i++) {
                if (info.getInfo_add3().getValue19()
                        .equals(adapter7.getItem(i).toString())) {
                    sp_19.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter8 = sp_20.getAdapter();
            for (int i = 0; i < adapter8.getCount(); i++) {
                if (info.getInfo_add3().getValue20()
                        .equals(adapter8.getItem(i).toString())) {
                    sp_20.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter9 = sp_21.getAdapter();
            for (int i = 0; i < adapter9.getCount(); i++) {
                if (info.getInfo_add3().getValue21()
                        .equals(adapter9.getItem(i).toString())) {
                    sp_21.setSelection(i, true);
                    break;
                }
            }
            et_22.setText(info.getInfo_add3().getValue22());
            et_23.setText(info.getInfo_add3().getValue23());
            et_24.setText(info.getInfo_add3().getValue24());
            et_25.setText(info.getInfo_add3().getValue25());
            et_26.setText(info.getInfo_add3().getValue26());
            et_27.setText(info.getInfo_add3().getValue27());
            et_28.setText(info.getInfo_add3().getValue28());
            et_29.setText(info.getInfo_add3().getValue29());
        }
    }
}