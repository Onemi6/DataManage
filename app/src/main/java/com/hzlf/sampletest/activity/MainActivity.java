package com.hzlf.sampletest.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.MainInfo;
import com.hzlf.sampletest.others.MaininfoAdapter;
import com.hzlf.sampletest.others.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long mExitTime;
    private String ALBUM_PATH;
    private boolean sdCardExist;
    private Context _context;
    private DBManage dbmanage = new DBManage(this);
    private List<MainInfo> maininfolist = new ArrayList<MainInfo>();
    private MaininfoAdapter adapter_maininfo;
    private TextView tv_maininfo_empty, tv_user_name;
    private ListView listview;
    private Toolbar toolbar;
    private FloatingActionButton data_add;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;
    private SharedPreferences isremember;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        _context = this;
        editor = getSharedPreferences("User", MODE_PRIVATE).edit();
        Filedir();
        initMaininfo();
        toolbar = findViewById(R.id.toolbar_main);
        data_add = findViewById(R.id.data_add);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //获取头布局文件
        //View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header);
        headerView = navigationView.getHeaderView(0);
        tv_user_name = headerView.findViewById(R.id.user_name);
        tv_maininfo_empty = findViewById(R.id.tv_maininfo_empty);
        listview = findViewById(R.id.list_maininfo);

        if (((MyApplication) getApplication()).getName() == null) {
            startActivity(new Intent(_context, LoginActivity.class));
            finish();
        } else {
            setSupportActionBar(toolbar);
            tv_user_name.setText(((MyApplication) getApplication()).getName() + "抽检员");
            // getPackageName()是你当前类的包名，0代表是获取版本信息

            data_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_apply = new Intent();
                    intent_apply.setClass(_context, ApplyNumberActivity.class);
                    // finish();// 结束当前活动
                    startActivity(intent_apply);
                }
            });
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                    .navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            listview.setEmptyView(tv_maininfo_empty);
            adapter_maininfo = new MaininfoAdapter(MainActivity.this,
                    R.layout.maininfo_item, maininfolist);
            listview.setAdapter(adapter_maininfo);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO 自动生成的方法存根
                    MainInfo maininfo = maininfolist.get(position);
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
                                        maininfolist.remove(position);
                                        adapter_maininfo.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this, "删除成功",
                                                Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        // TODO 自动生成的 catch 块
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "删除失败",
                                                Toast.LENGTH_LONG).show();
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
        }
    }

    private void initMaininfo() {
        // TODO 自动生成的方法存根
        List<MainInfo> list_info_main = dbmanage.findList_Info_main();
        for (MainInfo info_main : list_info_main) {
            if (dbmanage.findUpload(info_main.getNumber()).equals("1")) {
                info_main.setStatus("已上传");
            }
            maininfolist.add(info_main);
        }
    }

    public void Filedir() {
        sdCardExist = android.os.Environment.getExternalStorageState().equals(android.os
                .Environment
                .MEDIA_MOUNTED);
        if (sdCardExist) {
            ALBUM_PATH = Environment.getExternalStorageDirectory() + "/doc/";
        } else {
            ALBUM_PATH = this.getCacheDir().toString() + "/";
        }
        File dir = new File(ALBUM_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
        maininfolist.clear();
        List<MainInfo> list_info_main = dbmanage.findList_Info_main();
        for (MainInfo info_main : list_info_main) {
            if (dbmanage.findUpload(info_main.getNumber()).equals("1")) {
                info_main.setStatus("已上传");
            }
            maininfolist.add(info_main);
        }
        adapter_maininfo.notifyDataSetChanged();
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_settings:
                Intent intent_apply = new Intent();
                intent_apply.setClass(this, AboutActivity.class);
                // finish();// 结束当前活动
                startActivity(intent_apply);
                break;
            case R.id.nav_account:
                editor.clear();
                editor.commit();
                Intent intent_login = new Intent();
                intent_login.setClass(this, LoginActivity.class);
                finish();// 结束当前活动
                startActivity(intent_login);
                break;
            case R.id.nav_share:
                ShareAppCode();
                break;
            case R.id.nav_send:
                startActivity(new Intent(this, testActivity.class));
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
}