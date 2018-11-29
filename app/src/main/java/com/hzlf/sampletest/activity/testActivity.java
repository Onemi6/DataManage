package com.hzlf.sampletest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.http.Post_Login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class testActivity extends AppCompatActivity {
    private Button btn_test;
    private TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn_test = findViewById(R.id.btn_test);
        tv_test = findViewById(R.id.tv_test);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_login();
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
        Post_Login request = retrofit.create(Post_Login.class);
        Call<ResponseBody> call = request.getCall(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    tv_test.setText(tv_test.getText() + "请求成功!" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tv_test.setText(tv_test.getText() + "请求失败!" + t.getMessage());
            }
        });
    }
}
