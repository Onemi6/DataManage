package com.hzlf.sampletest.activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.entityclass.UpdateInfo;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.others.UsedPath;

public class SettingActivity extends Activity {

	// 更新版本要用到的一些信息
	private static String versionname, TAG = "update";
	private static UpdateInfo info;

	private static final int UPDATE_CLIENT = 1;
	private static final int GET_UNDATAINFO_ERROR = -1;
	private static final int DOWN_ERROR = -2;
	private static final int UPDATE_NO = -3;
	private static final int UPDATE_SUCCESS = 4;

	private TextView version_update, source_update;
	//private DBManage dbmanage = new DBManage(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);

		version_update = (TextView) findViewById(R.id.version_update);
		version_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				try {
					versionname = getVersionName();
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				/*
				 * 从服务器获取xml解析并进行比对版本号
				 */
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// 从资源文件获取服务器 地址
							String path = UsedPath.serverurl;
							// 包装成url的对象
							URL url = new URL(path);
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setConnectTimeout(5000);
							InputStream is = conn.getInputStream();
							info = HttpUtils.getUpdataInfo(is);

							if (info.getVersion().equals(versionname)) {

								Log.i(TAG, "版本号相同无需升级");
								Message msg = new Message();
								msg.what = UPDATE_NO;
								handler.sendMessage(msg);
							} else {
								Log.i(TAG, "版本号不同 ,提示用户升级 ");
								Message msg = new Message();
								msg.what = UPDATE_CLIENT;
								handler.sendMessage(msg);
							}
						} catch (Exception e) {
							// 待处理
							Message msg = new Message();
							msg.what = GET_UNDATAINFO_ERROR;
							handler.sendMessage(msg);
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		/*source_update = (TextView) findViewById(R.id.source_update);
		source_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				new Thread(new Runnable() {

					@Override
					public void run() {
						String result = HttpUtils
								.getAllSource(UsedPath.api_Sys_GetAllSource);
						Log.d("result", result);
						if (result.equals("获取数据失败") || result.equals("")) {
						} else {
							LinkedList<Source> source = GsonTools
									.getAllSource(result);
							if (source.size() != 0) {
								for (Iterator iterator = source.iterator(); iterator
										.hasNext();) {
									String name = (String) iterator.next();
									if (dbmanage.findTaskSource(name) != null) {

									} else {
										dbmanage.addTaskSource(name);
									}
								}
							}
							Message msg = new Message();
							msg.what = UPDATE_SUCCESS;
							handler.sendMessage(msg);
						}
					}
				}).start();
			}
		});*/
	}

	/*
	 * 获取当前程序的版本号
	 */
	public String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_CLIENT:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1)
						.show();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				break;
			case UPDATE_NO:
				// 已是最新版本
				Toast.makeText(getApplicationContext(), "已是最新版本", 1).show();
				break;
			case UPDATE_SUCCESS:
				Toast.makeText(getApplicationContext(), "更新成功", 1).show();
				break;
			}
		}
	};

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	public void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("有新版本,请升级");
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("马上升级", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk,更新");
				downLoadApk();
			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/*
	 * 从服务器中下载APK
	 */
	public void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = HttpUtils.getFileFromServer(info.getUrl(), pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 安装apk
	public void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	/*
	 * 进入程序的主界面
	 */
	public void LoginMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		// 结束掉当前的activity
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
