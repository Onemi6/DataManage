package com.hzlf.sampletest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.entityclass.Status;
import com.hzlf.sampletest.http.Post_Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.3tpi.com:8016")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Post_Login request = retrofit.create(Post_Login.class);
        Call<Status> call = request.getCall("zhouzy", "123");
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body() != null) {
                    tv_test.setText(tv_test.getText() + "请求成功!" + response.body().toString());
                } else {
                    tv_test.setText(tv_test.getText() + "请求成功!" + "NULL");
                }
                //System.out.println("请求成功");
                //System.out.print(response.body().getStatus());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                tv_test.setText(tv_test.getText() + "请求失败!" + t.getMessage());
                //System.out.println("请求失败");
                // System.out.print(t.getMessage());
            }
        });
    }
}
