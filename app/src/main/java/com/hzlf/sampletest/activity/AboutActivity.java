package com.hzlf.sampletest.activity;

import android.annotation.SuppressLint;
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
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.UpdateInfo;
import com.hzlf.sampletest.model.User;
import com.hzlf.sampletest.others.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {

    private static final int UPDATE_CLIENT = 4;
    private static final int GET_UNDATAINFO_ERROR = -4;
    private static final int DOWN_ERROR = -5;
    private static final int UPDATE_NO = -3;
    private static final int UPDATE_SUCCESS = 2;
    private static final int NUMBERFORMAT_ERROR = -6;
    // 更新版本要用到的一些信息
    private static String versionname, TAG = "update", token;
    private static UpdateInfo info;
    private TextView version_update, tv_app_version, btn_tongbu;
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
        btn_tongbu = findViewById(R.id.btn_tongbu);
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
                        /*try {
                            // 从资源文件获取服务器 地址
                            String path = UsedPath.serverurl;
                            // 包装成url的对象
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.setConnectTimeout(5000);
                            InputStream is = conn.getInputStream();
                            info = HttpUtils.getUpdataInfo(is);
                            if (Double.parseDouble(info.getVersion()) > Double
                                    .parseDouble(versionname)) {
                                // if (info.getVersion().equals(versionname)) {
                                Log.i(TAG, "服务器版本号大于本地 ,提示用户升级 ");
                                Message msg = new Message();
                                msg.what = UPDATE_CLIENT;
                                handler.sendMessage(msg);
                            } else if (Double.parseDouble(info.getVersion()) == Double
                                    .parseDouble(versionname)) {
                                Log.i(TAG, "版本号相同无需升级");
                                Message msg = new Message();
                                msg.what = UPDATE_NO;
                                handler.sendMessage(msg);
                            }
                        } catch (NumberFormatException e) {
                            Message msg = new Message();
                            msg.what = NUMBERFORMAT_ERROR;
                            handler.sendMessage(msg);
                            e.printStackTrace();
                        } catch (Exception e) {
                            // 待处理
                            Message msg = new Message();
                            msg.what = GET_UNDATAINFO_ERROR;
                            handler.sendMessage(msg);
                            e.printStackTrace();
                        }*/
                    }
                }).start();
            }
        });

        btn_tongbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptEmp();
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
                            Toast.makeText(AboutActivity.this, "同步成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Log.v("Emp请求成功!", "response.body is null");
                        Toast.makeText(AboutActivity.this, "同步失败，请稍后再试!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                progressbar_tongbu.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressbar_tongbu.setVisibility(View.GONE);
                Log.v("Emp请求失败!", t.getMessage());
                Toast.makeText(AboutActivity.this, "同步失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_CLIENT:
                    // 对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    // 服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case DOWN_ERROR:
                    // 下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_NO:
                    // 已是最新版本
                    Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_SUCCESS:
                    Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    break;
                case NUMBERFORMAT_ERROR:
                    Log.i(TAG, "版本号转换出错 ");
                    Toast.makeText(getApplicationContext(), "更新出错，请稍后再试", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };

    public void attempUpdate() {
        //创建 网络请求接口 的实例
        eLab_API request = HttpUtils.XmlApi();
        Call<UpdateInfo> call = request.UpdateXML();
        try {
            //同步
            info = call.execute().body();
            if (Double.parseDouble(info.getVersion()) > Double
                    .parseDouble(versionname)) {
                Log.i(TAG, "服务器版本号大于本地 ,提示用户升级 ");
                Message msg = new Message();
                msg.what = UPDATE_CLIENT;
                handler.sendMessage(msg);
            } else if (Double.parseDouble(info.getVersion()) == Double
                    .parseDouble(versionname)) {
                Log.i(TAG, "版本号相同无需升级");
                Message msg = new Message();
                msg.what = UPDATE_NO;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 获取当前程序的版本号*/
    public PackageInfo getPackageInfo() throws Exception {
        // 获取packagemanager的实例  getPackageManager()
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        return getPackageManager().getPackageInfo(getPackageName(), 0);
    }

    /* 弹出对话框通知用户更新程序
     * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
     * 3.通过builder 创建一个对话框 4.对话框show()出来*/
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