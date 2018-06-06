package com.hzlf.sampletest.activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.Status;
import com.hzlf.sampletest.entityclass.UpdateInfo;
import com.hzlf.sampletest.entityclass.User;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.jpush.Util;
import com.hzlf.sampletest.others.GsonTools;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.UsedPath;

public class LoginActivity extends InstrumentedActivity {

	private Context _context;
	private EditText input_zhanghao, input_mima;
	private Button btn_denglu, btn_tongbu;
	private CheckBox remember_mima;
	private ProgressBar progressbar_login;
	private SharedPreferences isremember;
	private SharedPreferences.Editor editor;
	private static final int LOGIN_TRUE = 1;
	private static final int LOGIN_FLASE = 0;
	private static final int MSG_SET_ALIAS = 2;

	private static final int UPDATE_TRUE = 3;
	private static final int UPDATE_FLASE = -3;

	public static boolean isForeground = false;

	// 更新版本要用到的一些信息
	private String versionname, TAG_UPDATE = "update";
	private UpdateInfo info;

	private static final int UPDATA_CLIENT = 4;
	private static final int GET_UNDATAINFO_ERROR = -4;
	private static final int DOWN_ERROR = -5;

	private String[] str;
	private String _zhanghao, _mima;
	private DBManage dbmanage = new DBManage(this);

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case LOGIN_TRUE:
				editor = isremember.edit();
				if (remember_mima.isChecked()) {
					editor.putBoolean("remember_mima", true);
					editor.putString("ZHANGHAO", _zhanghao);
					editor.putString("MIMA", _mima);
				} else {
					editor.clear();
					// remember_mima.setChecked(false);
				}
				editor.commit();
				 str = ((String) msg.obj).split("[,]");
				((MyApplication) getApplication()).setNo(str[0]);
				((MyApplication) getApplication()).setName(str[1]);
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();

				JPushInterface.init(getApplicationContext());
				String alias = ((MyApplication) getApplication()).getNo();
				if (TextUtils.isEmpty(alias)) {
					Log.d("alias", "空");
					return;
				}
				if (!Util.isValidTagAndAlias(alias)) {
					Log.d("alias", "格式不对");
					return;
				}

