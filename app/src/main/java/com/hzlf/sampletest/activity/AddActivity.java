package com.hzlf.sampletest.activity;

import android.content.Context;
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
import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Source;
import com.hzlf.sampletest.fragment.fragment_add1;
import com.hzlf.sampletest.fragment.fragment_add2;
import com.hzlf.sampletest.fragment.fragment_add3;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.others.GsonTools;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.MyViewPager;
import com.hzlf.sampletest.others.UsedPath;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private MyViewPager viewpager;

    // 页面集合
    private List<Fragment> fmList;
    private fragment_add1 fm_add1;
    private fragment_add2 fm_add2;
    private fragment_add3 fm_add3;

    private Toolbar toolbar;
    private String number;
    private DBManage dbmanage = new DBManage(this);
    private Context _context;

    public String getNumber() {
        return number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add);
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
        fmList = new ArrayList<Fragment>();
        fm_add1 = new fragment_add1();
        fm_add2 = new fragment_add2();
        fm_add3 = new fragment_add3();
        fmList.add(fm_add1);
        fmList.add(fm_add2);
        fmList.add(fm_add3);
        viewpager.setAdapter(new MyFrageStatePagerAdapter(
                getSupportFragmentManager()));
        ((MyApplication) this.getApplication()).setAdd1(0);
        ((MyApplication) this.getApplication()).setAdd2(0);
        ((MyApplication) this.getApplication()).setAdd3(0);

        UpdateTaskSource();//更新任务来源
    }

    public void UpdateTaskSource() {
        if (NetworkUtil.checkedNetWork(_context)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = HttpUtils
                            .getAllSource(UsedPath.api_Sys_GetAllSource);
                    if (result.equals("获取数据失败") || result.equals("")) {
                        Log.d("source", "更新任务来源失败");
                    } else {
                        LinkedList<Source> source = GsonTools
                                .getAllSource(result);
                        if (source.size() != 0) {
                            for (Iterator iterator = source.iterator(); iterator
                                    .hasNext(); ) {
                                Source newsource = (Source) iterator.next();
                                Source oldsource = dbmanage.findTaskSource(newsource
                                        .getSOURCE_NAME());
                                if (oldsource != null) {
                                    if (oldsource.getADDR() != newsource.getADDR()) {
                                        dbmanage.updateTaskSource(newsource);
                                    }
                                } else {
                                    dbmanage.addTaskSource(newsource);
                                }
                            }
                            Log.d("source", "更新任务来源成功");
                        }
                    }
                }
            });
            thread.start();
        } else {
            Log.d("source", "更新任务来源时无网络");
        }
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

    /**
     * 定义自己的ViewPager适配器。 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
     */
    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {
        Fragment currentFragment;

        public MyFrageStatePagerAdapter(FragmentManager fm) {
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
