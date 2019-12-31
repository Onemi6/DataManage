package com.hzlf.sampletest.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.Info_add;
import com.hzlf.sampletest.model.Info_add1;
import com.hzlf.sampletest.model.Info_add2;
import com.hzlf.sampletest.model.Info_add3;
import com.hzlf.sampletest.model.Template;
import com.hzlf.sampletest.model.Upload;
import com.hzlf.sampletest.others.DatePickerDialog;
import com.hzlf.sampletest.others.ImgAdapter;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.util.ClickUtil;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements OnClickListener {
    private DBManage dbmanage = new DBManage(this);
    private Button btn_update1, btn_update2, btn_update3;
    private Context _context;
    private Toolbar toolbar;
    private Info_add info_add;
    private String number, filename, modelFilepath, rptFilepath1, rptFilepath2, token;
    private EditText et_0, et_1_1, et_1_2, et_1_3, et_1_5, et_1_6, et_1_7, et_1_8, et_1_9,
            et_1_10, et_1_11, et_2_5, et_2_6, et_2_7, et_2_9, et_2_10, et_2_11, et_2_12, et_2_13,
            et_2_14, et_2_15, et_2_16, et_2_17, et_2_18, et_2_19, et_2_20, et_2_21, et_3_1, et_3_5,
            et_3_7, et_3_8, et_3_9, et_3_11, et_3_12, et_3_13, et_3_14, et_3_16, et_3_17, et_3_22,
            et_3_23, et_3_24, et_3_25, et_3_26, et_3_27, et_3_28, et_3_29, et_3_30;
    private Spinner spinner1_4, spinner2_1, spinner2_2, spinner2_3, spinner2_4, spinner2_8,
            spinner3_2, spinner3_3, spinner3_4, spinner3_6, spinner3_10, spinner3_15,
            spinner3_18, spinner3_19, spinner3_20, spinner3_21;
    private String[][] data2_2 = new String[][]{{"原辅料库", "生产线", "半成品库", "成品库(待检区)", "成品库(已检区)"},
            {"农贸市场", "菜市场", "批发市场", "商场", "超市", "网购", "小杂食店", "其他"}, {"餐馆(特大型餐馆)",
            "餐馆(大型餐馆)", "餐馆(中型餐馆)", "餐馆(小型餐馆)", "食堂(机关食堂)", "食堂(学校/托幼食堂)", "食堂(企事业单位食堂)",
            "食堂(建筑工地食堂)", "小吃店", "快餐店", "饮品店", "集体用餐配送单位", "中央厨房", "其他"}};
    private ArrayAdapter adapter2_2;
    private ImgAdapter ada_upload_img;
    private List<String> list_paths = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private BuildBean dialog_make;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);
        initView();
        initData();
    }

    public void initView() {
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        _context = this;
        number = ((MyApplication) getApplication()).getNumber();
        info_add = dbmanage.findInfo_details(number);

        list_paths = dbmanage.findList_UploadImage(number);
        RecyclerView rv_upload_img = this.findViewById(R.id.rv_upload_img);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rv_upload_img.setLayoutManager(layoutManager);
        ada_upload_img = new ImgAdapter(this, list_paths);
        rv_upload_img.setAdapter(ada_upload_img);
        ada_upload_img.setOnClickListener(new ImgAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                String path = list_paths.get(position);
                View layout = getLayoutInflater().inflate(R.layout.img_item, (ViewGroup)
                        findViewById(R.id.dialog_img));
                ImageView imageview = layout.findViewById(R.id.imageView);
                AlertDialog.Builder dialog_img = new AlertDialog.Builder(DetailsActivity
                        .this).setView(layout)
                        .setPositiveButton("确定", null);
                dialog_img.show();
                Glide.with(_context).load(path)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.error)
                        .into(imageview);
            }
        });
        toolbar = findViewById(R.id.toolbar_details);
        toolbar.setTitle("详细信息");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        et_0 = findViewById(R.id.details_choujianyuanbianhao);
        et_1_1 = findViewById(R.id.details_choujiandanbianhao);
        et_1_2 = findViewById(R.id.details_renwulaiyuan);
        et_1_3 = findViewById(R.id.details_weituodanweidizhi);
        spinner1_4 = findViewById(R.id.details_renwuleibie);
        et_1_5 = findViewById(R.id.details_renwushuxing);
        et_1_6 = findViewById(R.id.details_danweimingcheng1);
        et_1_7 = findViewById(R.id.details_danweidizhi1);
        et_1_8 = findViewById(R.id.details_lianxiren1);
        et_1_9 = findViewById(R.id.details_dianhua1);
        et_1_10 = findViewById(R.id.details_chuanzhen1);
        et_1_11 = findViewById(R.id.details_youbian1);
        spinner2_1 = findViewById(R.id.details_suozaidi);
        spinner2_2 = findViewById(R.id.details_chouyangdidian);
        spinner2_3 = findViewById(R.id.details_quyuleixing);
        spinner2_4 = findViewById(R.id.details_chouyanghuanjie);
        et_2_5 = findViewById(R.id.details_danweimingcheng2);
        et_2_6 = findViewById(R.id.details_danweidizhi2);
        et_2_7 = findViewById(R.id.details_yingyezhizhao);
        spinner2_8 = findViewById(R.id.details_xukezhengleixing);
        et_2_9 = findViewById(R.id.details_xukezhenghao);
        et_2_10 = findViewById(R.id.details_danweifaren);
        et_2_11 = findViewById(R.id.details_nianxiaoshoue);
        et_2_12 = findViewById(R.id.details_lianxiren2);
        et_2_13 = findViewById(R.id.details_dianhua2);
        et_2_14 = findViewById(R.id.details_chuanzhen2);
        et_2_15 = findViewById(R.id.details_youbian2);
        et_2_16 = findViewById(R.id.details_shengchanzhemingcheng);
        et_2_17 = findViewById(R.id.details_shengchanzhedizhi);
        et_2_18 = findViewById(R.id.details_shengchanzhelianxiren);
        et_2_19 = findViewById(R.id.details_dianhua3);
        et_2_20 = findViewById(R.id.details_jiezhiriqi);
        et_2_21 = findViewById(R.id.details_jisongdizhi);
        et_3_1 = findViewById(R.id.details_yangpinmingcheng);
        spinner3_2 = findViewById(R.id.details_yangpinleixing);
        spinner3_3 = findViewById(R.id.details_yangpinlaiyuan);
        spinner3_4 = findViewById(R.id.details_yangpinshuxing);
        et_3_5 = findViewById(R.id.details_yangpinshangbiao);
        spinner3_6 = findViewById(R.id.details_baozhuangfenlei);
        et_3_7 = findViewById(R.id.details_guigexinghao);
        et_3_8 = findViewById(R.id.details_zhiliangdengji);
        et_3_9 = findViewById(R.id.details_yangpintiaoma);
        spinner3_10 = findViewById(R.id.details_riqileixing);
        et_3_11 = findViewById(R.id.details_shengchanriqi);
        et_3_12 = findViewById(R.id.details_baozhiqi);
        et_3_13 = findViewById(R.id.details_chanpinpihao);
        et_3_14 = findViewById(R.id.details_yangpindanjia);
        spinner3_15 = findViewById(R.id.details_shifouchukou);
        et_3_16 = findViewById(R.id.details_yuanchandi);
        et_3_17 = findViewById(R.id.details_chouyangriqi);
        spinner3_18 = findViewById(R.id.details_chouyangfangshi);
        spinner3_19 = findViewById(R.id.details_yangpinxingtai);
        spinner3_20 = findViewById(R.id.details_yangpinbaozhuang);
        spinner3_21 = findViewById(R.id.details_chucuntiaojian);
        et_3_22 = findViewById(R.id.details_zhixingbiaozhun);
        et_3_23 = findViewById(R.id.details_chouyangshuliangdanwei);
        et_3_24 = findViewById(R.id.details_chouyangjishu);
        et_3_25 = findViewById(R.id.details_beiyangshuliang);
        et_3_26 = findViewById(R.id.details_chouyangshuliang);
        et_3_27 = findViewById(R.id.details_chouyangren);
        et_3_28 = findViewById(R.id.details_yangpinxukezheng);
        et_3_29 = findViewById(R.id.details_beizhu);
        et_3_30 = findViewById(R.id.details_dayinriqi);

        ArrayAdapter adapter1_4 = ArrayAdapter.createFromResource(_context, R.array.array1_3,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner1_4.setAdapter(adapter1_4);
        ArrayAdapter adapter2_1 = ArrayAdapter.createFromResource(_context, R.array.array2_1,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner2_1.setAdapter(adapter2_1);
        ArrayAdapter adapter2_3 = ArrayAdapter.createFromResource(_context, R.array.array2_3,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner2_3.setAdapter(adapter2_3);
        ArrayAdapter adapter2_4 = ArrayAdapter.createFromResource(_context, R.array.array2_4,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner2_4.setAdapter(adapter2_4);
        ArrayAdapter adapter2_8 = ArrayAdapter.createFromResource(_context, R.array.array2_8,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner2_8.setAdapter(adapter2_8);
        ArrayAdapter adapter3_2 = ArrayAdapter.createFromResource(_context, R.array.array3_2,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_2.setAdapter(adapter3_2);
        ArrayAdapter adapter3_3 = ArrayAdapter.createFromResource(_context, R.array.array3_3,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_3.setAdapter(adapter3_3);
        ArrayAdapter adapter3_4 = ArrayAdapter.createFromResource(_context, R.array.array3_4,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_4.setAdapter(adapter3_4);
        ArrayAdapter adapter3_6 = ArrayAdapter.createFromResource(_context, R.array.array3_6,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_6.setAdapter(adapter3_6);
        ArrayAdapter adapter3_10 = ArrayAdapter.createFromResource(_context, R.array.array3_10,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_10.setAdapter(adapter3_10);
        ArrayAdapter adapter3_15 = ArrayAdapter.createFromResource(_context, R.array.array3_15,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_15.setAdapter(adapter3_15);
        ArrayAdapter adapter3_18 = ArrayAdapter.createFromResource(_context, R.array.array3_18,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_18.setAdapter(adapter3_18);
        ArrayAdapter adapter3_19 = ArrayAdapter.createFromResource(_context, R.array.array3_19,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_19.setAdapter(adapter3_19);
        ArrayAdapter adapter3_20 = ArrayAdapter.createFromResource(_context, R.array.array3_20,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_20.setAdapter(adapter3_20);
        ArrayAdapter adapter3_21 = ArrayAdapter.createFromResource(_context, R.array.array3_21,
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner3_21.setAdapter(adapter3_21);
        adapter2_2 = new ArrayAdapter<>(_context, android.R.layout
                .simple_spinner_dropdown_item, data2_2[0]);
        /* 设置样式*/
        adapter2_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); /*
        加载适配器*/
        spinner2_2.setAdapter(adapter2_2);
        spinner2_4.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {/* TODO 自动生成的方法存根
            使用ArrayAdapter转换数据*/
                adapter2_2 = new ArrayAdapter<>(_context, android.R.layout
                        .simple_spinner_dropdown_item,
                        data2_2[position]);
                adapter2_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ; /* 加载适配器*/
                spinner2_2.setAdapter(adapter2_2);
                SpinnerAdapter adapter2_2 = spinner2_2.getAdapter();
                for (int i = 0; i < adapter2_2.getCount(); i++)
                    if (info_add.getInfo_add2().getValue2().equals(adapter2_2.getItem(i).toString
                            ())) {
                        spinner2_2.setSelection(i, true);
                        break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/* TODO 自动生成的方法存根*/}
        });

        btn_update1 = findViewById(R.id.btn_update1);
        btn_update2 = findViewById(R.id.btn_update2);
        btn_update3 = findViewById(R.id.btn_update3);


        btn_update1.setOnClickListener(this);
        btn_update2.setOnClickListener(this);
        btn_update3.setOnClickListener(this);
    }

    public void initData() {
        try {
            FormatData();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.v("FormatData()", "NoSuchMethodException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.v("FormatData()", "InvocationTargetException");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.v("FormatData()", "IllegalAccessException");
        }
        et_0.setText(info_add.getNO());
        et_1_1.setText(info_add.getInfo_add1().getValue1());
        et_1_2.setText(info_add.getInfo_add1().getValue2());
        et_1_3.setText(info_add.getInfo_add1().getValue3());
        SpinnerAdapter adapter1_4 = spinner1_4.getAdapter();
        for (int i = 0; i < adapter1_4.getCount(); i++)
            if (info_add.getInfo_add1().getValue4().equals(adapter1_4.getItem(i).toString())) {
                spinner1_4.setSelection(i, true);
                break;
            }
        et_1_5.setText(info_add.getInfo_add1().getValue5());
        et_1_6.setText(info_add.getInfo_add1().getValue6());
        et_1_7.setText(info_add.getInfo_add1().getValue7());
        et_1_8.setText(info_add.getInfo_add1().getValue8());
        et_1_9.setText(info_add.getInfo_add1().getValue9());
        et_1_10.setText(info_add.getInfo_add1().getValue10());
        et_1_11.setText(info_add.getInfo_add1().getValue11());
        et_0.setEnabled(false);
        et_1_1.setEnabled(false);
        et_1_2.setEnabled(false);
        et_1_3.setEnabled(false);
        spinner1_4.setEnabled(false);
        et_1_5.setEnabled(false);
        et_1_6.setEnabled(false);
        et_1_7.setEnabled(false);
        et_1_8.setEnabled(false);
        et_1_9.setEnabled(false);
        et_1_10.setEnabled(false);
        et_1_11.setEnabled(false);
        SpinnerAdapter adapter2_1 = spinner2_1.getAdapter();
        for (int i = 0; i < adapter2_1.getCount(); i++)
            if (info_add.getInfo_add2().getValue1().equals(adapter2_1.getItem(i).toString())) {
                spinner2_1.setSelection(i, true);
                break;
            }
        SpinnerAdapter adapter2_3 = spinner2_3.getAdapter();
        for (int i = 0; i < adapter2_3.getCount(); i++)
            if (info_add.getInfo_add2().getValue3().equals(adapter2_3.getItem(i).toString())) {
                spinner2_3.setSelection(i, true);
                break;
            }
        SpinnerAdapter adapter2_4 = spinner2_4.getAdapter();
        for (int i = 0; i < adapter2_4.getCount(); i++)
            if (info_add.getInfo_add2().getValue4().equals(adapter2_4.getItem(i).toString())) {
                spinner2_4.setSelection(i, true);
                break;
            }
        et_2_5.setText(info_add.getInfo_add2().getValue5());
        et_2_6.setText(info_add.getInfo_add2().getValue6());
        et_2_7.setText(info_add.getInfo_add2().getValue7());
        SpinnerAdapter adapter2_8 = spinner2_8.getAdapter();
        for (int i = 0; i < adapter2_8.getCount(); i++)
            if (info_add.getInfo_add2().getValue8().equals(adapter2_8.getItem(i).toString())) {
                spinner2_8.setSelection(i, true);
                break;
            }
        et_2_9.setText(info_add.getInfo_add2().getValue9());
        et_2_10.setText(info_add.getInfo_add2().getValue10());
        et_2_11.setText(info_add.getInfo_add2().getValue11());
        et_2_12.setText(info_add.getInfo_add2().getValue12());
        et_2_13.setText(info_add.getInfo_add2().getValue13());
        et_2_14.setText(info_add.getInfo_add2().getValue14());
        et_2_15.setText(info_add.getInfo_add2().getValue15());
        et_2_16.setText(info_add.getInfo_add2().getValue16());
        et_2_17.setText(info_add.getInfo_add2().getValue17());
        et_2_18.setText(info_add.getInfo_add2().getValue18());
        et_2_19.setText(info_add.getInfo_add2().getValue19());
        et_2_20.setText(info_add.getInfo_add2().getValue20());
        et_2_21.setText(info_add.getInfo_add2().getValue21());
        spinner2_1.setEnabled(false);
        spinner2_2.setEnabled(false);
        spinner2_3.setEnabled(false);
        spinner2_4.setEnabled(false);
        et_2_5.setEnabled(false);
        et_2_6.setEnabled(false);
        et_2_7.setEnabled(false);
        spinner2_8.setEnabled(false);
        et_2_9.setEnabled(false);
        et_2_10.setEnabled(false);
        et_2_11.setEnabled(false);
        et_2_12.setEnabled(false);
        et_2_13.setEnabled(false);
        et_2_14.setEnabled(false);
        et_2_15.setEnabled(false);
        et_2_16.setEnabled(false);
        et_2_17.setEnabled(false);
        et_2_18.setEnabled(false);
        et_2_19.setEnabled(false);
        et_2_20.setEnabled(false);
        et_2_21.setEnabled(false);
        et_3_1.setText(info_add.getInfo_add3().getValue1());
        SpinnerAdapter adapter3_2 = spinner3_2.getAdapter();
        for (int i = 0; i < adapter3_2.getCount(); i++)
            if (info_add.getInfo_add3().getValue2().equals(adapter3_2.getItem(i).toString())) {
                spinner3_2.setSelection(i, true);
                break;
            }
        SpinnerAdapter adapter3_3 = spinner3_3.getAdapter();
        for (int i = 0; i < adapter3_3.getCount(); i++)
            if (info_add.getInfo_add3().getValue3().equals(adapter3_3.getItem(i).toString())) {
                spinner3_3.setSelection(i, true);
                break;
            }
        SpinnerAdapter adapter3_4 = spinner3_4.getAdapter();
        for (int i = 0; i < adapter3_4.getCount(); i++)
            if (info_add.getInfo_add3().getValue4().equals(adapter3_4.getItem(i).toString())) {
                spinner3_4.setSelection(i, true);
                break;
            }
        et_3_5.setText(info_add.getInfo_add3().getValue5());
        SpinnerAdapter adapter3_6 = spinner3_6.getAdapter();
        for (int i = 0; i < adapter3_6.getCount(); i++)
            if (info_add.getInfo_add3().getValue6().equals(adapter3_6.getItem(i).toString())) {
                spinner3_6.setSelection(i, true);
                break;
            }
        et_3_7.setText(info_add.getInfo_add3().getValue7());
        et_3_8.setText(info_add.getInfo_add3().getValue8());
        et_3_9.setText(info_add.getInfo_add3().getValue9());
        SpinnerAdapter adapter3_10 = spinner3_10.getAdapter();
        for (int i = 0; i < adapter3_10.getCount(); i++)
            if (info_add.getInfo_add3().getValue10().equals(adapter3_10.getItem(i).toString())) {
                spinner3_10.setSelection(i, true);
                break;
            }
        et_3_11.setText(info_add.getInfo_add3().getValue11());
        et_3_12.setText(info_add.getInfo_add3().getValue12());
        et_3_13.setText(info_add.getInfo_add3().getValue13());
        et_3_14.setText(info_add.getInfo_add3().getValue14());
        SpinnerAdapter adapter3_15 = spinner3_15.getAdapter();
        for (int i = 0; i < adapter3_15.getCount(); i++)
            if (info_add.getInfo_add3().getValue15().equals(adapter3_15.getItem(i).toString())) {
                spinner3_15.setSelection(i, true);
                break;
            }
        et_3_16.setText(info_add.getInfo_add3().getValue16());
        et_3_17.setText(info_add.getInfo_add3().getValue17());
        SpinnerAdapter adapter3_18 = spinner3_18.getAdapter();
        for (int i = 0; i < adapter3_18.getCount(); i++)
            if (info_add.getInfo_add3().getValue18().equals(adapter3_18.getItem(i).toString())) {
                spinner3_18.setSelection(i, true);
                break;
            }
        SpinnerAdapter adapter3_19 = spinner3_19.getAdapter();
        for (int i = 0; i < adapter3_19.getCount(); i++) {
            if (info_add.getInfo_add3().getValue19()
                    .equals(adapter3_19.getItem(i).toString())) {
                spinner3_19.setSelection(i, true);
                break;
            }
        }
        SpinnerAdapter adapter3_20 = spinner3_20.getAdapter();
        for (int i = 0; i < adapter3_20.getCount(); i++) {
            if (info_add.getInfo_add3().getValue20()
                    .equals(adapter3_20.getItem(i).toString())) {
                spinner3_20.setSelection(i, true);
                break;
            }
        }
        SpinnerAdapter adapter3_21 = spinner3_21.getAdapter();
        for (int i = 0; i < adapter3_21.getCount(); i++) {
            if (info_add.getInfo_add3().getValue21()
                    .equals(adapter3_21.getItem(i).toString())) {
                spinner3_21.setSelection(i, true);
                break;
            }
        }
        et_3_22.setText(info_add.getInfo_add3().getValue22());
        et_3_23.setText(info_add.getInfo_add3().getValue23());
        et_3_24.setText(info_add.getInfo_add3().getValue24());
        et_3_25.setText(info_add.getInfo_add3().getValue25());
        et_3_26.setText(info_add.getInfo_add3().getValue26());
        et_3_27.setText(info_add.getInfo_add3().getValue27());
        et_3_28.setText(info_add.getInfo_add3().getValue28());
        et_3_29.setText(info_add.getInfo_add3().getValue29());
        et_3_30.setText(info_add.getInfo_add3().getValue30());

        et_3_1.setEnabled(false);
        spinner3_2.setEnabled(false);
        spinner3_3.setEnabled(false);
        spinner3_4.setEnabled(false);
        et_3_5.setEnabled(false);
        spinner3_6.setEnabled(false);
        et_3_7.setEnabled(false);
        et_3_8.setEnabled(false);
        et_3_9.setEnabled(false);
        spinner3_10.setEnabled(false);
        et_3_11.setEnabled(false);
        et_3_12.setEnabled(false);
        et_3_13.setEnabled(false);
        et_3_14.setEnabled(false);
        spinner3_15.setEnabled(false);
        et_3_16.setEnabled(false);
        et_3_17.setEnabled(false);
        spinner3_18.setEnabled(false);
        spinner3_19.setEnabled(false);
        spinner3_20.setEnabled(false);
        spinner3_21.setEnabled(false);
        et_3_22.setEnabled(false);
        et_3_23.setEnabled(false);
        et_3_24.setEnabled(false);
        et_3_25.setEnabled(false);
        et_3_26.setEnabled(false);
        et_3_27.setEnabled(false);
        et_3_28.setEnabled(false);
        et_3_29.setEnabled(false);
        et_3_30.setEnabled(false);
        filename = info_add.getInfo_add1().getValue1() + ".doc";
        modelFilepath = Environment.getExternalStorageDirectory() + "/DataManage/model/" + filename;
        rptFilepath1 = Environment.getExternalStorageDirectory() + "/DataManage/doc/" + filename;
        rptFilepath2 = Environment.getExternalStorageDirectory() + "/DataManage/doc2/" + filename;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update1:
                update1();
                break;
            case R.id.btn_update2:
                update2();
                break;
            case R.id.btn_update3:
                update3();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_upload_data:
                if (ClickUtil.isFastClick()) {
                    upload_data();
                } else {
                    Snackbar.make(toolbar, "点击太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                break;
            case R.id.action_upload_img:
                if (ClickUtil.isFastClick()) {
                    upload_img();
                } else {
                    Snackbar.make(toolbar, "提交太快了，请稍后再试",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
                break;
            case R.id.action_makeRpt1:
                getSignModel(1);
                break;
            case R.id.action_makeRpt2:
                getSignModel(2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void update1() {
        if (btn_update1.getText().toString().equals("修改")) {
            et_1_2.setEnabled(true);
            et_1_3.setEnabled(true);
            spinner1_4.setEnabled(true);
            et_1_5.setEnabled(true);
            et_1_8.setEnabled(true);
            et_1_9.setEnabled(true);
            et_1_10.setEnabled(true);
            et_1_11.setEnabled(true);
            btn_update1.setText("确认");
        } else if (btn_update1.getText().toString().equals("确认")) {
            et_1_2.setEnabled(false);
            et_1_3.setEnabled(false);
            spinner1_4.setEnabled(false);
            et_1_5.setEnabled(false);
            et_1_8.setEnabled(false);
            et_1_9.setEnabled(false);
            et_1_10.setEnabled(false);
            et_1_11.setEnabled(false);
            btn_update1.setText("修改");
            info_add.getInfo_add1().setValue2(et_1_2.getText().toString());
            info_add.getInfo_add1().setValue3(et_1_3.getText().toString());
            info_add.getInfo_add1().setValue4(spinner1_4.getSelectedItem().toString());
            info_add.getInfo_add1().setValue5(et_1_5.getText().toString());
            info_add.getInfo_add1().setValue8(et_1_8.getText().toString());
            info_add.getInfo_add1().setValue9(et_1_9.getText().toString());
            info_add.getInfo_add1().setValue10(et_1_10.getText().toString());
            info_add.getInfo_add1().setValue11(et_1_11.getText().toString());
            dbmanage.updateinfo(info_add);
        }
    }

    public void update2() {
        if (btn_update2.getText().toString().equals("修改")) {
            spinner2_1.setEnabled(true);
            spinner2_2.setEnabled(true);
            spinner2_3.setEnabled(true);
            spinner2_4.setEnabled(true);
            et_2_5.setEnabled(true);
            et_2_6.setEnabled(true);
            et_2_7.setEnabled(true);
            spinner2_8.setEnabled(true);
            et_2_9.setEnabled(true);
            et_2_10.setEnabled(true);
            et_2_11.setEnabled(true);
            et_2_12.setEnabled(true);
            et_2_13.setEnabled(true);
            et_2_14.setEnabled(true);
            et_2_15.setEnabled(true);
            et_2_16.setEnabled(true);
            et_2_17.setEnabled(true);
            et_2_18.setEnabled(true);
            et_2_19.setEnabled(true);
            et_2_20.setEnabled(true);
            et_2_21.setEnabled(true);
            btn_update2.setText("确认");
        } else if (btn_update2.getText().toString().equals("确认")) {
            spinner2_1.setEnabled(false);
            spinner2_2.setEnabled(false);
            spinner2_3.setEnabled(false);
            spinner2_4.setEnabled(false);
            et_2_5.setEnabled(false);
            et_2_6.setEnabled(false);
            et_2_7.setEnabled(false);
            spinner2_8.setEnabled(false);
            et_2_9.setEnabled(false);
            et_2_10.setEnabled(false);
            et_2_11.setEnabled(false);
            et_2_12.setEnabled(false);
            et_2_13.setEnabled(false);
            et_2_14.setEnabled(false);
            et_2_15.setEnabled(false);
            et_2_16.setEnabled(false);
            et_2_17.setEnabled(false);
            et_2_18.setEnabled(false);
            et_2_19.setEnabled(false);
            et_2_20.setEnabled(false);
            et_2_21.setEnabled(false);
            btn_update2.setText("修改");
            info_add.getInfo_add2().setValue1(spinner2_1.getSelectedItem().toString());
            info_add.getInfo_add2().setValue2(spinner2_2.getSelectedItem().toString());
            info_add.getInfo_add2().setValue3(spinner2_3.getSelectedItem().toString());
            info_add.getInfo_add2().setValue4(spinner2_4.getSelectedItem().toString());
            info_add.getInfo_add2().setValue5(et_2_5.getText().toString());
            info_add.getInfo_add2().setValue6(et_2_6.getText().toString());
            info_add.getInfo_add2().setValue7(et_2_7.getText().toString());
            info_add.getInfo_add2().setValue8(spinner2_8.getSelectedItem().toString());
            info_add.getInfo_add2().setValue9(et_2_9.getText().toString());
            info_add.getInfo_add2().setValue10(et_2_10.getText().toString());
            info_add.getInfo_add2().setValue11(et_2_11.getText().toString());
            info_add.getInfo_add2().setValue12(et_2_12.getText().toString());
            info_add.getInfo_add2().setValue13(et_2_13.getText().toString());
            info_add.getInfo_add2().setValue14(et_2_14.getText().toString());
            info_add.getInfo_add2().setValue15(et_2_15.getText().toString());
            info_add.getInfo_add2().setValue16(et_2_16.getText().toString());
            info_add.getInfo_add2().setValue17(et_2_17.getText().toString());
            info_add.getInfo_add2().setValue18(et_2_18.getText().toString());
            info_add.getInfo_add2().setValue19(et_2_19.getText().toString());
            info_add.getInfo_add2().setValue20(et_2_20.getText().toString());
            info_add.getInfo_add2().setValue21(et_2_21.getText().toString());
            dbmanage.updateinfo(info_add);
        }
    }

    public void update3() {
        if (btn_update3.getText().toString().equals("修改")) {
            et_3_1.setEnabled(true);
            spinner3_2.setEnabled(true);
            spinner3_3.setEnabled(true);
            spinner3_4.setEnabled(true);
            et_3_5.setEnabled(true);
            spinner3_6.setEnabled(true);
            et_3_7.setEnabled(true);
            et_3_8.setEnabled(true);
            et_3_9.setEnabled(true);
            spinner3_10.setEnabled(true);
            et_3_11.setEnabled(true);
            et_3_12.setEnabled(true);
            et_3_13.setEnabled(true);
            et_3_14.setEnabled(true);
            spinner3_15.setEnabled(true);
            et_3_16.setEnabled(true);
            et_3_17.setEnabled(true);
            spinner3_18.setEnabled(true);
            spinner3_19.setEnabled(true);
            spinner3_20.setEnabled(true);
            spinner3_21.setEnabled(true);
            et_3_22.setEnabled(true);
            et_3_23.setEnabled(true);
            et_3_24.setEnabled(true);
            et_3_25.setEnabled(true);
            et_3_26.setEnabled(true);
            et_3_27.setEnabled(true);
            et_3_28.setEnabled(true);
            et_3_29.setEnabled(true);
            et_3_30.setEnabled(true);
            btn_update3.setText("确认");
        } else if (btn_update3.getText().toString().equals("确认")) {
            et_3_1.setEnabled(false);
            spinner3_2.setEnabled(false);
            spinner3_3.setEnabled(false);
            spinner3_4.setEnabled(false);
            et_3_5.setEnabled(false);
            spinner3_6.setEnabled(false);
            et_3_7.setEnabled(false);
            et_3_8.setEnabled(false);
            et_3_9.setEnabled(false);
            spinner3_10.setEnabled(false);
            et_3_11.setEnabled(false);
            et_3_12.setEnabled(false);
            et_3_13.setEnabled(false);
            et_3_14.setEnabled(false);
            spinner3_15.setEnabled(false);
            et_3_16.setEnabled(false);
            et_3_17.setEnabled(false);
            spinner3_18.setEnabled(false);
            spinner3_19.setEnabled(false);
            spinner3_20.setEnabled(false);
            spinner3_21.setEnabled(false);
            et_3_22.setEnabled(false);
            et_3_23.setEnabled(false);
            et_3_24.setEnabled(false);
            et_3_25.setEnabled(false);
            et_3_26.setEnabled(false);
            et_3_27.setEnabled(false);
            et_3_28.setEnabled(false);
            et_3_29.setEnabled(false);
            et_3_30.setEnabled(false);
            btn_update3.setText("修改");
            info_add.getInfo_add3().setValue1(et_3_1.getText().toString());
            info_add.getInfo_add3().setValue2(spinner3_2.getSelectedItem().toString());
            info_add.getInfo_add3().setValue3(spinner3_3.getSelectedItem().toString());
            info_add.getInfo_add3().setValue4(spinner3_4.getSelectedItem().toString());
            info_add.getInfo_add3().setValue5(et_3_5.getText().toString());
            info_add.getInfo_add3().setValue6(spinner3_6.getSelectedItem().toString());
            info_add.getInfo_add3().setValue7(et_3_7.getText().toString());
            info_add.getInfo_add3().setValue8(et_3_8.getText().toString());
            info_add.getInfo_add3().setValue9(et_3_9.getText().toString());
            info_add.getInfo_add3().setValue10(spinner3_10.getSelectedItem().toString());
            info_add.getInfo_add3().setValue11(et_3_11.getText().toString());
            info_add.getInfo_add3().setValue12(et_3_12.getText().toString());
            info_add.getInfo_add3().setValue13(et_3_13.getText().toString());
            info_add.getInfo_add3().setValue14(et_3_14.getText().toString());
            info_add.getInfo_add3().setValue15(spinner3_15.getSelectedItem().toString());
            info_add.getInfo_add3().setValue16(et_3_16.getText().toString());
            info_add.getInfo_add3().setValue17(et_3_17.getText().toString());
            info_add.getInfo_add3().setValue18(spinner3_18.getSelectedItem().toString());
            info_add.getInfo_add3().setValue19(spinner3_19.getSelectedItem().toString());
            info_add.getInfo_add3().setValue20(spinner3_20.getSelectedItem().toString());
            info_add.getInfo_add3().setValue21(spinner3_21.getSelectedItem().toString());
            info_add.getInfo_add3().setValue22(et_3_22.getText().toString());
            info_add.getInfo_add3().setValue23(et_3_23.getText().toString());
            info_add.getInfo_add3().setValue24(et_3_24.getText().toString());
            info_add.getInfo_add3().setValue25(et_3_25.getText().toString());
            info_add.getInfo_add3().setValue26(et_3_26.getText().toString());
            info_add.getInfo_add3().setValue27(et_3_27.getText().toString());
            info_add.getInfo_add3().setValue28(et_3_28.getText().toString());
            info_add.getInfo_add3().setValue29(et_3_29.getText().toString());
            info_add.getInfo_add3().setValue30(et_3_30.getText().toString());
            dbmanage.updateinfo(info_add);
        }
    }

    public void upload_data() {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            /*ProgressDialog mypDialog = new ProgressDialog(DetailsActivity.this);*//* 实例化*//*
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);*//* 设置进度条风格，风格为圆形，旋转的*//*
            mypDialog.setTitle("上传数据中");*//* 设置ProgressDialog 标题*//*
            mypDialog.setIndeterminate(false);*//* 设置ProgressDialog 的进度条是否不明确*//*
            mypDialog.setCancelable(false);*//* 设置ProgressDialog 是否可以按退回按键取消*//*
            mypDialog.show();*//* 让ProgressDialog显示*/
            attemptApply();
            //mypDialog.dismiss();
        } else {
            Snackbar.make(toolbar, "当前网络不可用,请稍后再上传",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void attemptApply() {
        final BuildBean dialog_apply = DialogUIUtils.showLoading(_context, "上传数据中...", false,
                true, false,
                false);
        dialog_apply.show();
        String RECORDER;
        Info_add info_add_upload = dbmanage.findInfo_details(number);
        Map<String, String> map = new HashMap<>();
        //ID NO LAB_NO DATE_RECORD  SAMPLE_ADDR  APPLY_KIND
        //CHECKER DATE_CHECK STATE APPLY_KIND_NO CHECK_INFO
        map.put("GOODS_NAME", info_add_upload.getInfo_add3().getValue1());
        map.put("BUSINESS_SOURCE", info_add_upload.getInfo_add1().getValue2());
        map.put("MANU_COMPANY", info_add_upload.getInfo_add2().getValue16());
        map.put("SAMPLE_SOURCE", info_add_upload.getInfo_add3().getValue3());
        map.put("I_AND_O", info_add_upload.getInfo_add3().getValue15());
        map.put("DOMESTIC_AREA", info_add_upload.getInfo_add2().getValue3());
        map.put("SAMPLE_STATUS", info_add_upload.getInfo_add3().getValue19());
        map.put("REMARK", info_add_upload.getInfo_add3().getValue29());
        if (((MyApplication) getApplication()).getName() == null) {
            RECORDER = sharedPreferences.getString("NAME", null);
        } else {
            RECORDER = ((MyApplication) getApplication()).getName();
        }
        map.put("RECORDER", RECORDER);
        map.put("TRADEMARK", info_add_upload.getInfo_add3().getValue5());
        map.put("PACK", info_add_upload.getInfo_add3().getValue20());
        map.put("SAMPLE_CLASS", info_add_upload.getInfo_add3().getValue8());
        map.put("SAMPLE_MODEL", info_add_upload.getInfo_add3().getValue7());
        map.put("DRAW_ORG", info_add_upload.getInfo_add1().getValue6());
        map.put("DRAW_NUM", info_add_upload.getInfo_add3().getValue24() +
                info_add_upload.getInfo_add3().getValue23());
        map.put("DRAW_ADDR", info_add_upload.getInfo_add2().getValue4() + ":" +
                info_add_upload.getInfo_add2().getValue2());
        map.put("DATE_PRODUCT", info_add_upload.getInfo_add3().getValue11());
        map.put("SUPPLIER", info_add_upload.getInfo_add2().getValue5());
        map.put("SAMPLING_NO", info_add_upload.getInfo_add1().getValue1());
        map.put("EXPIRATIONDATE", info_add_upload.getInfo_add3().getValue12());
        map.put("MANU_COMPANY_PHONE", info_add_upload.getInfo_add2().getValue19());
        map.put("SUPPLIER_PHONE", info_add_upload.getInfo_add2().getValue13());
        map.put("SAVE_MODE", info_add_upload.getInfo_add3().getValue21());
        map.put("MANU_COMPANY_ADDR", info_add_upload.getInfo_add2().getValue17());
        map.put("STORAGESITE", info_add_upload.getInfo_add3().getValue25() +
                info_add_upload.getInfo_add3().getValue23());
        map.put("SUPPLIER_PERSON", info_add_upload.getInfo_add2().getValue12());
        map.put("SUPPLIER_ADDR", info_add_upload.getInfo_add2().getValue6());
        map.put("SUPPLIER_LEGAL", info_add_upload.getInfo_add2().getValue10());
        map.put("SUPPLIER_FAX", info_add_upload.getInfo_add2().getValue14());
        map.put("SAMPLE_TYPE", info_add_upload.getInfo_add1().getValue4());
        map.put("ANNUAL_SALES", info_add_upload.getInfo_add2().getValue11());
        map.put("BUSINESS_LICENCE", info_add_upload.getInfo_add2().getValue7());
        map.put("PERMIT_TYPE", info_add_upload.getInfo_add2().getValue8());
        map.put("PERMIT_NUM", info_add_upload.getInfo_add2().getValue9());
        map.put("SUPPLIER_ZIPCODE", info_add_upload.getInfo_add2().getValue15());
        map.put("SAMPLE_PROPERTY", info_add_upload.getInfo_add3().getValue4());
        map.put("SAMPLE_STYLE", info_add_upload.getInfo_add3().getValue2());
        map.put("SAMPLE_NUMBER", info_add_upload.getInfo_add3().getValue13());
        map.put("PRODUCTION_CERTIFICATE", info_add_upload.getInfo_add3().getValue28());
        map.put("UNIVALENT", info_add_upload.getInfo_add3().getValue14());
        map.put("PACK_TYPE", info_add_upload.getInfo_add3().getValue6());
        map.put("SAMPLE_CLOSE_DATE", info_add_upload.getInfo_add2()
                .getValue20());
        map.put("DRAW_METHOD", info_add_upload.getInfo_add3().getValue18());
        map.put("DRAW_PERSON", info_add_upload.getInfo_add1().getValue8());
        map.put("DRAW_PHONE", info_add_upload.getInfo_add1().getValue9());
        map.put("DRAW_FAX", info_add_upload.getInfo_add1().getValue10());
        map.put("DRAW_ZIPCODE", info_add_upload.getInfo_add1().getValue11());
        map.put("DRAW_ORG_ADDR", info_add_upload.getInfo_add1().getValue7());
        map.put("DRAW_AMOUNT", info_add_upload.getInfo_add3().getValue26() +
                info_add_upload.getInfo_add3().getValue23());
        map.put("TEST_FILE_NO", info_add_upload.getInfo_add3().getValue22());
        map.put("DATE_PRODUCT_TYPE", info_add_upload.getInfo_add3().getValue10());
        map.put("DRAW_MAN", info_add_upload.getInfo_add3().getValue27());
        map.put("DRAW_DATE", info_add_upload.getInfo_add3().getValue17());
        map.put("CLIENT_ADDR", info_add_upload.getInfo_add1().getValue3());
        map.put("TASK_REMARK", info_add_upload.getInfo_add1().getValue5());
        map.put("C_ADDR", info_add_upload.getInfo_add2().getValue1());
        map.put("SAMPLE_CODE", info_add_upload.getInfo_add3().getValue9());
        map.put("S_ADDR", info_add_upload.getInfo_add3().getValue16());

        map.put("SAMPLE_ADDR", info_add_upload.getInfo_add2().getValue18());

        String obj = new Gson().toJson(map);
        //eLab_API request = HttpUtils.GsonApi();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; " +
                "charset=utf-8"), obj);
        Log.v("obj", obj);
        eLab_API request = HttpUtils.GsonApi();
        if (((MyApplication) getApplication()).getToken() == null) {
            token = "Bearer " + sharedPreferences.getString("token", "");
        } else {
            token = "Bearer " + ((MyApplication) getApplication()).getToken();
        }
        Call<Upload> call = request.Apply(token, body);
        call.enqueue(new Callback<Upload>() {
            @Override
            public void onResponse(Call<Upload> call, Response<Upload> response) {
                if (response.code() == 401) {
                    Log.v("Apply请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(DetailsActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            dbmanage.updateId(response.body().getId(), number);
                            dbmanage.updateNumber(number, 1, 1, 1);
                            //mypDialog.dismiss();
                            Snackbar.make(toolbar, "上传" + number + response.body()
                                            .getMessage(),
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } else {
                            Snackbar.make(toolbar, "该" + response.body().getMessage(),
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    } else {
                        Log.v("Apply请求成功!", "response.code is null");
                        Snackbar.make(toolbar, "上传出现错误response.code is null",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } else if (response.code() == 500) {
                    Log.v("response", response.message());
                    Snackbar.make(toolbar, "数据已经上传",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                DialogUIUtils.dismiss(dialog_apply);
            }

            @Override
            public void onFailure(Call<Upload> call, Throwable t) {
                Log.v("Apply请求失败!", t.getMessage());
                DialogUIUtils.dismiss(dialog_apply);
                Snackbar.make(toolbar, "Apply请求失败!",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public void upload_img() {
        Intent intent_upload_img = new Intent();
        intent_upload_img.setClass(DetailsActivity.this, ImgUploadActivity.class);
        /*finish();// 结束当前活动*/
        startActivity(intent_upload_img);
    }

    public void getSignModel(int type) {
        dialog_make = DialogUIUtils.showLoading(_context, "生成报告中...", false,
                true, false,
                false);
        dialog_make.show();
        File file = copyFile("model.doc", modelFilepath);
        if (file != null) {
            if(type ==1) {
                attemptTemplate(file);
            }
            else if(type ==2){
                makeRpt(rptFilepath2);
            }
        } else {
            Snackbar.make(toolbar, "模板生成错误!",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public File copyFile(String oldPath$Name, String newPath$Name) {
        try {
            InputStream is = _context.getAssets().open(oldPath$Name);
            File file = new File(newPath$Name);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i;
            while ((i = is.read(temp)) > 0) fos.write(temp, 0, i);
            fos.close();
            is.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void attemptTemplate(File file) {
        String[] names = info_add.getInfo_add3().getValue27().split(" ");
        if (names.length == 2) {
            eLab_API request = HttpUtils.StreamApi();
            Map<String, String> params = new HashMap<>();
            params.put("name1", names[0]);
            params.put("name2", names[1]);
            if (((MyApplication) getApplication()).getToken() == null) {
                token = "Bearer " + sharedPreferences.getString("token", "");
            } else {
                token = "Bearer " + ((MyApplication) getApplication()).getToken();
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("application/msword"),
                    file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename,
                    requestFile);
            Call<ResponseBody> call = request.Template(token, params, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody>
                        response) {
                    Log.v("Template请求", "成功!" + response.code());
                    if (response.code() == 401) {
                        Log.v("Template请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(DetailsActivity.this,
                                LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        String type = response.body().contentType().toString();
                        //MediaType type1 = response.body().contentType();
                        //Log.v("Content-Type", type);
                        if (type.equals("application/octet-stream")) {
                            try {
                                // 创建指定路径的文件
                                File resultfile = new File(modelFilepath);
                                if (resultfile.exists()) {
                                    resultfile.delete();
                                }
                                // 获取文件的输出流对象
                                FileOutputStream outStream = new FileOutputStream
                                        (resultfile);
                                // 获取字符串对象的byte数组并写入文件流
                                outStream.write(response.body().bytes());
                                // 最后关闭文件输出流
                                outStream.close();
                                makeRpt(rptFilepath1);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "FileNotFoundException");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.v("ResponseBody", "ee.IOException");
                            }
                        } else if (type.equals("application/json; charset=utf-8")) {
                            try {
                                Gson gson = new Gson();
                                Template template = gson.fromJson(response.body().string
                                        (), Template.class);
                                if (template != null && template.getStatus().equals("error")) {
                                    DialogUIUtils.dismiss(dialog_make);
                                    Snackbar.make(toolbar, template.getMessage(),
                                            Snackbar.LENGTH_LONG).setAction("Action", null)
                                            .show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.v("" + response.code(), response.message());
                        DialogUIUtils.dismiss(dialog_make);
                        Snackbar.make(toolbar, response.message(),
                                Snackbar.LENGTH_LONG).setAction("Action", null)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("Template请求失败!", t.getMessage());
                    DialogUIUtils.dismiss(dialog_make);
                    Snackbar.make(toolbar, "生成报告失败(获取模板失败)",
                            Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
            });
        } else {
            DialogUIUtils.dismiss(dialog_make);
            Snackbar.make(toolbar, "抽样人格式错误",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void makeRpt(final String rptFilepath) {
        if (btn_update1.getText().toString().equals("修改") && btn_update2.getText()
                .toString().equals("修改") &&
                btn_update3.getText().toString().equals("修改")) {
            doScan(rptFilepath);
            DialogUIUtils.dismiss(dialog_make);
            //try {
            //Thread.sleep(500);
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);
            // 设置Title的图标
            builder.setIcon(R.drawable.ic_launcher);
            // 设置Title的内容
            builder.setTitle("提示");
            // 设置Content来显示一个信息
            builder.setMessage("报告已生成");
            // 设置一个PositiveButton
            builder.setPositiveButton("打印",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            //dialog.dismiss();
                            doPrintWord(rptFilepath);
                        }
                    });
            // 设置一个NegativeButton
            builder.setNegativeButton("预览",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            //dialog.dismiss();
                            doOpenWord(rptFilepath);
                        }
                    });
            // 显示出该对话框
            builder.show();
                /*} catch (Exception e) {
                    e.printStackTrace();
                }*/
        } else {
            Snackbar.make(toolbar, "部分修改未确认",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }

    private void doScan(String rptFilepath) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 3委托单位地址 5任务属性
        map.put("$CJDBH$", info_add.getInfo_add1().getValue1());
        map.put("$RWLY$", info_add.getInfo_add1().getValue2());
        map.put("$RWLB$", info_add.getInfo_add1().getValue4());
        map.put("$DWMC1$", info_add.getInfo_add1().getValue6());
        map.put("$DZ1$", info_add.getInfo_add1().getValue7());
        map.put("$LXR1$", info_add.getInfo_add1().getValue8());
        map.put("$DH1$", info_add.getInfo_add1().getValue9());
        map.put("$CZ1$", info_add.getInfo_add1().getValue10());
        map.put("$YB1$", info_add.getInfo_add1().getValue11());
        // 1所在地
        map.put("$CYDD$", info_add.getInfo_add2().getValue2());
        map.put("$QYLX$", info_add.getInfo_add2().getValue3());
        map.put("$CYHJ$", info_add.getInfo_add2().getValue4());
        map.put("$DWMC2$", info_add.getInfo_add2().getValue5());
        map.put("$DWDZ2$", info_add.getInfo_add2().getValue6());
        map.put("$YYZZ$", info_add.getInfo_add2().getValue7());
        map.put("$QYXKZ$", info_add.getInfo_add2().getValue8() + info_add.getInfo_add2()
                .getValue9());
        map.put("$FRDB$", info_add.getInfo_add2().getValue10());
        map.put("$NXSE$", info_add.getInfo_add2().getValue11());
        map.put("$LXR2$", info_add.getInfo_add2().getValue12());
        map.put("$DH2$", info_add.getInfo_add2().getValue13());
        map.put("$CZ2$", info_add.getInfo_add2().getValue14());
        map.put("$YB2$", info_add.getInfo_add2().getValue15());
        map.put("$SCZMC$", info_add.getInfo_add2().getValue16());
        map.put("$SCZDZ$", info_add.getInfo_add2().getValue17());
        map.put("$SCZLXR$", info_add.getInfo_add2().getValue18());
        map.put("$DH3$", info_add.getInfo_add2().getValue19());
        map.put("$JZRQ$", info_add.getInfo_add2().getValue20());
        map.put("$JSDZ$", info_add.getInfo_add2().getValue21());
        //9样品条码 16原产地 17抽样日期 27抽样人
        map.put("$YPMC$", info_add.getInfo_add3().getValue1());
        map.put("$YPLX$", info_add.getInfo_add3().getValue2());
        map.put("$YPLY$", info_add.getInfo_add3().getValue3());
        map.put("$YPSX$", info_add.getInfo_add3().getValue4());
        map.put("$SB$", info_add.getInfo_add3().getValue5());
        map.put("$BZFL$", info_add.getInfo_add3().getValue6());
        map.put("$GGXH$", info_add.getInfo_add3().getValue7());
        map.put("$ZLDJ$", info_add.getInfo_add3().getValue8());
        map.put("$RQLX$", info_add.getInfo_add3().getValue10());
        map.put("$SCRQ$", info_add.getInfo_add3().getValue11());
        map.put("$BZQ$", info_add.getInfo_add3().getValue12());
        map.put("$CPPH$", info_add.getInfo_add3().getValue13());
        map.put("$DJ$", info_add.getInfo_add3().getValue14());
        map.put("$SFCK$", info_add.getInfo_add3().getValue15());
        map.put("$CYFS$", info_add.getInfo_add3().getValue18());
        map.put("$YPXT$", info_add.getInfo_add3().getValue19());
        map.put("$YPBZ$", info_add.getInfo_add3().getValue20());
        map.put("$CCTJ$", info_add.getInfo_add3().getValue21());
        map.put("$ZXBZ$", info_add.getInfo_add3().getValue22());
        if (info_add.getInfo_add3().getValue23() == null) {
            map.put("$CYJS$", info_add.getInfo_add3().getValue24());
            map.put("$BYSL$", info_add.getInfo_add3().getValue25());
            map.put("$CYSL$", info_add.getInfo_add3().getValue26());
        } else {
            map.put("$CYJS$", info_add.getInfo_add3().getValue24() + info_add.getInfo_add3()
                    .getValue23());
            map.put("$BYSL$", info_add.getInfo_add3().getValue25() + info_add.getInfo_add3()
                    .getValue23());
            map.put("$CYSL$", info_add.getInfo_add3().getValue26() + info_add.getInfo_add3()
                    .getValue23());
        }
        map.put("$YPXKZ$", info_add.getInfo_add3().getValue28());
        map.put("$BZ$", info_add.getInfo_add3().getValue29());
        map.put("$DYRQ$", info_add.getInfo_add3().getValue17());

        /* android无法插入图片*/
        writeDoc(modelFilepath, rptFilepath, map);
        /* 查看 doOpenWord();*/
    }

    //demoFile 模板文件 newFile 生成文件 map 要填充的数据
    public void writeDoc(String modelPath, String outFilePath, Map<String, Object> map) {
        if (new File(outFilePath).exists()) {
            new File(outFilePath).delete();
        }
        try {
            InputStream in = new FileInputStream(modelPath);
            //InputStream in = getClass().getResourceAsStream("/assets/" + demopath);
            HWPFDocument hdt = new HWPFDocument(in);
            Range range = hdt.getRange();/* 替换文本内容*/
            for (Map.Entry<String, Object> entry : map.entrySet())
                if ((entry.getValue()) instanceof String) {
                    // 替换文本
                    range.replaceText(entry.getKey(), entry.getValue().toString());
                }
            /*//获取doc中的书签
            Bookmarks bookmarks = hdt.getBookmarks();
            Log.v("书签数量：", "" + bookmarks.getBookmarksCount());*/

            //获取doc中的图片数
            List<Picture> pics = hdt.getPicturesTable().getAllPictures();
            System.out.printf("word中的pic数量:" + pics.size() + "\n");

            OutputStream os = new FileOutputStream(outFilePath);
            /* ByteArrayOutputStream ostream = new
            ByteArrayOutputStream(); FileOutputStream out = new FileOutputStream(newFile, true);*/
            hdt.write(os);
            /* 1.通过Picture的writeImageContent方法 写文件 2.获取Picture的byte 自己写*/
            copyPic2Disk(pics, os);
            os.close();
            in.close();
            /* 输出字节流 out.write(ostream.toByteArray()); out.close(); ostream.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //调用手机中安装的可打开word的软件
    private void doOpenWord(final String rptFilepath) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                String fileMimeType = "application/msword";
                intent.setDataAndType(Uri.fromFile(new File(rptFilepath)), fileMimeType);
                /*intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Rpt/doc/" +
                                filename)),
                        fileMimeType);*/
                try {
                    DetailsActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    /* 检测到系统尚未安装OliveOffice的apk程序*/
                    Snackbar.make(toolbar, "未找到可用软件",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        }, 1500);
        /* 延时1.5s执行*/
    }

    private void doPrintWord(final String rptFilepath) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appIsInstalled(_context, "com.dynamixsoftware.printershare")) {
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.dynamixsoftware" +
                            ".printershare", "com" +
                            ".dynamixsoftware.printershare.ActivityPrintDocuments");
                    intent.setComponent(comp);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setType("application/doc");
                    intent.setData(Uri.fromFile(new File(rptFilepath)));
                    /*intent.setData(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory() +
                            "/doc/" + filename)));*/
                    dbmanage.updateNumber(number, 1, 1, 0);
                    startActivity(intent);
                } else {
                    Snackbar.make(toolbar, "未找到PrinterShare软件",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    /* 安装apk*/
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = getAssetFileToCacheDir(_context, "PrinterShare.apk");
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd" +
                            ".android.package-archive");
                    DetailsActivity.this.startActivity(intent);
                }
            }
        }, 2000); /* 延时2s执行*/
    }

    // 通过Picture 自己类中的读写方法 <p> pics path
    public static void copyPic2Disk(List<Picture> pics, OutputStream os) {
        if (pics == null || pics.size() <= 0)
            return; /* if(!path.isDirectory()){ throw new RuntimeException("路径填写不正确"); }
            //当文件夹路径不存在的情况下，我们自己创建文件夹目录
            if(!path.exists() ){ path.mkdirs(); } */
        try {
            for (Picture pic : pics) {/* 写出数据，我们使用的是Poi类中，Picture自己所带的函数*/
                // 图片在doc文件中的位置,分析Doc 转化成其他文本时需要用到
                /*int start = pic.getStartOffset();
                int width = pic.getWidth();
                int height = pic.getHeight();
                String mimeType = pic.getMimeType();
                System.out.printf("开始位置%d\t图片大小度%d,高%d,\t图片类型%s\r\n",
                        start, width, height, mimeType);*/

                pic.writeImageContent(os);
                /* byte [] picBytes = pic.getContent();
                //获取字节流，也可以自己写入数据 copyByteToFile
                (picBytes); */
            }
        } catch (Exception e) {/* TODO Auto-generated catch block*/
            e.printStackTrace();
        }
    }

    // 判断apk是否安装
    public static boolean appIsInstalled(Context context, String pageName) {
        try {
            context.getPackageManager().getPackageInfo(pageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    // 把Asset下的apk拷贝到sdcard下 /Android/data/你的包名/cache 目录下
    public File getAssetFileToCacheDir(Context context, String fileName) {
        try {
            File cacheDir = getCacheDir(context);
            final String cachePath = cacheDir.getAbsolutePath() + File.separator + fileName;
            InputStream is = context.getAssets().open(fileName);
            File file = new File(cachePath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) fos.write(temp, 0, i);
            fos.close();
            is.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取sdcard中的缓存目录
    public static File getCacheDir(Context context) {
        String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/";
        File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    public void FormatData() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Info_add1 info_add1 = info_add.getInfo_add1();
        Info_add2 info_add2 = info_add.getInfo_add2();
        Info_add3 info_add3 = info_add.getInfo_add3();

        Field[] fields_1 = info_add1.getClass().getDeclaredFields();
        String name;
        Field f;
        Object value;
        for (int i = 0; i < fields_1.length; i++) {
            f = fields_1[i];
            f.setAccessible(true);
            // 获取属性的名字
            name = f.getName();
            // 获取属性类型
            //type = f.getGenericType().toString();
            // 将属性的首字母大写
            //name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
            Method getMethod = info_add1.getClass().getMethod("get" + name);
            //Log.v("getMethod", getMethod.getName().toString());
            value = getMethod.invoke(info_add1);
            if (value == null) {
                Method setMethod = info_add1.getClass().getMethod("set" + name, String.class);
                setMethod.invoke(info_add1, "");
            }
        }

        Field[] fields_2 = info_add2.getClass().getDeclaredFields();
        for (int i = 0; i < fields_2.length; i++) {
            f = fields_2[i];
            f.setAccessible(true);
            // 获取属性的名字
            name = f.getName();
            // 获取属性类型
            //type = f.getGenericType().toString();
            // 将属性的首字母大写
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                    .toUpperCase());
            Method getMethod = info_add2.getClass().getMethod("get" + name);
            //Log.v("getMethod", getMethod.getName().toString());
            value = getMethod.invoke(info_add2);
            if (value == null) {
                Method setMethod = info_add2.getClass().getMethod("set" + name, String.class);
                setMethod.invoke(info_add2, "");
            }
        }

        Field[] fields_3 = info_add3.getClass().getDeclaredFields();
        for (int i = 0; i < fields_3.length; i++) {
            f = fields_3[i];
            f.setAccessible(true);
            // 获取属性的名字
            name = f.getName();
            // 获取属性类型
            //type = f.getGenericType().toString();
            // 将属性的首字母大写
            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                    .toUpperCase());
            Method getMethod = info_add3.getClass().getMethod("get" + name);
            //Log.v("getMethod", getMethod.getName().toString());
            value = getMethod.invoke(info_add3);
            if (value == null) {
                Method setMethod = info_add3.getClass().getMethod("set" + name, String.class);
                setMethod.invoke(info_add3, "");
            }
        }
        info_add.setInfo_add1(info_add1);
        info_add.setInfo_add2(info_add2);
        info_add.setInfo_add3(info_add3);
    }

    // 刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        list_paths = dbmanage.findList_UploadImage(number);
        ada_upload_img.changList_add(list_paths);
    }
}
