package com.hzlf.sampletest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.model.Status;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.others.MyApplication;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Context _context;
    private EditText input_zhanghao, input_mima;
    private Button btn_denglu;
    private CheckBox remember_mima;
    private ProgressBar progressbar_login;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int login_type;
    private String account, password;
    private DBManage dbmanage = new DBManage(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        login_type = intent.getIntExtra("login_type", 0);
        _context = this;
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        initview();
        if (login_type == 1) {
            account = sharedPreferences.getString("ZHANGHAO", null);
            password = sharedPreferences.getString("MIMA", null);
            Log.v("", account + "," + password);
            attempLogin();
        }
        // 登录
        btn_denglu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {/* TODO 自动生成的方法存根*/
                account = input_zhanghao.getText().toString();
                password = input_mima.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    input_zhanghao.requestFocus();
                    input_zhanghao.setError("账号不能为空");
                } else {
                    input_zhanghao.setError(null);
                    if (TextUtils.isEmpty(password)) {
                        input_mima.requestFocus();
                        input_mima.setError("密码不能为空");
                    } else {
                        input_mima.setError(null);
                        if (NetworkUtil.isNetworkAvailable(_context)) {
                            attempLogin();
                        } else {
                            localLogin();
                        }
                    }
                }
            }
        });
    }

    //初始化
    public void initview() {
        input_zhanghao = findViewById(R.id.input_zhanghao);
        input_mima = findViewById(R.id.input_mima);
        remember_mima = findViewById(R.id.remember_mima);
        btn_denglu = findViewById(R.id.btn_denglu);
        progressbar_login = findViewById(R.id.progressbar_login);
        if (sharedPreferences != null) {
            boolean isremember = sharedPreferences.getBoolean("remember_mima", false);
            if (isremember) {
                input_zhanghao.setText(sharedPreferences.getString("ZHANGHAO", null));
                input_mima.setText(sharedPreferences.getString("MIMA", null));
                remember_mima.setChecked(true);
            } else {
                input_zhanghao.setText(sharedPreferences.getString("ZHANGHAO", null));
                //input_mima.setText(sharedPreferences.getString("MIMA",null));
                remember_mima.setChecked(false);
            }
        }
    }

    //有网时登录
    public void attempLogin() {
        progressbar_login.setVisibility(View.VISIBLE);
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", account);
        map.put("passWord", password);
        String obj = new Gson().toJson(map);
        //eLab_API request = HttpUtils.GsonApi();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; " +
                        "charset=utf-8"),
                obj);
        eLab_API request = HttpUtils.GsonApi();
        Call<Status> call = request.Login(body);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("success")) {
                            editor = sharedPreferences.edit();
                            if (remember_mima.isChecked()) {
                                editor.putBoolean("remember_mima", true);
                                editor.putString("ZHANGHAO", account);
                                editor.putString("MIMA", password);
                                editor.putString("NO", response.body().getNo());
                                editor.putString("NAME", response.body().getName());
                                editor.putString("token", response.body().getToken());
                            } else {
                                editor.putBoolean("remember_mima", false);
                                editor.putString("ZHANGHAO", account);
                                editor.putString("MIMA", password);
                                editor.putString("NO", response.body().getNo());
                                editor.putString("NAME", response.body().getName());
                                editor.putString("token", response.body().getToken());
                            }
                            editor.apply();
                            ((MyApplication) getApplication()).setNo(response.body()
                                    .getNo());
                            ((MyApplication) getApplication()).setName(response.body()
                                    .getName());
                            ((MyApplication) getApplication()).setToken(response.body()
                                    .getToken());
                            if (login_type == 1) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 500); // 延时1s执行
                            } else if (login_type == -1) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast
                                        .LENGTH_SHORT)
                                        .show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(LoginActivity.this,
                                                MainActivity
                                                        .class));
                                        finish();
                                    }
                                }, 1000); /* 延时1s执行*/
                            }
                        } else if (response.body().getStatus().equals("error")) {
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast
                                    .LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
                progressbar_login.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                progressbar_login.setVisibility(View.GONE);
                Log.v("Login请求失败", t.getMessage());
                if (login_type == -1) {
                    localLogin();
                }
            }
        });
    }

    //没网、网络不可用 本地登录
    public void localLogin() {
        progressbar_login.setVisibility(View.VISIBLE);
        if (login_type == -1) {
            if ((dbmanage.check(account) != null) && (dbmanage.check(account).equals(password))) {
                editor = sharedPreferences.edit();
                String[] info = dbmanage.getSomeInfo(account).split(",");
                if (remember_mima.isChecked()) {
                    editor.putBoolean("remember_mima", true);
                    editor.putString("ZHANGHAO", account);
                    editor.putString("MIMA", password);
                    editor.putString("NO", info[0]);
                    editor.putString("NAME", info[1]);
                } else {
                    editor.putBoolean("remember_mima", false);
                    editor.putString("ZHANGHAO", account);
                    editor.putString("MIMA", password);
                    editor.putString("NO", info[0]);
                    editor.putString("NAME", info[1]);
                }
                editor.apply();
                ((MyApplication) getApplication()).setNo(info[0]);
                ((MyApplication) getApplication()).setName(info[1]);
                Toast.makeText(LoginActivity.this, "登录成功", Toast
                        .LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {/* do something*/
                        progressbar_login.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this,
                                MainActivity
                                        .class));
                        finish();
                    }
                }, 1000); /* 延时1s执行*/
            } else if (dbmanage.check(account) == null) {
                input_zhanghao.requestFocus();
                input_zhanghao.setError("此用户不存在!");
            } else if (!dbmanage.check(account).equals(password)) {
                input_mima.requestFocus();
                input_mima.setError("密码不正确");
            }
        } else {
            Toast.makeText(LoginActivity.this, "请到网络良好的地方再进行尝试!", Toast
                    .LENGTH_SHORT)
                    .show();
        }
        progressbar_login.setVisibility(View.GONE);
    }
}