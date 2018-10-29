package com.hzlf.sampletest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    // 创建user表
    public static final String CREATE_USER = "create table User("
            + "id integer primary key autoincrement," + "no text not null,"
            + "name text not null," + "login_name text not null,"
            + "password text not null," + "time_stamp text not null)";

    // 创建详细 表
    public static final String CREATE_INFO_DETAILS = "create table Details_Info("
            + "num integer primary key autoincrement,"
            + "no text not null,"
            + "chouyangdanbianhao text not null,"
            + "renwulaiyuan text not null,"
            + "weituodanweidizhi text,"
            + "renwuleibie text not null,"
            + "renwushuxing text ,"
            + "danweimingcheng1 text not null,"
            + "danweidizhi1 text not null,"
            + "lianxiren1 text ,"
            + "dianhau1 text ,"
            + "chuanzhen1 text ,"
            + "youbian1 text,"

            + "suozaidi text not null,"
            + "chouyangdidian text not null,"
            + "quyuleixing text not null ,"
            + "chouyanghuanjie text not null,"
            + "danweimingcheng2 text not null,"
            + "danweidizhi2 text not null,"
            + "yingyezhizhao text not null,"
            + "xukezhengleixing text not null,"
            + "xukezhenghao text not null,"
            + "danweifaren text not null,"
            + "nianxiaoshoue text,"
            + "lianxiren2 text not null,"
            + "dianhua2 text not null,"
            + "chuanzhen2 text ,"
            + "youbian2 text,"
            + "shengchanzhemingcheng text not null,"
            + "shengchanzhedizhi text not null,"
            + "shengchanzhelianxiren text not null,"
            + "dianhua3 text not null,"
            + "jiezhiriqi text not null,"
            + "jisongdizhi text not null,"

            + "yangpinmingcheng text not null,"
            + "yangpinleixing text not null,"
            + "yangpinlaiyuan text not null,"
            + "yangpinshuxing text not null,"
            + "yangpinshangbiao text not null,"
            + "baozhuangfenlei text not null,"
            + "guigexinghao text not null,"
            + "zhiliangdengji text not null,"
            + "yangpintiaoma text not null,"
            + "riqileixing text not null,"
            + "riqi text not null,"
            + "baozhiqi text not null,"
            + "chanpinpihao text not null ,"
            + "yangpindanjia text not null,"
            + "chukou text not null,"
            + "yuanchandi text not null,"
            + "chouyangriqi text not null,"
            + "chouyangfangshi text not null,"
            + "chouyangxingtai text,"
            + "yangpinbaozhuang text not null,"
            + "chucuntiaojian text not null,"
            + "zhixingbiaozhun text not null,"
            + "chouyangshuliangdanwei text,"
            + "chouyangjishu text not null,"
            + "beiyangshuliang text not null,"
            + "chouyangshuliang text not null,"
            + "chouyangren text not null,"
            + "yangpinxukezheng text,"
            + "beizhu text,"
            + "dayinriqi text," + "id text)";

    // 创建抽样单编号表
    public static final String CREATE_SAMPLENUMBER = "create table SampleNumber("
            + "id integer primary key autoincrement,"
            + "number text not null,"
            + "used text not null,"
            + "print text not null,"
            + "upload text not null," + "sign text not null)";

    // 创建上传图片表
    public static final String CREATE_UPLOAD_IMG = "create table UploadImg("
            + "id integer primary key autoincrement," + "number text not null,"
            + "imagepath text not null)";

    // 创建任务来源表
    public static final String CREATE_TASK_SOURCE = "create table TaskSource("
            + "id integer primary key autoincrement," + "name text not null,"
            + "addr text)";

    /*public static final String DROP_USER = "drop table if exists User";
    public static final String DROP_INFO_DETAILS = "drop table if exists Details_Info";
    public static final String DROP_SAMPLENUMBER = "drop table if exists SampleNumber";
    public static final String DROP_UPLOAD_IMG = "drop table if exists UploadImg";
    public static final String DROP_TASK_SOURCE = "drop table if exists TaskSource";*/

    private Context mContext;

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