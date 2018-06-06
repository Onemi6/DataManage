package com.hzlf.sampletest.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Info_add1;
import com.hzlf.sampletest.entityclass.Info_add2;
import com.hzlf.sampletest.entityclass.Info_add3;
import com.hzlf.sampletest.entityclass.MainInfo;
import com.hzlf.sampletest.entityclass.User;

public class DBManage {
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private Context _context;

	public DBManage(Context context) {
		// TODO 自动生成的构造函数存根
		dbHelper = new MyDatabaseHelper(context, "DataManage.db", null, 4);
		// db = dbHelper.getWritableDatabase();
		_context = context;
	}

	// 插入用户
	public void adduser(User user) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into User (no,name,login_name,password,time_stamp) values (?,?,?,?,?)",
				new Object[] { user.getNO(), user.getNAME(),
						user.getLOGIN_NAME(), user.getPASSWORD(),
						user.getTIME_STAMP() });
	}

	// 插入信息
	public void addInfo(String no, Info_add1 info_add1, Info_add2 info_add2,
			Info_add3 info_add3) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into Details_Info(no,chouyangdanbianhao,renwulaiyuan,renwuleibie,danweimingcheng1,"
						+ "danweidizhi1,lianxiren1,dianhau1,chuanzhen1,youbian1,"

						+ "suozaidi,chouyangdidian,quyuleixing,chouyanghuanjie,danweimingcheng2,"
						+ "danweidizhi2,yingyezhizhao,xukezhengleixing,xukezhenghao,danweifaren,nianxiaoshoue,"
						+ "lianxiren2,dianhua2,chuanzhen2,youbian2,shengchanzhemingcheng,shengchanzhedizhi,dianhua3,"

						+ "yangpinmingcheng,yangpinleixing,yangpinlaiyuan,yangpinshuxing,"
						+ "yangpinshangbiao,baozhuangfenlei,guigexinghao,zhiliangdengji,yangpintiaoma,riqileixing,riqi,"
						+ "baozhiqi,chanpinpihao,yangpindanjia,chukou,yuanchandi,chouyangriqi,chouyangfangshi,"
						+ "chouyangxingtai,yangpinbaozhuang,chucuntiaojian,zhixingbiaozhun,"
						+ "chouyangshuliangdanwei,chouyangjishu,beiyangshuliang,chouyangshuliang,chouyangren,beizhu)"
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { no, info_add1.getValue1(),
						info_add1.getValue2(), info_add1.getValue3(),
						info_add1.getValue4(), info_add1.getValue5(),
						info_add1.getValue6(), info_add1.getValue7(),
						info_add1.getValue8(), info_add1.getValue9(),

						info_add2.getValue1(), info_add2.getValue2(),
						info_add2.getValue3(), info_add2.getValue4(),
						info_add2.getValue5(), info_add2.getValue6(),
						info_add2.getValue7(), info_add2.getValue8(),
						info_add2.getValue9(), info_add2.getValue10(),
						info_add2.getValue11(), info_add2.getValue12(),
						info_add2.getValue13(), info_add2.getValue14(),
						info_add2.getValue15(), info_add2.getValue16(),
						info_add2.getValue17(), info_add2.getValue18(),

						info_add3.getValue1(), info_add3.getValue2(),
						info_add3.getValue3(), info_add3.getValue4(),
						info_add3.getValue5(), info_add3.getValue6(),
						info_add3.getValue7(), info_add3.getValue8(),
						info_add3.getValue9(), info_add3.getValue28(),
						info_add3.getValue10(), info_add3.getValue11(),
						info_add3.getValue12(), info_add3.getValue13(),
						info_add3.getValue14(), info_add3.getValue15(),
						info_add3.getValue16(), info_add3.getValue17(),
						info_add3.getValue18(), info_add3.getValue19(),
						info_add3.getValue20(), info_add3.getValue22(),
						info_add3.getValue23(), info_add3.getValue24(),
						info_add3.getValue25(), info_add3.getValue26(),
						info_add3.getValue21(), info_add3.getValue27() });
	}

	// 插入抽样单编号
	public void addSampleNumber(String number) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into SampleNumber (number,used,print,upload,sign) values (?,0,0,0,0)",
				new String[] { number });
	}

	// 插入图片路径
	public void addImagePath(String number, String imagepath) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("insert into UploadImg (number,imagepath) values (?,?)",
				new String[] { number, imagepath });
	}

	// 插入任务来源
	public void addTaskSource(String name) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("insert into TaskSource (name) values (?)",
				new String[] { name });
	}
	
	// 修改图片路径
		public void updateImagePath(String number, String imagepath) {
			db = dbHelper.getWritableDatabase();
			db.execSQL("update UploadImg set imagepath=? where number=?",
					new String[] { imagepath ,number});
		}

	// 修改用户信息
	public void updateuser(User user) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update User set login_name=?,name=?,password=?,time_stamp=? where no=?",
				new Object[] { user.getLOGIN_NAME(), user.getNAME(),
						user.getPASSWORD(), user.getTIME_STAMP(), user.getNO() });
	}

	// 修改抽样信息
	public void updateinfo(Info_add info_add) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update Details_Info set renwulaiyuan=?,renwuleibie=?,danweimingcheng1=?,danweidizhi1=?,lianxiren1=?,dianhau1=?,"
						+ "chuanzhen1=?,youbian1=?,suozaidi=?,chouyangdidian=?,quyuleixing=?,chouyanghuanjie=?,danweimingcheng2=?,"
						+ "danweidizhi2=?,yingyezhizhao=?,xukezhengleixing=?,xukezhenghao=?,danweifaren=?,nianxiaoshoue=?,"
						+ "lianxiren2=?,dianhua2=?,chuanzhen2=?,youbian2=?,shengchanzhemingcheng=?,shengchanzhedizhi=?,"
						+ "dianhua3=?,yangpinmingcheng=?,yangpinleixing=?,"
						+ "yangpinlaiyuan=?,yangpinshuxing=?,yangpinshangbiao=?,baozhuangfenlei=?,guigexinghao=?,"
						+ "zhiliangdengji=?,yangpintiaoma=?,riqi=?,baozhiqi=?,chanpinpihao=?,yangpindanjia=?,"
						+ "chukou=?,yuanchandi=?,chouyangfangshi=?,chouyangxingtai=?,yangpinbaozhuang=?,chucuntiaojian=?,"
						+ "chouyangren=?,zhixingbiaozhun=?,chouyangshuliangdanwei=?,chouyangjishu=?,beiyangshuliang=?,"
						+ "chouyangshuliang=?,beizhu=?,riqileixing=? where chouyangdanbianhao=?",
				new Object[] { info_add.getInfo_add1().getValue2(),
						info_add.getInfo_add1().getValue3(),
						info_add.getInfo_add1().getValue4(),
						info_add.getInfo_add1().getValue5(),
						info_add.getInfo_add1().getValue6(),
						info_add.getInfo_add1().getValue7(),
						info_add.getInfo_add1().getValue8(),
						info_add.getInfo_add1().getValue9(),
						info_add.getInfo_add2().getValue1(),
						info_add.getInfo_add2().getValue2(),
						info_add.getInfo_add2().getValue3(),
						info_add.getInfo_add2().getValue4(),
						info_add.getInfo_add2().getValue5(),
						info_add.getInfo_add2().getValue6(),
						info_add.getInfo_add2().getValue7(),
						info_add.getInfo_add2().getValue8(),
						info_add.getInfo_add2().getValue9(),
						info_add.getInfo_add2().getValue10(),
						info_add.getInfo_add2().getValue11(),
						info_add.getInfo_add2().getValue12(),
						info_add.getInfo_add2().getValue13(),
						info_add.getInfo_add2().getValue14(),
						info_add.getInfo_add2().getValue15(),
						info_add.getInfo_add2().getValue16(),
						info_add.getInfo_add2().getValue17(),
						info_add.getInfo_add2().getValue18(),
						info_add.getInfo_add3().getValue1(),
						info_add.getInfo_add3().getValue2(),
						info_add.getInfo_add3().getValue3(),
						info_add.getInfo_add3().getValue4(),
						info_add.getInfo_add3().getValue5(),
						info_add.getInfo_add3().getValue6(),
						info_add.getInfo_add3().getValue7(),
						info_add.getInfo_add3().getValue8(),
						info_add.getInfo_add3().getValue9(),
						info_add.getInfo_add3().getValue10(),
						info_add.getInfo_add3().getValue11(),
						info_add.getInfo_add3().getValue12(),
						info_add.getInfo_add3().getValue13(),
						info_add.getInfo_add3().getValue14(),
						info_add.getInfo_add3().getValue15(),
						info_add.getInfo_add3().getValue17(),
						info_add.getInfo_add3().getValue18(),
						info_add.getInfo_add3().getValue19(),
						info_add.getInfo_add3().getValue20(),
						info_add.getInfo_add3().getValue21(),
						info_add.getInfo_add3().getValue22(),
						info_add.getInfo_add3().getValue23(),
						info_add.getInfo_add3().getValue24(),
						info_add.getInfo_add3().getValue25(),
						info_add.getInfo_add3().getValue26(),
						info_add.getInfo_add3().getValue27(),
						info_add.getInfo_add3().getValue28(),
						info_add.getInfo_add1().getValue1() });
	}

	// 标记ID
	public void updateId(String id, String number) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("update Details_Info set id=? where chouyangdanbianhao=?",
				new String[] { id, number });
	}

	// 标记number
	public void updateNumber(String number, int used, int print, int upload) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update SampleNumber set used=?,print=?,upload=? where number=?",
				new Object[] { used, print, upload, number });
	}
	
	public void updateSign(String number,int sign) {
		db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update SampleNumber set sign=? where number=?",
				new Object[] {sign, number });
	}

	// 删除用户
	public void deteleuser(String login_name) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("delete from User where login_name = ?",
				new String[] { login_name });
	}

	// 删除信息
	public void deteleinfo(String number) {
		db = dbHelper.getWritableDatabase();
		db.execSQL("delete from Details_Info where chouyangdanbianhao = ?",
				new String[] { number });
	}

	// 验证密码
	public String check(String login_name) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select password from User where login_name=?",
				new String[] { String.valueOf(login_name) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("password"));
		}
		return null;
	}

	// 得到no , name
	public String getSomeInfo(String login_name) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select no,name from User where login_name=?",
				new String[] { String.valueOf(login_name) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("no")) + ","
					+ cursor.getString(cursor.getColumnIndex("name"));
		}
		return null;
	}

	// 查询用户
	public User finduser(String no) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from User where no=?",
				new String[] { String.valueOf(no) });
		if (cursor.moveToNext()) {
			return new User(no,
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("no")),
					cursor.getString(cursor.getColumnIndex("password")),
					cursor.getString(cursor.getColumnIndex("time_stamp")));
		}
		return null;
	}

	// 得到id
	public String findID(String number) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select id from Details_Info where chouyangdanbianhao=?",
				new String[] { String.valueOf(number) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("id"));
		}
		return null;
	}

	// 查询一条信息
	public Info_add findInfo_details(String number) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from Details_Info where chouyangdanbianhao=?",
				new String[] { String.valueOf(number) });
		if (cursor.moveToNext()) {
			return new Info_add(
					cursor.getString(cursor.getColumnIndex("no")),
					cursor.getString(cursor.getColumnIndex("id")),
					new Info_add1(
							cursor.getString(cursor
									.getColumnIndex("chouyangdanbianhao")),
							cursor.getString(cursor
									.getColumnIndex("renwulaiyuan")),
							cursor.getString(cursor
									.getColumnIndex("renwuleibie")),
							cursor.getString(cursor
									.getColumnIndex("danweimingcheng1")),
							cursor.getString(cursor
									.getColumnIndex("danweidizhi1")),
							cursor.getString(cursor
									.getColumnIndex("lianxiren1")),
							cursor.getString(cursor.getColumnIndex("dianhau1")),
							cursor.getString(cursor
									.getColumnIndex("chuanzhen1")), cursor
									.getString(cursor
											.getColumnIndex("youbian1"))),
					new Info_add2(
							cursor.getString(cursor.getColumnIndex("suozaidi")),
							cursor.getString(cursor
									.getColumnIndex("chouyangdidian")),
							cursor.getString(cursor
									.getColumnIndex("quyuleixing")),
							cursor.getString(cursor
									.getColumnIndex("chouyanghuanjie")),
							cursor.getString(cursor
									.getColumnIndex("danweimingcheng2")),
							cursor.getString(cursor
									.getColumnIndex("danweidizhi2")),
							cursor.getString(cursor
									.getColumnIndex("yingyezhizhao")),
							cursor.getString(cursor
									.getColumnIndex("xukezhengleixing")),
							cursor.getString(cursor
									.getColumnIndex("xukezhenghao")),
							cursor.getString(cursor
									.getColumnIndex("danweifaren")),
							cursor.getString(cursor
									.getColumnIndex("nianxiaoshoue")),
							cursor.getString(cursor
									.getColumnIndex("lianxiren2")),
							cursor.getString(cursor.getColumnIndex("dianhua2")),
							cursor.getString(cursor
									.getColumnIndex("chuanzhen2")), cursor
									.getString(cursor
											.getColumnIndex("youbian2")),
							cursor.getString(cursor
									.getColumnIndex("shengchanzhemingcheng")),
							cursor.getString(cursor
									.getColumnIndex("shengchanzhedizhi")),
							cursor.getString(cursor.getColumnIndex("dianhua3"))),
					new Info_add3(
							cursor.getString(cursor
									.getColumnIndex("yangpinmingcheng")),
							cursor.getString(cursor
									.getColumnIndex("yangpinleixing")),
							cursor.getString(cursor
									.getColumnIndex("yangpinlaiyuan")),
							cursor.getString(cursor
									.getColumnIndex("yangpinshuxing")),
							cursor.getString(cursor
									.getColumnIndex("yangpinshangbiao")),
							cursor.getString(cursor
									.getColumnIndex("baozhuangfenlei")),
							cursor.getString(cursor
									.getColumnIndex("guigexinghao")),
							cursor.getString(cursor
									.getColumnIndex("zhiliangdengji")),
							cursor.getString(cursor
									.getColumnIndex("yangpintiaoma")),
							cursor.getString(cursor.getColumnIndex("riqi")),
							cursor.getString(cursor.getColumnIndex("baozhiqi")),
							cursor.getString(cursor
									.getColumnIndex("chanpinpihao")), cursor
									.getString(cursor
											.getColumnIndex("yangpindanjia")),
							cursor.getString(cursor.getColumnIndex("chukou")),
							cursor.getString(cursor
									.getColumnIndex("yuanchandi")), cursor
									.getString(cursor
											.getColumnIndex("chouyangriqi")),
							cursor.getString(cursor
									.getColumnIndex("chouyangfangshi")),
							cursor.getString(cursor
									.getColumnIndex("chouyangxingtai")),
							cursor.getString(cursor
									.getColumnIndex("yangpinbaozhuang")),
							cursor.getString(cursor
									.getColumnIndex("chucuntiaojian")), cursor
									.getString(cursor
											.getColumnIndex("chouyangren")),
							cursor.getString(cursor
									.getColumnIndex("zhixingbiaozhun")),
							cursor.getString(cursor
									.getColumnIndex("chouyangshuliangdanwei")),
							cursor.getString(cursor
									.getColumnIndex("chouyangjishu")),
							cursor.getString(cursor
									.getColumnIndex("beiyangshuliang")),
							cursor.getString(cursor
									.getColumnIndex("chouyangshuliang")),
							cursor.getString(cursor.getColumnIndex("beizhu")),
							cursor.getString(cursor
									.getColumnIndex("riqileixing"))));
		}
		return null;
	}

	// 查询主要信息
	public Cursor findInfo_main() {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select no,chouyangdanbianhao,chouyangriqi from Details_Info ",
				null);
		return cursor;
	}

	public List<MainInfo> findList_Info_main() {
		ArrayList<MainInfo> list_main_info = new ArrayList<MainInfo>();
		Cursor cursor = findInfo_main();
		while (cursor.moveToNext()) {
			MainInfo maininfo = new MainInfo("未上传", cursor.getString(cursor
					.getColumnIndex("no")), cursor.getString(cursor
					.getColumnIndex("chouyangdanbianhao")),
					cursor.getString(cursor.getColumnIndex("chouyangriqi")));
			list_main_info.add(maininfo);
		}
		cursor.close();
		return list_main_info;
	}

	// 查询抽检单编号
	public Cursor findNumber(int type) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		// 查询未使用的
		if (type == 1) {
			cursor = db.rawQuery(
					"select number from SampleNumber where used=0 ", null);
			/* "select number from SampleNumber where used=0 order by id DESC" */
		} else if (type == 2) {// 查询打印过的
			cursor = db
					.rawQuery(
							"select number from SampleNumber where print=1 and used=1 and upload=0 order by id DESC",
							null);
		} else if (type == 3) {// 查询上传过的
			cursor = db
					.rawQuery(
							"select number from SampleNumber where print=1 and used=1 and upload=1 order by id DESC",
							null);
		}
		return cursor;
	}

	public List<String> findList_Number(int type) {
		ArrayList<String> list_number = new ArrayList<String>();
		Cursor cursor = findNumber(type);
		while (cursor.moveToNext()) {
			String number = cursor.getString(cursor.getColumnIndex("number"));
			list_number.add(number);
		}
		cursor.close();
		return list_number;
	}

	public String findUpload(String number) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select upload from SampleNumber where number=?",
				new String[] { String.valueOf(number) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("upload"));
		}
		return null;
	}
	
	
	public String findSign(String number) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select sign from SampleNumber where number=?",
				new String[] { String.valueOf(number) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("sign"));
		}
		return null;
	}

	// 查询上传过的图片
	public Cursor findUploadImage(String number) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select distinct imagepath from UploadImg where number=?",
				new String[] { String.valueOf(number) });
		return cursor;
	}

	public List<String> findList_UploadImage(String number) {
		ArrayList<String> list_upload_image = new ArrayList<String>();
		Cursor cursor = findUploadImage(number);
		while (cursor.moveToNext()) {
			list_upload_image.add(cursor.getString(cursor
					.getColumnIndex("imagepath")));
		}
		cursor.close();
		return list_upload_image;
	}

	// 查询任务来源

	public String findTaskSource(String name) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from TaskSource where name=?",
				new String[] { String.valueOf(name) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("name"));
		}
		return null;
	}

	public Cursor findTaskSource() {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name from TaskSource ", null);
		return cursor;
	}

	public List<String> findList_TaskSource() {
		ArrayList<String> TaskSource = new ArrayList<String>();
		Cursor cursor = findTaskSource();
		while (cursor.moveToNext()) {
			TaskSource.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		return TaskSource;
	}

	// 查询Info_add2

	public Info_add2 findInfo_add2(String danweimingcheng) {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from Details_Info where danweimingcheng2=?",
				new String[] { String.valueOf(danweimingcheng) });
			if (cursor.moveToNext()) {
				return new Info_add2(
						cursor.getString(cursor.getColumnIndex("suozaidi")),
						cursor.getString(cursor
								.getColumnIndex("chouyangdidian")),
						cursor.getString(cursor.getColumnIndex("quyuleixing")),
						cursor.getString(cursor
								.getColumnIndex("chouyanghuanjie")),
						cursor.getString(cursor
								.getColumnIndex("danweimingcheng2")),
						cursor.getString(cursor.getColumnIndex("danweidizhi2")),
						cursor.getString(cursor.getColumnIndex("yingyezhizhao")),
						cursor.getString(cursor
								.getColumnIndex("xukezhengleixing")),
						cursor.getString(cursor.getColumnIndex("xukezhenghao")),
						cursor.getString(cursor.getColumnIndex("danweifaren")),
						cursor.getString(cursor.getColumnIndex("nianxiaoshoue")),
						cursor.getString(cursor.getColumnIndex("lianxiren2")),
						cursor.getString(cursor.getColumnIndex("dianhua2")),
						cursor.getString(cursor.getColumnIndex("chuanzhen2")),
						cursor.getString(cursor.getColumnIndex("youbian2")),
						cursor.getString(cursor
								.getColumnIndex("shengchanzhemingcheng")),
						cursor.getString(cursor
								.getColumnIndex("shengchanzhedizhi")), cursor
								.getString(cursor.getColumnIndex("dianhua3")));
			}
		return null;
	}

	public Cursor findInfo_add2() {
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select distinct danweimingcheng2 from Details_Info ", null);
		return cursor;
	}

	public List<String> findList_Info_add2() {
		ArrayList<String> Info_add2 = new ArrayList<String>();
		Cursor cursor = findInfo_add2();
		while (cursor.moveToNext()) {
			Info_add2.add(cursor.getString(cursor
					.getColumnIndex("danweimingcheng2")));
		}
		cursor.close();
		return Info_add2;
	}

	public void closeDB() {
		db.close();
	}
}
