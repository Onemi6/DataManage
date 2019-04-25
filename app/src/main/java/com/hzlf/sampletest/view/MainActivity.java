package com.hzlf.sampletest.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.model.MainInfo;
import com.hzlf.sampletest.model.UpdateInfo;
import com.hzlf.sampletest.others.CrashHandler;
import com.hzlf.sampletest.others.MainInfoAdapter;
import com.hzlf.sampletest.others.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long mExitTime, mId;
    private String versionName, TAG_UPDATE = "AppUpdate";
    private UpdateInfo info;
    private Context _context;
    private DBManage dbmanage = new DBManage(this);
    private List<MainInfo> mainInfoList = new ArrayList<>(), allList = new ArrayList<>();
    private MainInfoAdapter adapter_mainInfo;
    private ListView listview;
    private FloatingActionButton data_add;
    private SharedPreferences sharedPreferences;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        _context = this;
        CrashHandler.getInstance().init(_context);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        data_add = findViewById(R.id.data_add);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //获取头布局文件
        //View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);
        View headerView = navigationView.getHeaderView(0);
        TextView tv_user_name = headerView.findViewById(R.id.user_name);
        TextView tv_mainInfo_empty = findViewById(R.id.tv_maininfo_empty);
        listview = findViewById(R.id.list_maininfo);
        if (sharedPreferences.getString("NAME", null) == null) {
            Intent intent_login = new Intent();
            intent_login.setClass(MainActivity.this, LoginActivity.class);
            intent_login.putExtra("login_type", -1);
            finish();
            startActivity(intent_login);
        } else {
            setSupportActionBar(toolbar);
            String name;
            if (((MyApplication) getApplication()).getName() == null) {
                name = sharedPreferences.getString("NAME", null);
            } else {
                name = ((MyApplication) getApplication()).getName();
            }
            tv_user_name.setText(String.format(getResources().getString(R.string.user_name), name));
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                    .navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            listview.setEmptyView(tv_mainInfo_empty);
            adapter_mainInfo = new MainInfoAdapter(MainActivity.this,
                    R.layout.maininfo_item, mainInfoList);
            listview.setAdapter(adapter_mainInfo);
            initMainInfo();
            attemptUpdate();
            viewAction();
        }
    }

    private void initMainInfo() {
        // TODO 自动生成的方法存根
        if (((MyApplication) getApplication()).getNo() == null) {
            ((MyApplication) getApplication()).setNo(sharedPreferences.getString("NO", null));
        }
        allList = dbmanage.findList_Info_main(((MyApplication) getApplication()).getNo());
        for (int i = 0; i < allList.size(); i++) {
            if (dbmanage.findUpload(allList.get(i).getNumber()).equals("1")) {
                allList.get(i).setStatus("已上传");
            }
        }
        mainInfoList.addAll(allList);
        if (mainInfoList.size() > 0) {
            adapter_mainInfo.notifyDataSetChanged();
        }
        /*if (allList.size() >= (10*num)) {
            List<MainInfo> getlist = allList.subList(num * 10, (num + 1) * 10);
            mainInfoList.addAll(getlist);
            num++;
            btn_getmoredata.setVisibility(View.VISIBLE);
        } else {
            mainInfoList.addAll(allList);
        }*/
        //adapter_mainInfo.notifyDataSetChanged();
    }

    private void viewAction() {
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                MainInfo maininfo = mainInfoList.get(position);
                ((MyApplication) getApplication()).setNumber(maininfo.getNumber());
                Intent intent_details = new Intent();
                intent_details.setClass(MainActivity.this,
                        DetailsActivity.class);
                // finish();// 结束当前活动
                startActivity(intent_details);
            }
        });
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO 自动生成的方法存根
                // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                // 设置Title的图标
                builder.setIcon(R.drawable.logo);
                // 设置Title的内容
                // builder.setTitle("弹出警告框");
                // 设置Content来显示一个信息
                builder.setMessage("确定删除？");
                // 设置一个PositiveButton
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                try {
                                    MainInfo item = (MainInfo) listview
                                            .getItemAtPosition(position);
                                    dbmanage.deteleinfo(item.getNumber());
                                    mainInfoList.remove(position);
                                    adapter_mainInfo.notifyDataSetChanged();
                                    Snackbar.make(listview, "删除成功",
                                            Snackbar.LENGTH_LONG).setAction("Action", null)
                                            .show();
                                } catch (Exception e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                    Snackbar.make(listview, "删除失败",
                                            Snackbar.LENGTH_LONG).setAction("Action", null)
                                            .show();
                                }
                            }
                        });
                // 设置一个NegativeButton
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                // 显示出该对话框
                builder.show();
                return true;
            }
        });
        data_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_apply = new Intent();
                intent_apply.setClass(_context, CodeActivity.class);
                // finish();// 结束当前活动
                startActivity(intent_apply);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // Object mHelperUtils;
                Snackbar.make(listview, "再按一次退出程序",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainInfoList.clear();
        allList = dbmanage.findList_Info_main(((MyApplication) getApplication()).getNo());
        for (int i = 0; i < allList.size(); i++) {
            if (dbmanage.findUpload(allList.get(i).getNumber()).equals("1")) {
                allList.get(i).setStatus("已上传");
            }
        }
        mainInfoList.addAll(allList);
        if (mainInfoList.size() > 0) {
            adapter_mainInfo.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_account:
                Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent_login = new Intent();
                intent_login.setClass(MainActivity.this, LoginActivity.class);
                intent_login.putExtra("login_type", -1);
                startActivity(intent_login);
                finish();
                break;
            case R.id.nav_share:
                ShareAppCode();
                break;
            case R.id.nav_send:
                Snackbar.make(listview, "暂无此功能",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ShareAppCode() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.img_item, (ViewGroup) findViewById(R
                .id.dialog_img));
        ImageView imageview = layout.findViewById(R.id.imageView);
        imageview.setImageResource(R.drawable.appcode);
        AlertDialog.Builder dialog_img = new AlertDialog.Builder(_context).setView(layout)
                .setPositiveButton("确定", null);
        dialog_img.show();
    }

    public void attemptUpdate() {
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
                        versionName = packInfo.versionName;
                        info = response.body();
                        if (Double.parseDouble(info.getVersion()) > Double
                                .parseDouble(versionName)) {
                            Log.i(TAG_UPDATE, "服务器版本号大于本地 ,提示用户升级 ");
                            showUpdateDialog();
                        } else if (Double.parseDouble(info.getVersion()) == Double
                                .parseDouble(versionName)) {
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

    /* 弹出更新对话框 */
    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context, R.style.dialog_update);
        LayoutInflater inflater = LayoutInflater.from(_context);
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
        dialog.setCancelable(false);//点击返回键，对话框不消失
        btn_commit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                File apkFile = getExternalFilesDir("DownLoad/datamanage.apk");
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                downloadApp();
            }
        });
    }

    public void downloadApp() {
        //此处使用DownLoadManager开启下载任务
        DownloadManager mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.getUrl()));
        // 下载过程和下载完成后通知栏有通知消息。
        request.setNotificationVisibility(DownloadManager.Request
                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载");
        request.setDescription("apk正在下载");
        //设置保存目录  /storage/emulated/0/Android/包名/files/Download
        request.setDestinationInExternalFilesDir(MainActivity.this, Environment
                .DIRECTORY_DOWNLOADS, "datamanage.apk");
        mId = mDownloadManager.enqueue(request);

        //注册内容观察者，实时显示进度
        MainActivity.MyContentObserver downloadChangeObserver = new MainActivity
                .MyContentObserver(null);
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
                        FileProvider.getUriForFile(_context, "com.hzlf.sampletest.fileprovider",
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
                final File apkFile = getExternalFilesDir("DownLoad/datamanage.apk");
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

        private MyContentObserver(Handler handler) {
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