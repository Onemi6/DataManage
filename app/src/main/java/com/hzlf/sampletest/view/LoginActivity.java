package com.hzlf.sampletest.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.Status;
import com.hzlf.sampletest.others.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int login_type;
    private String account, password;
    private DBManage dbmanage = new DBManage(this);
    /*定义一个list，用于存储需要申请的权限*/
    private ArrayList<String> permissionList = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        login_type = intent.getIntExtra("login_type", 0);
        _context = this;
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        initView();
        FileDir();
        getPermission();
        if (login_type == 1) {
            account = sharedPreferences.getString("ZHANGHAO", null);
            password = sharedPreferences.getString("MIMA", null);
            attemptLogin();
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
                            attemptLogin();
                        } else {
                            localLogin();
                        }
                    }
                }
            }
        });
    }

    //初始化
    public void initView() {
        input_zhanghao = findViewById(R.id.input_zhanghao);
        input_mima = findViewById(R.id.input_mima);
        remember_mima = findViewById(R.id.remember_mima);
        btn_denglu = findViewById(R.id.btn_denglu);
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
    public void attemptLogin() {
        final BuildBean dialog_login = DialogUIUtils.showLoading(_context, "登录中...", false,
                true, false,
                false);
        dialog_login.show();
        try {
            PackageManager packageManager = getPackageManager();
            /* getPackageName()是你当前类的包名，0代表是获取版本信息*/
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionname = packInfo.versionName;
            Map<String, Object> map = new HashMap<>();
            map.put("loginName", account);
            map.put("passWord", password);
            map.put("version", versionname);
            String obj = new Gson().toJson(map);
            //eLab_API request = HttpUtils.GsonApi();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; " +
                    "charset=utf-8"), obj);
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
                                    Snackbar.make(btn_denglu, "登录成功",
                                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(LoginActivity.this,
                                                    MainActivity
                                                            .class));
                                            finish();
                                        }
                                    }, 300); /* 延时1s执行*/
                                }
                            } else if (response.body().getStatus().equals("error")) {
                                Snackbar.make(btn_denglu, response.body().getMessage(),
                                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        } else {
                            Snackbar.make(btn_denglu, "登录失败(请求成功)",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    } else {
                        Snackbar.make(btn_denglu, "登录失败(code:" + response.code() + ")",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    DialogUIUtils.dismiss(dialog_login);
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Log.v("Login请求失败", t.getMessage());
                    if (login_type == -1) {
                        localLogin();
                    }
                    DialogUIUtils.dismiss(dialog_login);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("version", "获取APP版本出错");
            e.printStackTrace();
        }
    }

    //没网、网络不可用 本地登录
    public void localLogin() {
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
                Snackbar.make(btn_denglu, "登录成功",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {/* do something*/
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
            Snackbar.make(btn_denglu, "请到网络良好的地方再进行尝试!",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void getPermission() {
        permissionList.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissionList.add(Manifest.permission.VIBRATE);
        permissionList.add(Manifest.permission.CAMERA);
        permissionList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        permissionList.add(Manifest.permission.READ_LOGS);
        checkAndRequestPermissions(permissionList);
    }

    private void checkAndRequestPermissions(ArrayList<String> permissionList) {
        ArrayList<String> list = new ArrayList<>(permissionList);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String permission = it.next();/*检查权限是否已经申请*/
            int hasPermission = ContextCompat.checkSelfPermission(this, permission);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) it.remove();
        }
        /**
         *补充说明：ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
         * .RECORD_AUDIO);
         *对于原生Android，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale就会为true。
         *此时，用户可以弹出一个对话框，向用户解释为什么需要这项权限。
         *对于一些深度定制的系统，如果用户选择了“不再提示”，那么shouldShowRequestPermissionRationale永远为false
         */
        if (list.size() == 0) {
            return;
        }
        String[] permissions = list.toArray(new String[0]);
        //正式请求权限
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
    }

    public void FileDir() {
        boolean sdCardExist = android.os.Environment.getExternalStorageState().equals(android.os
                .Environment.MEDIA_MOUNTED);
        String DOC_PATH, DOC2_PATH, MODEL_PATH, CRASH_PATH;
        if (sdCardExist) {
            DOC_PATH = Environment.getExternalStorageDirectory() + "/DataManage/doc/";
            DOC2_PATH = Environment.getExternalStorageDirectory() + "/DataManage/doc2/";
            MODEL_PATH = Environment.getExternalStorageDirectory() + "/DataManage/model/";
            CRASH_PATH = Environment.getExternalStorageDirectory() + "/DataManage/crash/";
        } else {
            DOC_PATH = DOC2_PATH = MODEL_PATH = CRASH_PATH = this.getCacheDir().toString() + "/";
        }
        File doc = new File(DOC_PATH),doc2 = new File(DOC2_PATH), model = new File(MODEL_PATH), crash = new File(CRASH_PATH);
        if (!doc.exists()) {
            doc.mkdirs();
        }
        if (!doc2.exists()) {
            doc2.mkdirs();
        }
        if (!model.exists()) {
            model.mkdirs();
        }
        if (!crash.exists()) {
            crash.mkdirs();
        }
    }
}