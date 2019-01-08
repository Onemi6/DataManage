package com.hzlf.sampletest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    // 创建user表
    private static final String CREATE_USER = "create table User("
            + "id integer primary key autoincrement," + "no text not null,"
            + "name text not null," + "login_name text not null,"
            + "password text not null," + "time_stamp text not null)";

    // 创建详细 表
    private static final String CREATE_INFO_DETAILS = "create table Details_Info("
            + "num integer primary key autoincrement,"
            + "no text ,"
            + "chouyangdanbianhao text ,"
            + "renwulaiyuan text ,"
            + "weituodanweidizhi text,"
            + "renwuleibie text ,"
            + "renwushuxing text ,"
            + "danweimingcheng1 text ,"
            + "danweidizhi1 text ,"
            + "lianxiren1 text ,"
            + "dianhau1 text ,"
            + "chuanzhen1 text ,"
            + "youbian1 text,"

            + "suozaidi text ,"
            + "chouyangdidian text ,"
            + "quyuleixing text  ,"
            + "chouyanghuanjie text ,"
            + "danweimingcheng2 text ,"
            + "danweidizhi2 text ,"
            + "yingyezhizhao text ,"
            + "xukezhengleixing text ,"
            + "xukezhenghao text ,"
            + "danweifaren text ,"
            + "nianxiaoshoue text,"
            + "lianxiren2 text ,"
            + "dianhua2 text ,"
            + "chuanzhen2 text ,"
            + "youbian2 text,"
            + "shengchanzhemingcheng text ,"
            + "shengchanzhedizhi text ,"
            + "shengchanzhelianxiren text ,"
            + "dianhua3 text ,"
            + "jiezhiriqi text ,"
            + "jisongdizhi text ,"

            + "yangpinmingcheng text ,"
            + "yangpinleixing text ,"
            + "yangpinlaiyuan text ,"
            + "yangpinshuxing text ,"
            + "yangpinshangbiao text ,"
            + "baozhuangfenlei text ,"
            + "guigexinghao text ,"
            + "zhiliangdengji text ,"
            + "yangpintiaoma text ,"
            + "riqileixing text ,"
            + "riqi text ,"
            + "baozhiqi text ,"
            + "chanpinpihao text  ,"
            + "yangpindanjia text ,"
            + "chukou text ,"
            + "yuanchandi text ,"
            + "chouyangriqi text ,"
            + "chouyangfangshi text ,"
            + "chouyangxingtai text,"
            + "yangpinbaozhuang text ,"
            + "chucuntiaojian text ,"
            + "zhixingbiaozhun text ,"
            + "chouyangshuliangdanwei text,"
            + "chouyangjishu text ,"
            + "beiyangshuliang text ,"
            + "chouyangshuliang text ,"
            + "chouyangren text ,"
            + "yangpinxukezheng text,"
            + "beizhu text,"
            + "dayinriqi text," + "id text)";

    // 创建抽样单编号表
    private static final String CREATE_SAMPLENUMBER = "create table SampleNumber("
            + "id integer primary key autoincrement,"
            + "number text not null,"
            + "used text not null,"
            + "print text not null,"
            + "upload text not null," + "sign text not null)";

    // 创建上传图片表
    private static final String CREATE_UPLOAD_IMG = "create table UploadImg("
            + "id integer primary key autoincrement," + "number text not null,"
            + "imagepath text not null)";

    // 创建任务来源表
    private static final String CREATE_TASK_SOURCE = "create table TaskSource("
            + "id integer primary key autoincrement," + "name text not null,"
            + "addr text)";

    /*public static final String DROP_USER = "drop table if exists User";
    public static final String DROP_INFO_DETAILS = "drop table if exists Details_Info";
    public static final String DROP_SAMPLENUMBER = "drop table if exists SampleNumber";
    public static final String DROP_UPLOAD_IMG = "drop table if exists UploadImg";
    public static final String DROP_TASK_SOURCE = "drop table if exists TaskSource";*/


    public MyDatabaseHelper(Context context, String name,
                            CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO 自动生成的构造函数存根
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 自动生成的方法存根
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_INFO_DETAILS);
        db.execSQL(CREATE_SAMPLENUMBER);
        db.execSQL(CREATE_UPLOAD_IMG);
        db.execSQL(CREATE_TASK_SOURCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 自动生成的方法存根
        switch (oldVersion) {
            /*case 1:
                db.execSQL(CREATE_UPLOAD_IMG);
            case 2:
                db.execSQL(CREATE_TASK_SOURCE);
            case 3:
                db.execSQL("alter table SampleNumber add column sign text");
            case 4:
            case 5:
                db.execSQL("alter table Details_Info add column yangpinxukezheng text");
            case 6:
                db.execSQL("alter table Details_Info add column shengchanzhelianxiren text");
                db.execSQL("alter table Details_Info add column jiezhiriqi text");
                db.execSQL("alter table Details_Info add column jisongdizhi text");
                db.execSQL("alter table Details_Info add column dayinriqi text");*/
            default:
        }
    }
}