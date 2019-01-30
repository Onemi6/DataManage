package com.hzlf.sampletest.others;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.hzlf.sampletest.model.Info_add1;
import com.hzlf.sampletest.model.Info_add2;
import com.hzlf.sampletest.model.Info_add3;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyApplication extends Application {

    private String no, name, number, token;
    private Integer add1, add2, add3;
    private Info_add1 info_add1;
    private Info_add2 info_add2;
    private Info_add3 info_add3;

    @Override
    public void onCreate() {
        super.onCreate();

        //增加上报进程控制,只在主进程下上报数据
        boolean isDebug = false;
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "e27eeb30ef", isDebug, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy);

        //最简单的初始化
        //CrashReport.initCrashReport(getApplicationContext(), "e27eeb30ef", true);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAdd1() {
        return add1;
    }

    public void setAdd1(Integer add1) {
        this.add1 = add1;
    }

    public Integer getAdd2() {
        return add2;
    }

    public void setAdd2(Integer add2) {
        this.add2 = add2;
    }

    public Integer getAdd3() {
        return add3;
    }

    public void setAdd3(Integer add3) {
        this.add3 = add3;
    }

    public Info_add1 getInfoAdd1() {
        return info_add1;
    }

    public void setInfoAdd1(Info_add1 info_add1) {
        this.info_add1 = info_add1;
    }

    public Info_add2 getInfoAdd2() {
        return info_add2;
    }

    public void setInfoAdd2(Info_add2 info_add2) {
        this.info_add2 = info_add2;
    }

    public Info_add3 getInfoAdd3() {
        return info_add3;
    }

    public void setInfoAdd3(Info_add3 info_add3) {
        this.info_add3 = info_add3;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}