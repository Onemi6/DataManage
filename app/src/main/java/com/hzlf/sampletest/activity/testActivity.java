package com.hzlf.sampletest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.model.Status;
import com.hzlf.sampletest.http.eLab_API;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class testActivity extends AppCompatActivity {
    private Button btn_test;
    private TextView tv_test;

    private Button btn_scanner;
    private TextView tv_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn_test = findViewById(R.id.btn_test);
        tv_test = findViewById(R.id.tv_test);

        btn_scanner = findViewById(R.id.btn_test_scaner);
        tv_result = findViewById(R.id.tv_result);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_login();
            }
        });

        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(testActivity.this);
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
                integrator.setPrompt("请扫描一维码"); //底部的提示文字，设为""可以置空
                //integrator.setCameraId(0); //前置或者后置摄像头
                //integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                //integrator.setBarcodeImageEnabled(false); // 扫完码之后生成二维码的图片
                integrator.initiateScan();
            }
        });
    }

    public void request_login() {

        Map<String, Object> map = new HashMap<>();
        map.put("loginName", "123");
        map.put("passWord", "123");

        Gson gson = new Gson();
        String obj = gson.toJson(map);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.3tpi.com:8016/")
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                obj);
        eLab_API request = retrofit.create(eLab_API.class);
        Call<Status> call = request.Login(body);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                tv_test.setText(tv_test.getText() + "请求成功!" + response.body().toString());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                tv_test.setText(tv_test.getText() + "请求失败!" + t.getMessage());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Log.v("result", result);
            tv_result.setText(tv_result.getText() + result);
        } else {
            tv_result.setText(tv_result.getText() + "result is null");
        }
    }
}
