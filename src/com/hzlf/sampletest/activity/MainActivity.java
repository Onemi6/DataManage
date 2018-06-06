package com.hzlf.sampletest.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.MainInfo;
import com.hzlf.sampletest.fragment.NavigationDrawerFragment;
import com.hzlf.sampletest.others.MaininfoAdapter;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private long mExitTime;
	private CharSequence mTitle;
	private DBManage dbmanage = new DBManage(this);

	private List<MainInfo> maininfolist = new ArrayList<MainInfo>();
	private MaininfoAdapter adapter_maininfo;
	private TextView textview;
	private ListView listview;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		textview = (TextView) findViewById(R.id.text_maininfo);
		listview = (ListView) findViewById(R.id.list_maininfo);
		initMaininfo();
		listview.setEmptyView(textview);
		adapter_maininfo = new MaininfoAdapter(MainActivity.this,
				R.layout.maininfo_item, maininfolist);
		listview.setAdapter(adapter_maininfo);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				MainInfo maininfo = maininfolist.get(position);
				Intent intent_details = new Intent();
				intent_details.setClass(MainActivity.this,
						DetailsActivity.class);
				intent_details.putExtra("info_number", maininfo.getNumber());
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

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, PlaceholderFragment.newInstance(1))
				.commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.add, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.btn_xinzeng) {
			Intent intent_apply = new Intent();
			intent_apply.setClass(this, ApplyNumberActivity.class);
			// finish();// 结束当前活动
			this.startActivity(intent_apply);
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	/**
	 * 刷新, 这种刷新方法，只有一个Activity实例。
	 */
	public void refresh() {
		onCreate(null);
	}

	/**
	 * 调用onCreate(), 目的是刷新数据, 从另一activity界面返回到该activity界面时, 此方法自动调用
	 */

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
		// onCreate(null);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);

		}
	}
}