				// 调用JPush API设置Alias
				try {
					JPushInterface.setAliasAndTags(getApplicationContext(),
							alias, null, mAliasCallback);
					Log.d("alias", "设置成功");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.d("alias", "设置失败");
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// do something
						progressbar_login.setVisibility(View.GONE);

						Intent intent_main = new Intent();
						intent_main.setClass(LoginActivity.this,
								MainActivity.class);
						finish();// 结束当前活动
						LoginActivity.this.startActivity(intent_main);
					}
				}, 1000); // 延时1s执行
				break;
			case LOGIN_FLASE:
				progressbar_login.setVisibility(View.GONE);
				
				/*Toast.makeText(LoginActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();*/
				
				if ("当前网络不可用".equals(msg.obj)) {
					if ((dbmanage.check(_zhanghao) != null)
							&& (dbmanage.check(_zhanghao).equals(_mima))) {
						Message message = new Message();
						message.what = LOGIN_TRUE;
						message.obj = dbmanage.getSomeInfo(_zhanghao);
						handler.sendMessage(message);
					} else if (dbmanage.check(_zhanghao) == null
							|| !(dbmanage.check(_zhanghao).equals(_mima))) {
						Message message = new Message();
						message.what = LOGIN_FLASE;
						message.obj = "用户名或密码不正确！";
						handler.sendMessage(message);
					}
				}

				break;
			case UPDATE_TRUE:
				progressbar_login.setVisibility(View.GONE);
				Toast.makeText(LoginActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_FLASE:
				progressbar_login.setVisibility(View.GONE);
				Toast.makeText(LoginActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1)
						.show();
				// LoginMain();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				// LoginMain();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		_context = this;
		isremember = this.getSharedPreferences("User", MODE_PRIVATE);
		input_zhanghao = (EditText) findViewById(R.id.input_zhanghao);
		input_mima = (EditText) findViewById(R.id.input_mima);
		remember_mima = (CheckBox) findViewById(R.id.remember_mima);
		progressbar_login = (ProgressBar) findViewById(R.id.progressbar_login);

		boolean isRemember = isremember.getBoolean("remember_mima", false);
		if (isRemember) {
			input_zhanghao.setText(isremember.getString("ZHANGHAO", ""));
			input_mima.setText(isremember.getString("MIMA", ""));
			remember_mima.setChecked(true);
		}

		registerMessageReceiver();

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
						Log.i(TAG_UPDATE, "版本号相同无需升级");
						// LoginMain();
					} else {
						Log.i(TAG_UPDATE, "版本号不同 ,提示用户升级 ");
						Message msg = new Message();
						msg.what = UPDATA_CLIENT;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					// 待处理
					/*
					 * Message msg = new Message(); msg.what =
					 * GET_UNDATAINFO_ERROR; handler.sendMessage(msg);
					 */
					e.printStackTrace();
				}
			}
		}).start();
		
		// 登录
		btn_denglu = (Button) findViewById(R.id.btn_denglu);
		btn_denglu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				progressbar_login.setVisibility(View.VISIBLE);
				_zhanghao = input_zhanghao.getText().toString();
				_mima = input_mima.getText().toString();

				if (_zhanghao.equals("")) {
					Toast.makeText(LoginActivity.this, "账号不能为空",
							Toast.LENGTH_SHORT).show();
				} else if (_mima.equals("")) {
					Toast.makeText(LoginActivity.this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					if (!NetworkUtil.checkedNetWork(_context)) {
						/* if (!isNetworkAvailable(_context)) { */
						if ((dbmanage.check(_zhanghao) != null)
								&& (dbmanage.check(_zhanghao).equals(_mima))) {
							Message message = new Message();
							message.what = LOGIN_TRUE;
							message.obj = dbmanage.getSomeInfo(_zhanghao);
							handler.sendMessage(message);
						} else if (dbmanage.check(_zhanghao) == null
								|| !(dbmanage.check(_zhanghao).equals(_mima))) {
							Message message = new Message();
							message.what = LOGIN_FLASE;
							message.obj = "用户名或密码不正确！";
							handler.sendMessage(message);
						}
					} else {
						new Thread(new Runnable() {
							@Override
							public void run() {
								Map<String, String> body = new HashMap<String, String>();
								body.put("loginName", _zhanghao);
								body.put("passWord", _mima);
								String result = HttpUtils.LoginCheck(
										UsedPath.api_Login_POST, body);
								if (result.equals("获取数据失败")
										|| result.equals("")) {
									Message message = new Message();
									message.what = LOGIN_FLASE;
									message.obj = "登录失败";
									handler.sendMessage(message);
								} else if (result.equals("1")) {
									Message message = new Message();
									message.what = LOGIN_FLASE;
									message.obj = "当前网络不可用";
									handler.sendMessage(message);
								} else {
									Gson gson = new Gson();
									Status status = gson.fromJson(result,
											Status.class);
									if (status.getStatus().equals("success")) {
										Message message = new Message();
										message.what = LOGIN_TRUE;
										message.obj = status.getNo() + ","
												+ status.getName();
										handler.sendMessage(message);
									} else {
										Message message = new Message();
										message.what = LOGIN_FLASE;
										message.obj = status.getMessage();
										handler.sendMessage(message);
									}
								}
							}
						}).start();
					}
				}
			}
		});

		btn_tongbu = (Button) findViewById(R.id.btn_tongbu);
		btn_tongbu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				progressbar_login.setVisibility(View.VISIBLE);
				if (NetworkUtil.checkedNetWork(_context)) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							String result = HttpUtils
									.getUsers(UsedPath.api_Emp_GET);
							if (result.equals("获取数据失败") || result.equals("")) {
								Message message = new Message();
								message.what = UPDATE_FLASE;
								message.obj = "同步失败";
								handler.sendMessage(message);
							} else if (result.equals("1")) {
								Message message = new Message();
								message.what = UPDATE_FLASE;
								message.obj = "当前网络不可用";
								handler.sendMessage(message);
							} else {
								LinkedList<User> users = GsonTools
										.getUsers(result);
								if (users.size() != 0) {
									for (Iterator iterator = users.iterator(); iterator
											.hasNext();) {
										User user = (User) iterator.next();
										if (dbmanage.finduser(user.getNO()) != null) {
											if (dbmanage.finduser(user.getNO())
													.getTIME_STAMP() == user
													.getTIME_STAMP()) {
											} else {
												dbmanage.updateuser(user);
											}
										} else {
											dbmanage.adduser(user);
										}
									}
									Message message = new Message();
									message.what = UPDATE_TRUE;
									message.obj = "同步成功";
									handler.sendMessage(message);
								}
							}
						}
					});
					thread.start();
				} else {
					Message message = new Message();
					message.what = UPDATE_FLASE;
					message.obj = "当前无网络";
					handler.sendMessage(message);
				}
			}
		});

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
				Log.i(TAG_UPDATE, "下载apk,更新");
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
	 * 
	 * public void LoginMain() { Intent intent = new Intent(this,
	 * LoginActivity.class); startActivity(intent); // 结束掉当前的activity
	 * this.finish(); }
	 */

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.hzlf.sampletest.activity.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!Util.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}
	}

	private void setCostomMsg(String msg) {
	}

	private static final String TAG = "JPush";
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (Util.isConnected(getApplicationContext())) {
					handler.sendMessageDelayed(
							handler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}

			// Util.showToast(logs, getApplicationContext());
		}

	};
}
