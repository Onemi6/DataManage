package com.hzlf.sampletest.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.model.Info_add;
import com.hzlf.sampletest.model.Source;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.http.eLab_API;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.MyViewPager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    private MyViewPager viewpager;

    // 页面集合
    private List<Fragment> fmList;
    private fragment_add1 fm_add1;
    private fragment_add2 fm_add2;
    private fragment_add3 fm_add3;

    private Toolbar toolbar;
    private String number, token;
    private DBManage dbmanage = new DBManage(this);
    private Context _context;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        _context = this;
        number = getIntent().getStringExtra("number");
        toolbar = findViewById(R.id.toolbar_add);
        toolbar.setTitle("抽样信息填报");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //设置toolbar
        setSupportActionBar(toolbar);
        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white);
        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);
        viewpager = findViewById(R.id.viewpager);
        // 设置限制页面数
        viewpager.setOffscreenPageLimit(3);
        fmList = new ArrayList<>();
        fm_add1 = new fragment_add1();
        fm_add2 = new fragment_add2();
        fm_add3 = new fragment_add3();
        fmList.add(fm_add1);
        fmList.add(fm_add2);
        fmList.add(fm_add3);
        viewpager.setAdapter(new MyFragStatePagerAdapter(
                getSupportFragmentManager()));
        ((MyApplication) this.getApplication()).setAdd1(0);
        ((MyApplication) this.getApplication()).setAdd2(0);
        ((MyApplication) this.getApplication()).setAdd3(0);
        attemptGetAllSource();//更新任务来源
    }

    public void attemptGetAllSource() {
        if (NetworkUtil.isNetworkAvailable(_context)) {
            eLab_API request = HttpUtils.GsonApi();
            if (((MyApplication) getApplication()).getToken() == null) {
                token = "Bearer " + sharedPreferences.getString("token", "");
            } else {
                token = "Bearer " + ((MyApplication) getApplication()).getToken();
            }
            Call<List<Source>> call = request.GetAllSource(token);
            call.enqueue(new Callback<List<Source>>() {
                @Override
                public void onResponse(Call<List<Source>> call, Response<List<Source>> response) {
                    if (response.code() == 401) {
                        Log.v("GetAllSource请求", "token过期");
                        Intent intent_login = new Intent();
                        intent_login.setClass(AddActivity.this,
                                LoginActivity.class);
                        intent_login.putExtra("login_type", 1);
                        startActivity(intent_login);
                    } else if (response.code() == 200) {
                        if (response.body() != null) {
                            if (response.body().size() != 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    Source newSource = response.body().get(i);
                                    Source oldSource = dbmanage.findTaskSource(newSource
                                            .getSOURCE_NAME());
                                    if (oldSource != null) {
                                        if (oldSource.getADDR() != newSource.getADDR()) {
                                            dbmanage.updateTaskSource(newSource);
                                        }
                                    } else {
                                        dbmanage.addTaskSource(newSource);
                                    }
                                }
                            }
                        } else {
                            Log.v("GetAllSource请求成功!", "response.code is null");
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Source>> call, Throwable t) {
                    Log.v("GetAllSource请求失败!", t.getMessage());
                }
            });
        } else {
            Log.d("source", "更新任务来源时无网络");
        }
    }

    public String getNumber() {
        return number;
    }

    public void nextFragment() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
    }

    public void backFragment() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() - 1, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(Info_add info) {
        Gson info_add_gson = new Gson();
        String str = info_add_gson.toJson(info);
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            // MODE_PRIVATE 覆盖内容
            // MODE_APPEND 追加内容
            out = openFileOutput("data.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load() {
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    //定义自己的ViewPager适配器。 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。

    class MyFragStatePagerAdapter extends FragmentStatePagerAdapter {
        Fragment currentFragment;

        public MyFragStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fmList.get(position);
        }

        @Override
        public int getCount() {
            return fmList.size();
        }
    }
}
