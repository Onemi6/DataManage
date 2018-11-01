package com.hzlf.sampletest.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.entityclass.UpdateInfo;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.others.UsedPath;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AboutActivity extends AppCompatActivity {

    // 更新版本要用到的一些信息
    private static String versionname, TAG = "update";
    private static UpdateInfo info;

    private static final int UPDATE_CLIENT = 4;
    private static final int GET_UNDATAINFO_ERROR = -4;
    private static final int DOWN_ERROR = -5;
    private static final int UPDATE_NO = -3;
    private static final int UPDATE_SUCCESS = 2;
    private static final int NUMBERFORMAT_ERROR = -6;

    private TextView version_update, tv_app_version;
    private Toolbar toolbar;
    private PackageInfo packInfo;

    // private DBManage dbmanage = new DBManage(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);

        version_update = findViewById(R.id.version_update);
        tv_app_version = findViewById(R.id.app_version);
        toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTitle("关于");
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
            tv_app_version.setText("V" + versionname);
        }catch (Exception e){
            e.printStackTrace();
        }
        version_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                /*
                 * 从服务器获取xml解析并进行比对版本号
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
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
                        }
                    }
                }).start();
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

    /* 获取当前程序的版本号*/
    public PackageInfo getPackageInfo() throws Exception {
        // 获取packagemanager的实例  getPackageManager()
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        return getPackageManager().getPackageInfo(getPackageName(), 0);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
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
                    Toast.makeText(getApplicationContext(), "更新出错，请稍后再试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /*
     * 弹出对话框通知用户更新程序
     * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
     * 3.通过builder 创建一个对话框 4.对话框show()出来
     */
    public void showUpdataDialog() {
        AlertDialog.Builder builer = new Builder(this);
        builer.setTitle("有新版本,请升级");
        // 当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        // 当点取消按钮时进行登录
        builer.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // LoginMain();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /*
     * 从服务器中下载APK
     */
    public void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = HttpUtils.getFileFromServer(info.getUrl(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }
    // 安装apk
    public void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}