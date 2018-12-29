package com.hzlf.sampletest.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.activity.AddActivity;
import com.hzlf.sampletest.activity.ScanActivity;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.model.Info_add;
import com.hzlf.sampletest.model.Info_add2;
import com.hzlf.sampletest.others.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fragment_add2 extends Fragment implements OnClickListener, CompoundButton
        .OnCheckedChangeListener {

    private Spinner spinner_suozaidi, spinner_chouyangdidian,
            spinner_quyuleixing, spinner_chouyanghuanjie;
    private String[][] data_chouyangdidian = new String[][]{
            {"原辅料库", "生产线", "半成品库", "成品库(待检区)", "成品库(已检区)"},
            {"农贸市场", "菜市场", "批发市场", "商场", "超市", "网购", "小杂食店", "其他"},
            {"餐馆(特大型餐馆)", "餐馆(大型餐馆)", "餐馆(中型餐馆)", "餐馆(小型餐馆)", "食堂(机关食堂)",
                    "食堂(学校/托幼食堂)", "食堂(企事业单位食堂)", "食堂(建筑工地食堂)", "小吃店",
                    "快餐店", "饮品店", "集体用餐配送单位", "中央厨房", "其他"}};
    private ArrayAdapter adapter_danweimingcheng, adapter_suozaidi,
            adapter_chouyangdidian, adapter_quyuleixing,
            adapter_chouyanghuanjie;
    private Button btn_back2, btn_next2, btn_clear2, btn_scanning_info, btn_queren;
    private String[] danweimingcheng;
    private AutoCompleteTextView textview1;
    private EditText textview2, textview3, textview4, textview5, textview6,
            textview7, textview8, textview9, textview10, textview11,
            textview12, textview13, textview14, textview15, textview16;
    private RadioGroup radio_xukezheng1, radio_xukezheng2;
    private RadioButton xukezheng_1, xukezheng_2, xukezheng_3, xukezheng_4;
    private String str_xukezheng, str_nianxiaoshoue, str_chuanzhen, str_youbian, info_add_str;
    private Info_add info = null;
    private AddActivity addActivity;
    private DBManage dbmanage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addActivity = (AddActivity) getActivity();
        View view = inflater.inflate(R.layout.add_layout2, container, false);
        dbmanage = new DBManage(getActivity());

        info_add_str = addActivity.load();
        Gson gson = new Gson();
        info = gson.fromJson(info_add_str, Info_add.class);

        /*------------------------------------------------------------------------------*/
        spinner_suozaidi = view.findViewById(R.id.spinner_suozaidi);
        spinner_chouyangdidian = view
                .findViewById(R.id.spinner_chouyangdidian);
        spinner_quyuleixing = view
                .findViewById(R.id.spinner_quyuleixing);
        spinner_chouyanghuanjie = view
                .findViewById(R.id.spinner_chouyanghuanjie);
        textview1 = view
                .findViewById(R.id.input_danweimingcheng);
        textview2 = view.findViewById(R.id.input_danweidizhi);
        textview3 = view.findViewById(R.id.input_yingyezhizhao);
        textview4 = view.findViewById(R.id.input_xukezhenghao);
        textview5 = view.findViewById(R.id.input_danweifaren);
        textview6 = view.findViewById(R.id.input_nianxiaoshoue);
        textview7 = view.findViewById(R.id.input_lianxiren);
        textview8 = view.findViewById(R.id.input_dianhua);
        textview9 = view.findViewById(R.id.input_chuanzhen);
        textview10 = view.findViewById(R.id.input_youbian);
        textview11 = view
                .findViewById(R.id.input_shengchanzhemingcheng);
        textview12 = view.findViewById(R.id.input_shengchanzhedizhi);
        textview13 = view.findViewById(R.id.input_shengchanzhelianxiren);
        textview14 = view.findViewById(R.id.input_dianhua2);
        textview15 = view.findViewById(R.id.input_jiezhiriqi);
        textview16 = view.findViewById(R.id.input_jisongdizhi);
        radio_xukezheng1 = view.findViewById(R.id.radio_xukezheng1);
        radio_xukezheng2 = view.findViewById(R.id.radio_xukezheng2);
        xukezheng_1 = view.findViewById(R.id.radio_xukezheng_liutong);
        xukezheng_2 = view.findViewById(R.id.radio_xukezheng_canyinfuwu);
        xukezheng_3 = view.findViewById(R.id.radio_xukezheng_shipinjingying);
        xukezheng_4 = view.findViewById(R.id.radio_xukezheng_shipinshengchan);
        btn_scanning_info = view.findViewById(R.id.btn_scanning_info);
        btn_back2 = view.findViewById(R.id.btn_back2);
        btn_clear2 = view.findViewById(R.id.btn_clear2);
        btn_next2 = view.findViewById(R.id.btn_next2);
        btn_queren = view.findViewById(R.id.btn_queren);

        adapter_suozaidi = ArrayAdapter.createFromResource(getActivity(),
                R.array.array2_1, android.R.layout.simple_spinner_dropdown_item);
        spinner_suozaidi.setAdapter(adapter_suozaidi);

        adapter_chouyangdidian = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, data_chouyangdidian[0]); // 设置样式
        adapter_chouyangdidian
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
        spinner_chouyangdidian.setAdapter(adapter_chouyangdidian);

        adapter_quyuleixing = ArrayAdapter.createFromResource(getActivity(),
                R.array.array2_3, android.R.layout.simple_spinner_dropdown_item);
        spinner_quyuleixing.setAdapter(adapter_quyuleixing);

        adapter_chouyanghuanjie = ArrayAdapter.createFromResource(getActivity(),
                R.array.array2_4, android.R.layout.simple_spinner_dropdown_item);
        spinner_chouyanghuanjie.setAdapter(adapter_chouyanghuanjie);

        spinner_chouyanghuanjie
                .setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO 自动生成的方法存根
                        // 使用ArrayAdapter转换数据
                        adapter_chouyangdidian = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_dropdown_item,
                                data_chouyangdidian[position]);
                        adapter_chouyangdidian
                                .setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item); // 加载适配器
                        spinner_chouyangdidian
                                .setAdapter(adapter_chouyangdidian);
                        SpinnerAdapter adapter2 = spinner_chouyangdidian.getAdapter();
                        try {
                            if (info != null && info.getInfo_add2().getValue2() != null) {
                                for (int i = 0; i < adapter2.getCount(); i++) {
                                    if (info.getInfo_add2().getValue2()
                                            .equals(adapter2.getItem(i).toString())) {
                                        spinner_chouyangdidian.setSelection(i, true);
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO 自动生成的方法存根
                    }
                });

        danweimingcheng = dbmanage.findList_Info_add2().toArray(
                new String[dbmanage.findList_Info_add2().size()]);
        //需要一个适配器 初始化数据源--这个数据源去匹配文本框中输入的内容
        adapter_danweimingcheng = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, danweimingcheng);
        // 将adpter与当前AutoCompleteTextView绑定
        textview1.setAdapter(adapter_danweimingcheng);

        xukezheng_1.setOnCheckedChangeListener(this);
        xukezheng_2.setOnCheckedChangeListener(this);
        xukezheng_3.setOnCheckedChangeListener(this);
        xukezheng_4.setOnCheckedChangeListener(this);

        btn_scanning_info.setOnClickListener(this);
        btn_back2.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
        btn_next2.setOnClickListener(this);
        btn_queren.setOnClickListener(this);

        initdata();
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.radio_xukezheng_liutong:
                radio_xukezheng2.clearCheck();
                break;
            case R.id.radio_xukezheng_canyinfuwu:
                radio_xukezheng2.clearCheck();
                break;
            case R.id.radio_xukezheng_shipinjingying:
                radio_xukezheng1.clearCheck();
                break;
            case R.id.radio_xukezheng_shipinshengchan:
                radio_xukezheng1.clearCheck();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scanning_info:
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator = integrator.forSupportFragment(fragment_add2.this);
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
                integrator.setPrompt("请扫描二维码"); //底部的提示文字，设为""可以置空
                integrator.setRequestCode(2);
                //integrator.setCameraId(0); //前置或者后置摄像头
                //integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                //integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
                break;
            case R.id.btn_next2:
                if (textview1.getText().toString().equals("")
                        || textview2.getText().toString().equals("")
                        || textview3.getText().toString().equals("")
                        || textview4.getText().toString().equals("")
                        || textview5.getText().toString().equals("")
                        || textview7.getText().toString().equals("")
                        || textview8.getText().toString().equals("")
                        || textview11.getText().toString().equals("")
                        || textview12.getText().toString().equals("")
                        || textview13.getText().toString().equals("")
                        || textview14.getText().toString().equals("")
                        || textview15.getText().toString().equals("")
                        || textview16.getText().toString().equals("")) {
                    ((MyApplication) getActivity().getApplication()).setAdd2(0);
                } else {
                    if (textview6.getText().toString().equals("")) {
                        str_nianxiaoshoue = "/";
                    } else {
                        str_nianxiaoshoue = textview6.getText().toString();
                    }
                    if (textview9.getText().toString().equals("")) {
                        str_chuanzhen = "/";
                    } else {
                        str_chuanzhen = textview9.getText().toString();
                    }
                    if (textview10.getText().toString().equals("")) {
                        str_youbian = "/";
                    } else {
                        str_youbian = textview10.getText().toString();
                    }
                    for (int i = 0; i < radio_xukezheng1.getChildCount(); i++) {
                        RadioButton r = (RadioButton) radio_xukezheng1.getChildAt(i);
                        if (r.isChecked()) {
                            str_xukezheng = r.getText().toString();
                            break;
                        }
                    }
                    for (int i = 0; i < radio_xukezheng2.getChildCount(); i++) {
                        RadioButton r = (RadioButton) radio_xukezheng2.getChildAt(i);
                        if (r.isChecked()) {
                            str_xukezheng = r.getText().toString();
                            break;
                        }
                    }
                    Info_add2 info_add2 = new Info_add2(spinner_suozaidi
                            .getSelectedItem().toString(), spinner_chouyangdidian
                            .getSelectedItem().toString(), spinner_quyuleixing
                            .getSelectedItem().toString(), spinner_chouyanghuanjie
                            .getSelectedItem().toString(), textview1.getText()
                            .toString(), textview2.getText().toString(), textview3
                            .getText().toString(), str_xukezheng, textview4
                            .getText().toString(), textview5.getText().toString(),
                            str_nianxiaoshoue, textview7.getText().toString(),
                            textview8.getText().toString(), str_chuanzhen,
                            str_youbian, textview11.getText().toString(),
                            textview12.getText().toString(), textview13.getText()
                            .toString(), textview14.getText().toString(),
                            textview15.getText().toString(), textview16.getText()
                            .toString());
                    ((MyApplication) getActivity().getApplication())
                            .setInfoAdd2(info_add2);
                    ((MyApplication) getActivity().getApplication()).setAdd2(1);
                }
                AddActivity addActivity = (AddActivity) getActivity();
                addActivity.nextFragment();
                break;
            case R.id.btn_clear2:
                spinner_suozaidi.setSelection(0);
                spinner_chouyangdidian.setSelection(0);
                spinner_quyuleixing.setSelection(0);
                spinner_chouyanghuanjie.setSelection(0);
                textview1.setText("");
                textview2.setText("");
                textview3.setText("");
                radio_xukezheng1.check(R.id.radio_xukezheng_liutong);
                radio_xukezheng2.clearCheck();
                textview4.setText("");
                textview5.setText("");
                textview6.setText("");
                textview7.setText("");
                textview8.setText("");
                textview9.setText("");
                textview10.setText("");
                textview11.setText("");
                textview12.setText("");
                textview13.setText("");
                textview14.setText("");
                textview15.setText("");
                textview16.setText("");
                break;
            case R.id.btn_back2:
                AddActivity backFragment = (AddActivity) getActivity();
                backFragment.backFragment();
                break;
            case R.id.btn_queren:
                if (textview1.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "单位名称未填写", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (dbmanage.findInfo_add2(textview1.getText().toString()) == null) {
                        Toast.makeText(getActivity(), "无填写记录", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Info_add2 info_add2 = dbmanage.findInfo_add2(textview1
                                .getText().toString());
                        textview2.setText(info_add2.getValue6());
                        textview3.setText(info_add2.getValue7());
                        if (info_add2.getValue8().equals("流通许可证")) {
                            radio_xukezheng1.check(R.id.radio_xukezheng_liutong);
                            radio_xukezheng2.clearCheck();
                        } else if (info_add2.getValue8().equals("餐饮服务许可证")) {
                            radio_xukezheng1.check(R.id.radio_xukezheng_canyinfuwu);
                            radio_xukezheng2.clearCheck();
                        } else if (info_add2.getValue8().equals("食品经营许可证")) {
                            radio_xukezheng2
                                    .check(R.id.radio_xukezheng_shipinjingying);
                            radio_xukezheng1.clearCheck();
                        } else if (info_add2.getValue8().equals("食品生产许可证")) {
                            radio_xukezheng2
                                    .check(R.id.radio_xukezheng_shipinshengchan);
                            radio_xukezheng1.clearCheck();
                        }
                        textview4.setText(info_add2.getValue9());
                        textview5.setText(info_add2.getValue10());
                        textview6.setText(info_add2.getValue11());
                        textview7.setText(info_add2.getValue12());
                        textview8.setText(info_add2.getValue13());
                        textview9.setText(info_add2.getValue14());
                        textview10.setText(info_add2.getValue15());
                        textview11.setText(info_add2.getValue16());
                        textview12.setText(info_add2.getValue17());
                        textview13.setText(info_add2.getValue18());
                        textview14.setText(info_add2.getValue19());
                        textview15.setText(info_add2.getValue20());
                        textview16.setText(info_add2.getValue21());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // 这个super可不能落下，否则可能回调不了
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(resultCode, data);
                if (scanResult != null) {
                    String result = scanResult.getContents();
                    Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
                    Matcher isNum = pattern.matcher(result);
                    if (!isNum.matches()) {
                        String[] info = result.split("\r\n");
                        for (String oneinfo : info) {
                            if (oneinfo.indexOf("企业名称") != -1) {
                                textview1.setText(oneinfo.substring(5));
                            } else if (oneinfo.indexOf("注册号") != -1) {
                                textview3.setText(oneinfo.substring(4));
                            } else if (oneinfo.indexOf("住所") != -1) {
                                textview2.setText(oneinfo.substring(3));
                            } else if (oneinfo.indexOf("法定代表人") != -1) {
                                textview5.setText(oneinfo.substring(6));
                            }
                        }
                    }
                } else {
                    Log.v("scanResult", "scanResult is null");
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        } else {

        }
    }

    @Override
    public void onPause() {
        for (int i = 0; i < radio_xukezheng1.getChildCount(); i++) {
            RadioButton r = (RadioButton) radio_xukezheng1.getChildAt(i);
            if (r.isChecked()) {
                str_xukezheng = r.getText().toString();
                break;
            }
        }
        for (int i = 0; i < radio_xukezheng2.getChildCount(); i++) {
            RadioButton r = (RadioButton) radio_xukezheng2.getChildAt(i);
            if (r.isChecked()) {
                str_xukezheng = r.getText().toString();
                break;
            }
        }
        if (textview6.getText().toString().equals("")) {
            str_nianxiaoshoue = "/";
        } else {
            str_nianxiaoshoue = textview6.getText().toString();
        }
        if (textview9.getText().toString().equals("")) {
            str_chuanzhen = "/";
        } else {
            str_chuanzhen = textview9.getText().toString();
        }
        if (textview10.getText().toString().equals("")) {
            str_youbian = "/";
        } else {
            str_youbian = textview10.getText().toString();
        }
        Info_add2 info_add2 = new Info_add2(spinner_suozaidi.getSelectedItem()
                .toString(), spinner_chouyangdidian.getSelectedItem()
                .toString(), spinner_quyuleixing.getSelectedItem().toString(),
                spinner_chouyanghuanjie.getSelectedItem().toString(), textview1
                .getText().toString(), textview2.getText().toString(),
                textview3.getText().toString(), str_xukezheng, textview4
                .getText().toString(), textview5.getText().toString(),
                str_nianxiaoshoue, textview7.getText().toString(), textview8
                .getText().toString(), str_chuanzhen, str_youbian,
                textview11.getText().toString(), textview12.getText()
                .toString(), textview13.getText().toString(),
                textview14.getText().toString(), textview15.getText().toString(),
                textview16.getText().toString());
        ((MyApplication) getActivity().getApplication()).setInfoAdd2(info_add2);
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
        for (int i = 0; i < radio_xukezheng1.getChildCount(); i++) {
            RadioButton r = (RadioButton) radio_xukezheng1.getChildAt(i);
            if (r.isChecked()) {
                str_xukezheng = r.getText().toString();
                break;
            }
        }
        for (int i = 0; i < radio_xukezheng2.getChildCount(); i++) {
            RadioButton r = (RadioButton) radio_xukezheng2.getChildAt(i);
            if (r.isChecked()) {
                str_xukezheng = r.getText().toString();
                break;
            }
        }
        if (textview6.getText().toString().equals("")) {
            str_nianxiaoshoue = "/";
        } else {
            str_nianxiaoshoue = textview6.getText().toString();
        }
        if (textview9.getText().toString().equals("")) {
            str_chuanzhen = "/";
        } else {
            str_chuanzhen = textview9.getText().toString();
        }
        if (textview10.getText().toString().equals("")) {
            str_youbian = "/";
        } else {
            str_youbian = textview10.getText().toString();
        }
        Info_add2 info_add2 = new Info_add2(spinner_suozaidi.getSelectedItem()
                .toString(), spinner_chouyangdidian.getSelectedItem()
                .toString(), spinner_quyuleixing.getSelectedItem().toString(),
                spinner_chouyanghuanjie.getSelectedItem().toString(), textview1
                .getText().toString(), textview2.getText().toString(),
                textview3.getText().toString(), str_xukezheng, textview4
                .getText().toString(), textview5.getText().toString(),
                str_nianxiaoshoue, textview7.getText().toString(), textview8
                .getText().toString(), str_chuanzhen, str_youbian,
                textview11.getText().toString(), textview12.getText()
                .toString(), textview13.getText().toString(),
                textview14.getText().toString(), textview15.getText().toString(),
                textview16.getText().toString());
        ((MyApplication) getActivity().getApplication()).setInfoAdd2(info_add2);
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

    public void initdata() {
        if (info != null && info.getInfo_add2() != null) {
            SpinnerAdapter adapter1 = spinner_suozaidi.getAdapter();
            for (int i = 0; i < adapter1.getCount(); i++) {
                if (info.getInfo_add2().getValue1()
                        .equals(adapter1.getItem(i).toString())) {
                    spinner_suozaidi.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter3 = spinner_quyuleixing.getAdapter();
            for (int i = 0; i < adapter3.getCount(); i++) {
                if (info.getInfo_add2().getValue3()
                        .equals(adapter3.getItem(i).toString())) {
                    spinner_quyuleixing.setSelection(i, true);
                    break;
                }
            }
            SpinnerAdapter adapter4 = spinner_chouyanghuanjie.getAdapter();
            for (int i = 0; i < adapter4.getCount(); i++) {
                if (info.getInfo_add2().getValue4()
                        .equals(adapter4.getItem(i).toString())) {
                    spinner_chouyanghuanjie.setSelection(i, true);
                    break;
                }
            }
            textview1.setText(info.getInfo_add2().getValue5());
            textview2.setText(info.getInfo_add2().getValue6());
            textview3.setText(info.getInfo_add2().getValue7());
            if (info.getInfo_add2().getValue8() != null) {
                if (info.getInfo_add2().getValue8().equals("流通许可证")) {
                    radio_xukezheng1.check(R.id.radio_xukezheng_liutong);
                    radio_xukezheng2.clearCheck();
                } else if (info.getInfo_add2().getValue8().equals("餐饮服务许可证")) {
                    radio_xukezheng1.check(R.id.radio_xukezheng_canyinfuwu);
                    radio_xukezheng2.clearCheck();
                } else if (info.getInfo_add2().getValue8().equals("食品经营许可证")) {
                    radio_xukezheng2.check(R.id.radio_xukezheng_shipinjingying);
                    radio_xukezheng1.clearCheck();
                } else if (info.getInfo_add2().getValue8().equals("食品生产许可证")) {
                    radio_xukezheng2.check(R.id.radio_xukezheng_shipinshengchan);
                    radio_xukezheng1.clearCheck();
                }
            }
            textview4.setText(info.getInfo_add2().getValue9());
            textview5.setText(info.getInfo_add2().getValue10());
            textview6.setText(info.getInfo_add2().getValue11());
            textview7.setText(info.getInfo_add2().getValue12());
            textview8.setText(info.getInfo_add2().getValue13());
            textview9.setText(info.getInfo_add2().getValue14());
            textview10.setText(info.getInfo_add2().getValue15());
            textview11.setText(info.getInfo_add2().getValue16());
            textview12.setText(info.getInfo_add2().getValue17());
            textview13.setText(info.getInfo_add2().getValue18());
            textview14.setText(info.getInfo_add2().getValue19());
            textview15.setText(info.getInfo_add2().getValue20());
            textview16.setText(info.getInfo_add2().getValue21());
        }
    }
}