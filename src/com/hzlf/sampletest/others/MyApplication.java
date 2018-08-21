package com.hzlf.sampletest.others;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

import com.hzlf.sampletest.entityclass.Info_add1;
import com.hzlf.sampletest.entityclass.Info_add2;
import com.hzlf.sampletest.entityclass.Info_add3;

public class MyApplication extends Application {

	private String no, name;
	private Integer add1, add2, add3;

	private Info_add1 info_add1;
	private Info_add2 info_add2;
	private Info_add3 info_add3;

	private static final String TAG = "JPush";

	@Override
	public void onCreate() {
		Log.d(TAG, "[ExampleApplication] onCreate");
		super.onCreate();

		JPushInterface.setDebugMode(true); // ���ÿ�����־,����ʱ��ر���־
		JPushInterface.init(this); // ��ʼ�� JPush
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
}
