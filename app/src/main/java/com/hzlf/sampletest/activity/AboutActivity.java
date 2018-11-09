package com.hzlf.sampletest.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

    private static final int UPDATE_CLIENT = 4;
    private static final int GET_UNDATAINFO_ERROR = -4;
    private static final int DOWN_ERROR = -5;
    private static final int UPDATE_NO = -3;
    private static final int UPDATE_SUCCESS = 2;
    private static final int NUMBERFORMAT_ERROR = -6;
    // 更新版本要用到的一些信息
    private static String versionname, TAG = "update";
    private static UpdateInfo info;
    private TextView version_update, tv_app_version;
    private Toolbar toolbar;
    private PackageInfo packInfo;
    private Context context;

    // private DBManage dbmanage = new DBManage(this);
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
                    Toast.makeText(getApplicationContext(), "更新出错，请稍后再试", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        context = this;
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
        } catch (Exception e) {
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

    /*
     * 弹出对话框通知用户更新程序
     * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
     * 3.通过builder 创建一个对话框 4.对话框show()出来
     */
    public void showUpdataDialog() {
        AlertDialog.Builder builder = new Builder(context, R.style.dialog_update);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_update, null);
        TextView tv_title = v.findViewById(R.id.tv_title);
        TextView tv_msg = v.findViewById(R.id.tv_msg);
        Button btn_commit = v.findViewById(R.id.btn_commit);
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
                downLoadApk();
            }
        });
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
        /*Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);*/

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
        }
    }
}