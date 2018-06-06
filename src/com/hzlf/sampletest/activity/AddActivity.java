package com.hzlf.sampletest.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
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

public class AddActivity extends FragmentActivity implements OnClickListener {

	private MyViewPager viewpager;

	// ҳ�漯��
	private List<Fragment> fmList;
	private fragment_add1 fm_add1;
	private fragment_add2 fm_add2;
	private fragment_add3 fm_add3;

	private Button btn_back;
	private String number;
	private DBManage dbmanage = new DBManage(this);

	private Context _context;

	/*private static final int UPDATE_TRUE = 3;
	private static final int UPDATE_FLASE = -3;*/

	public String getNumber() {
		return number;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add);

		_context = this;

		number = getIntent().getStringExtra("number");

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		viewpager = (MyViewPager) findViewById(R.id.viewpager);
		// ��������ҳ����
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

		if (NetworkUtil.checkedNetWork(_context)) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					/*
					 * String result = HttpUtils
					 * .getUsers(UsedPath.api_Emp_GET);
					 */
					String result = HttpUtils
							.getAllSource(UsedPath.api_Sys_GetAllSource);

					if (result.equals("��ȡ����ʧ��") || result.equals("")) {
						Log.d("source", "����������Դʧ��");
						/*
						 * Message message = new Message(); message.what =
						 * UPDATE_FLASE; message.obj = "ͬ��ʧ��";
						 * handler.sendMessage(message);
						 */
					} else {
						LinkedList<Source> source = GsonTools
								.getAllSource(result);
						if (source.size() != 0) {
							for (Iterator iterator = source.iterator(); iterator
									.hasNext();) {
								Source onesource = (Source) iterator.next();
								if (dbmanage.findTaskSource(onesource
										.getSOURCE_NAME()) != null) {

								} else {
									dbmanage.addTaskSource(onesource
											.getSOURCE_NAME());
								}
							}
							Log.d("source", "����������Դ�ɹ�");
							/*
							 * Message message = new Message(); message.what =
							 * UPDATE_TRUE; message.obj = "ͬ���ɹ�";
							 * handler.sendMessage(message);
							 */
						}
					}
				}
			});
			thread.start();
		} else {
			Log.d("source", "����������Դʱ������");
			/*
			 * Message message = new Message(); message.what = UPDATE_FLASE;
			 * message.obj = "��ǰ������"; handler.sendMessage(message);
			 */
		}
	}

	/*
	 * private Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { switch (msg.what) {
	 * case UPDATE_TRUE: Toast.makeText(AddActivity.this, (String) msg.obj,
	 * Toast.LENGTH_SHORT).show(); break; case UPDATE_FLASE:
	 * Toast.makeText(AddActivity.this, (String) msg.obj,
	 * Toast.LENGTH_SHORT).show(); break; } } };
	 */

	/**
	 * �����Լ���ViewPager�������� Ҳ����ʹ��FragmentPagerAdapter������������֮������𣬿����Լ�ȥ��һ�¡�
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

	public void nextFragment() {
		viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
	}

	public void backFragment() {
		viewpager.setCurrentItem(viewpager.getCurrentItem() - 1, true);
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		Intent intent_back = new Intent();
		intent_back.setClass(AddActivity.this, ApplyNumberActivity.class);
		switch (v.getId()) {
		case R.id.btn_back:
			finish();// ������ǰ�
			AddActivity.this.startActivity(intent_back);
			break;
		default:
			break;
		}
	}
}
