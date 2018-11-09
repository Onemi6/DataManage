package com.hzlf.sampletest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.Codes;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.UsedPath;

import java.util.ArrayList;
import java.util.List;

public class ApplyNumberActivity extends Activity {

    private static final int APPLY_TRUE = 1;
    private static final int APPLY_FLASE = 0;
    private DBManage dbmanage = new DBManage(this);
    private List<String> numberlist = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String no;
    private Button btn_apply;
    private EditText input_number;
    private TextView text_number;
    private ListView list_number;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APPLY_TRUE:
                    String[] codes = ((String) msg.obj).split("[,]");
                    for (String code : codes) {
                        dbmanage.addSampleNumber(code);
                        numberlist.add(code);
                        /* numberlist.add(0, code); */
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ApplyNumberActivity.this, "申请编号成功",
                            Toast.LENGTH_SHORT).show();
                    input_number.getText().clear();
                    break;
                case APPLY_FLASE:
                    Toast.makeText(ApplyNumberActivity.this, (String) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_apply_number);
        initNumber();
    }

    // 初始化number
    private void initNumber() {
        // TODO 自动生成的方法存根
        no = ((MyApplication) getApplication()).getNo();
        input_number = findViewById(R.id.input_number);
        btn_apply = findViewById(R.id.btn_applynumber);
        text_number = findViewById(R.id.text_number);
        list_number = findViewById(R.id.list_number);
        numberlist = dbmanage.findList_Number(1);

        list_number.setEmptyView(text_number);
        adapter = new ArrayAdapter<String>(ApplyNumberActivity.this,
                android.R.layout.simple_list_item_1, numberlist);

        list_number.setAdapter(adapter);
        list_number.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                String number = numberlist.get(position);
                Intent intent_add = new Intent();
                intent_add
                        .setClass(ApplyNumberActivity.this, AddActivity.class);
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
                if (input_number.getText().toString().equals("")) {
                    Toast.makeText(ApplyNumberActivity.this, "数量不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String path = UsedPath.api_Code_GET + "no=" + no
                                    + "&num="
                                    + input_number.getText().toString();
                            String result = HttpUtils.getCode(path);

                            if (result.equals("获取数据失败") || result.equals("")) {
                                Log.d("result", "1" + result);
                                Message message = new Message();
                                message.what = APPLY_FLASE;
                                message.obj = "获取编号失败";
                                handler.sendMessage(message);
                            } else {
                                Gson gson = new Gson();
                                Codes codes = gson
                                        .fromJson(result, Codes.class);
                                if (codes.getStatus().equals("success")) {
                                    Message message = new Message();
                                    message.what = APPLY_TRUE;
                                    message.obj = codes.getCodes();
                                    handler.sendMessage(message);
                                } else {
                                    Message message = new Message();
                                    message.what = APPLY_FLASE;
                                    message.obj = codes.getMessage();
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
