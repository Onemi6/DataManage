package com.hzlf.sampletest.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.Apply;
import com.hzlf.sampletest.model.Info_add;
import com.hzlf.sampletest.model.Info_add1;
import com.hzlf.sampletest.model.Info_add2;
import com.hzlf.sampletest.model.Info_add3;
import com.hzlf.sampletest.model.UpdateInfo;
import com.hzlf.sampletest.model.User;
import com.hzlf.sampletest.others.MyApplication;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {

    private static String versionname, TAG_UPDATE = "AppUpdate", token, no;
    private static UpdateInfo info;
    private TextView tv_app_version;
    private Button version_update, btn_tongbu_emp, btn_tongbu_apply;
    private ProgressBar progressbar_tongbu;
    private Toolbar toolbar;
    private PackageInfo packInfo;
    private Context context;
    private DBManage dbmanage = new DBManage(this);
    private SharedPreferences sharedPreferences;
    private ProgressDialog pd;
    private DownloadManager mDownloadManager;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        context = this;
        tv_app_version = findViewById(R.id.app_version);
        version_update = findViewById(R.id.version_update);
        btn_tongbu_emp = findViewById(R.id.btn_tongbu_emp);
        btn_tongbu_apply = findViewById(R.id.btn_tongbu_apply);
        progressbar_tongbu = findViewById(R.id.progressbar_tongbu);
        toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);
        try {
            packInfo = getPackageInfo();
            versionname = packInfo.versionName;
            tv_app_version.setText(String.format(getResources().getString(R.string.app_version),
                    versionname));
        } catch (Exception e) {
            e.printStackTrace();
        }
        version_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                //从服务器获取xml解析并进行比对版本号
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        attempUpdate();
                    }
                }).start();
            }
        });

        btn_tongbu_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptEmp();
            }
        });

        btn_tongbu_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptApply();
            }
        });
    }

    public void attemptEmp() {
        progressbar_tongbu.setVisibility(View.VISIBLE);
        eLab_API request = HttpUtils.GsonApi();
        if (((MyApplication) getApplication()).getToken() == null) {
            token = "Bearer " + sharedPreferences.getString("token", "");
        } else {
            token = "Bearer " + ((MyApplication) getApplication()).getToken();
        }
        Call<List<User>> call = request.Emp(token);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 401) {
                    Log.v("Emp请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(AboutActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().size() != 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                User user = response.body().get(i);
                                if (dbmanage.finduser(user.getNO()) != null) {
                                    if (dbmanage.finduser(user.getNO()).getTIME_STAMP() != user
                                            .getTIME_STAMP()) {
                                        dbmanage.updateuser(user);
                                    }
                                } else {
                                    dbmanage.adduser(user);
                                }
                            }
                            Snackbar.make(btn_tongbu_emp, "同步成功",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    } else {
                        Log.v("Emp请求成功!", "response.body is null");
                        Snackbar.make(btn_tongbu_emp, "同步失败，请稍后再试!",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
                progressbar_tongbu.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressbar_tongbu.setVisibility(View.GONE);
                Log.v("Emp请求失败!", t.getMessage());
                Snackbar.make(btn_tongbu_emp, "同步失败，请稍后再试!",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        });

    }

    public void attemptApply() {
        progressbar_tongbu.setVisibility(View.VISIBLE);
        eLab_API request = HttpUtils.GsonApi();
        if (((MyApplication) getApplication()).getToken() == null) {
            token = "Bearer " + sharedPreferences.getString("token", "");
        } else {
            token = "Bearer " + ((MyApplication) getApplication()).getToken();
        }
        Call<List<Apply>> call = request.Apply(token);
        call.enqueue(new Callback<List<Apply>>() {
            @Override
            public void onResponse(Call<List<Apply>> call, Response<List<Apply>> response) {
                if (response.code() == 401) {
                    Log.v("Apply请求", "token过期");
                    Intent intent_login = new Intent();
                    intent_login.setClass(AboutActivity.this,
                            LoginActivity.class);
                    intent_login.putExtra("login_type", 1);
                    startActivity(intent_login);
                } else if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().size() != 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                Apply apply = response.body().get(i);
                                if (dbmanage.checkApply(apply.getSAMPLING_NO()) == 0) {
                                    Info_add1 info_add1 = new Info_add1();
                                    Info_add2 info_add2 = new Info_add2();
                                    Info_add3 info_add3 = new Info_add3();

                                    info_add3.setValue1(apply.getGOODS_NAME());
                                    info_add1.setValue2(apply.getBUSINESS_SOURCE());
                                    info_add2.setValue16(apply.getMANU_COMPANY());
                                    info_add3.setValue3(apply.getSAMPLE_SOURCE());
                                    info_add3.setValue15(apply.getI_AND_O());
                                    info_add2.setValue3(apply.getDOMESTIC_AREA());
                                    info_add3.setValue19(apply.getSAMPLE_STATUS());
                                    info_add3.setValue29(apply.getREMARK());
                                    info_add3.setValue5(apply.getTRADEMARK());
                                    info_add3.setValue20(apply.getPACK());
                                    info_add3.setValue8(apply.getSAMPLE_CLASS());
                                    info_add3.setValue7(apply.getSAMPLE_MODEL());
                                    info_add1.setValue6(apply.getDRAW_ORG());
                                    info_add3.setValue24(apply.getDRAW_NUM());

                                    String[] str = apply.getDRAW_ADDR().split(":");
                                    if (str.length == 1) {
                                        info_add2.setValue4("生成环节");
                                        info_add2.setValue2(str[0]);
                                    } else {
                                        info_add2.setValue4(str[0]);
                                        info_add2.setValue2(str[1]);
                                    }

                                    info_add3.setValue11(apply.getDATE_PRODUCT());
                                    info_add2.setValue5(apply.getSUPPLIER());
                                    info_add1.setValue1(apply.getSAMPLING_NO());
                                    info_add3.setValue12(apply.getEXPIRATIONDATE());
                                    info_add2
                                            .setValue19(apply.getMANU_COMPANY_PHONE());
                                    info_add2.setValue13(apply.getSUPPLIER_PHONE());
                                    info_add3.setValue21(apply.getSAVE_MODE());
                                    info_add2
                                            .setValue17(apply.getMANU_COMPANY_ADDR());
                                    info_add3.setValue25(apply.getSTORAGESITE());
                                    info_add2.setValue12
                                            (apply.getSUPPLIER_PERSON());
                                    info_add2.setValue6(apply.getSUPPLIER_ADDR());
                                    info_add2.setValue10(apply.getSUPPLIER_LEGAL());
                                    info_add2.setValue14(apply.getSUPPLIER_FAX());
                                    info_add1.setValue4(apply.getSAMPLE_TYPE());
                                    info_add2.setValue11(apply.getANNUAL_SALES());
                                    info_add2.setValue7(apply.getBUSINESS_LICENCE());
                                    info_add2.setValue8(apply.getPERMIT_TYPE());
                                    info_add2.setValue9(apply.getPERMIT_NUM());
                                    info_add2
                                            .setValue15(apply.getSUPPLIER_ZIPCODE());
                                    info_add3.setValue4(apply.getSAMPLE_PROPERTY());
                                    info_add3.setValue2(apply.getSAMPLE_STYLE());
                                    info_add3.setValue13(apply.getSAMPLE_NUMBER());
                                    info_add3
                                            .setValue28(apply.getPRODUCTION_CERTIFICATE());
                                    info_add3.setValue14(apply.getUNIVALENT());
                                    info_add3.setValue6(apply.getPACK_TYPE());
                                    info_add2
                                            .setValue20(apply.getSAMPLE_CLOSE_DATE());
                                    info_add3.setValue18(apply.getDRAW_METHOD());
                                    info_add1.setValue8(apply.getDRAW_PERSON());
                                    info_add1.setValue9(apply.getDRAW_PHONE());
                                    info_add1.setValue10(apply.getDRAW_FAX());
                                    info_add1.setValue11(apply.getDRAW_ZIPCODE());
                                    info_add1.setValue7(apply.getDRAW_ORG_ADDR());
                                    info_add3.setValue26(apply.getDRAW_AMOUNT());
                                    info_add3.setValue22(apply.getTEST_FILE_NO());
                                    info_add3
                                            .setValue10(apply.getDATE_PRODUCT_TYPE());
                                    info_add3.setValue27(apply.getDRAW_MAN());
                                    info_add3.setValue17(apply.getDRAW_DATE());
                                    info_add1.setValue3(apply.getCLIENT_ADDR());
                                    info_add1.setValue5(apply.getTASK_REMARK());
                                    info_add2.setValue1(apply.getC_ADDR());
                                    info_add3.setValue9(apply.getSAMPLE_CODE());
                                    info_add3.setValue16(apply.getS_ADDR());
                                    info_add2.setValue18(apply.getSAMPLE_ADDR());

                                    if (info_add2.getValue1() == null) {
                                        info_add2.setValue1("浙江宁波海曙区");
                                    }
                                    if (info_add2.getValue21() == null) {
                                        info_add2.setValue21("/");
                                    }
                                    if (info_add2.getValue20() == null) {
                                        info_add2.setValue20("/");
                                    }
                                    if (info_add2.getValue19() == null) {
                                        info_add2.setValue19("/");
                                    }
                                    if (info_add2.getValue18() == null) {
                                        info_add2.setValue18("/");
                                    }
                                    if (info_add3.getValue9() == null) {
                                        info_add3.setValue9("/");
                                    }
                                    if (info_add3.getValue16() == null) {
                                        info_add3.setValue16("中国");
                                    }
                                    if (((MyApplication) getApplication()).getNo() == null) {
                                        no = sharedPreferences.getString("NO", null);
                                    } else {
                                        no = ((MyApplication) getApplication()).getNo();
                                    }
                                    dbmanage.addInfo(no, info_add1, info_add2
                                            , info_add3);
                                    if (dbmanage.checkNumber(apply.getSAMPLING_NO()) == 0) {
                                        dbmanage.addSampleNumber(apply.getSAMPLING_NO());
                                        dbmanage.updateNumber(apply.getSAMPLING_NO(), 1, 1, 1);
                                        //dbmanage.updateSign(apply.getSAMPLING_NO(), 0);
                                    }
                                    if (i == response.body().size()) {
                                        AddActivity addActivity = new AddActivity();
                                        Info_add info = new Info_add();
                                        info.setNO(((MyApplication) getApplication()).getNo());
                                        info.setID("" + apply.getID());
                                        info.setInfo_add1(info_add1);
                                        info.setInfo_add2(info_add2);
                                        info.setInfo_add3(info_add3);
                                        addActivity.save(info);
                                    }
                                    /*Log.v("apply.getSAMPLING_NO()", apply.getSAMPLING_NO() +
                                            "插入成功!");*/
                                }
                            }
                            Snackbar.make(btn_tongbu_apply, "同步成功",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    } else {
                        Log.v("Apply请求成功!", "response.body is null");
                        Snackbar.make(btn_tongbu_apply, "同步失败，请稍后再试!",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
                progressbar_tongbu.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Apply>> call, Throwable t) {
                progressbar_tongbu.setVisibility(View.GONE);
                Log.v("Apply请求失败!", t.getMessage());
                Snackbar.make(btn_tongbu_apply, "同步失败，请稍后再试!",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void attempUpdate() {
        //创建 网络请求接口 的实例
        eLab_API request = HttpUtils.XmlApi();
        Call<UpdateInfo> call = request.UpdateXML();
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (response.body() != null) {
                    try {
                        /* 获取packagemanager的实例*/
                        PackageManager packageManager = getPackageManager();
                        /* getPackageName()是你当前类的包名，0代表是获取版本信息*/
                        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                        versionname = packInfo.versionName;
                        info = response.body();
                        if (Double.parseDouble(info.getVersion()) > Double
                                .parseDouble(versionname)) {
                            Log.i(TAG_UPDATE, "服务器版本号大于本地 ,提示用户升级 ");
                            showUpdataDialog();
                        } else if (Double.parseDouble(info.getVersion()) == Double
                                .parseDouble(versionname)) {
                            Log.i(TAG_UPDATE, "无需升级");
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.i(TAG_UPDATE, "获取APP版本出错");
                        e.printStackTrace();
                    }
                } else {
                    Log.v("update请求成功!", "response.body is null");
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                Log.v("update请求失败!", t.getMessage());
            }
        });
    }

    /* 获取当前程序的版本号*/
    public PackageInfo getPackageInfo() throws Exception {
        // 获取packagemanager的实例  getPackageManager()
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        return getPackageManager().getPackageInfo(getPackageName(), 0);
    }

    // 弹出对话框通知用户更新程序
    public void showUpdataDialog() {
        AlertDialog.Builder builder = new Builder(context, R.style.dialog_update);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_update, null);
        TextView tv_title = v.findViewById(R.id.tv_title);
        TextView tv_msg = v.findViewById(R.id.tv_msg);
        Button btn_commit = v.findViewById(R.id.btn_commit);
        tv_title.setText(String.format(getResources().getString(R.string.update_title), info
                .getVersion()));
        tv_msg.setText(info.getDescription());
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        dialog.setCanceledOnTouchOutside(false);//点击对话框以外的区域，对话框不消失
        btn_commit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downloadApp();
            }
        });
    }

    public void downloadApp() {
        //此处使用DownLoadManager开启下载任务
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.getUrl()));
        // 下载过程和下载完成后通知栏有通知消息。
        request.setNotificationVisibility(DownloadManager.Request
                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载");
        request.setDescription("apk正在下载");
        //设置保存目录  /storage/emulated/0/Android/包名/files/Download
        request.setDestinationInExternalFilesDir(AboutActivity.this, Environment
                .DIRECTORY_DOWNLOADS, "datamanage.apk");
        mId = mDownloadManager.enqueue(request);

        //注册内容观察者，实时显示进度
        MyContentObserver downloadChangeObserver = new MyContentObserver(null);
        getContentResolver().registerContentObserver(Uri.parse
                ("content://downloads/my_downloads"), true, downloadChangeObserver);

        //广播监听下载完成
        listener(mId);
        //弹出进度条，先隐藏前一个dialog
        //dialog.dismiss();
        //显示进度的对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        pd.setCancelable(false);// 设置是否可以通过点击Back键取消
        pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pd.setIcon(R.drawable.logo);// 设置提示的title的图标，默认是没有的
        pd.setTitle("提示");
        pd.setMessage("玩命儿下载中,请稍后...");
        pd.show();
    }

    // 安装apk
    public void installApk(File file) {
        if (file != null) {   // file 即 apk文件
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri =
                        FileProvider.getUriForFile(context, "com.hzlf.sampletest.fileprovider",
                                file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
            }
            startActivity(intent);
            finish();
        }
    }

    private void listener(final long id) {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                File apkFile = getExternalFilesDir("DownLoad/datamanage.apk");
                long longExtra = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == longExtra) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            installApk(apkFile);
                        }
                    }, 1000); /* 延时1s执行*/
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContentObserver extends ContentObserver {

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mId);
            DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager
                        .COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager
                        .COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                float progress = (float) Math.floor(percent * 100);
                pd.setProgress((int) progress);
                if (progress == 100) {
                    pd.dismiss();
                }
            }
        }
    }
}