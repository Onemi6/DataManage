package com.hzlf.sampletest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.Codes;
import com.hzlf.sampletest.others.MyApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeActivity extends Activity {

    private static final int CODE_TRUE = 1;
    private static final int CODE_FLASE = 0;
    private DBManage dbmanage = new DBManage(this);
    private List<String> numberlist = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String num, token;
    private Button btn_apply;
    private EditText input_number;
    private TextView text_number;
    private ListView list_number;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_code);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        initNumber();
        list_number.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                String number = numberlist.get(position);
                Intent intent_add = new Intent();
                intent_add
                        .setClass(CodeActivity.this, AddActivity.class);
                intent_add.putExtra("number", number);
                finish();
                startActivity(intent_add);
            }
        });
        // 调用接口,得到code
        btn_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                input_number.requestFocus();
                num = input_number.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    input_number.setError("数量不能为空");
                } else if (num.equals("0")) {
                    input_number.setError("数量不能为0");
                } else {
                    attempCode();
                }
            }
        });
    }

    public void attempCode() {
        eLab_API request = HttpUtils.GsonApi();
        if (((MyApplication) getApplication()).getToken() == null) {
            token = "Bearer " + sharedPreferences.getString("token", "");
        } else {
            token = "Bearer " + ((MyApplication) getApplication()).getToken();
        }
        Call<Codes> call = request.Code(token, num);
        call.enqueue(new Callback<Codes>() {
            @Override
            public void onResponse(Call<Codes> call, Response<Codes> response) {
                if (response.code() == 401) {
                    Log.v("Code请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(CodeActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            Message message = new Message();
                            message.what = CODE_TRUE;
                            message.obj = response.body().getCodes();
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = CODE_FLASE;
                            message.obj = response.body().getMessage();
                            handler.sendMessage(message);
                        }
                    } else {
                        Log.v("Code请求成功!", "response.code is null");
                    }
                    input_number.getText().clear();
                }
            }

            @Override
            public void onFailure(Call<Codes> call, Throwable t) {
                Log.v("Code请求失败!", t.getMessage());
            }
        });
    }

    // 初始化number
    private void initNumber() {
        // TODO 自动生成的方法存根
        input_number = findViewById(R.id.input_number);
        btn_apply = findViewById(R.id.btn_applynumber);
        text_number = findViewById(R.id.text_number);
        list_number = findViewById(R.id.list_number);
        numberlist = dbmanage.findList_Number(1);

        list_number.setEmptyView(text_number);
        adapter = new ArrayAdapter<>(CodeActivity.this,
                android.R.layout.simple_list_item_1, numberlist);
        list_number.setAdapter(adapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_TRUE:
                    String[] codes = ((String) msg.obj).split("[,]");
                    for (String code : codes) {
                        if (dbmanage.checkNumber(code) == 0) {
                            dbmanage.addSampleNumber(code);
                            numberlist.add(code);
                            /* numberlist.add(0, code); */
                        }
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(CodeActivity.this, "申请编号成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case CODE_FLASE:
                    Toast.makeText(CodeActivity.this, (String) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}