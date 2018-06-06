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

	// ���°汾Ҫ�õ���һЩ��Ϣ
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
				// TODO �Զ����ɵķ������
				try {
					versionname = getVersionName();
				} catch (Exception e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				/*
				 * �ӷ�������ȡxml���������бȶ԰汾��
				 */
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// ����Դ�ļ���ȡ������ ��ַ
							String path = UsedPath.serverurl;
							// ��װ��url�Ķ���
							URL url = new URL(path);
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setConnectTimeout(5000);
							InputStream is = conn.getInputStream();
							info = HttpUtils.getUpdataInfo(is);

							if (info.getVersion().equals(versionname)) {

								Log.i(TAG, "�汾����ͬ��������");
								Message msg = new Message();
								msg.what = UPDATE_NO;
								handler.sendMessage(msg);
							} else {
								Log.i(TAG, "�汾�Ų�ͬ ,��ʾ�û����� ");
								Message msg = new Message();
								msg.what = UPDATE_CLIENT;
								handler.sendMessage(msg);
							}
						} catch (Exception e) {
							// ������
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
				// TODO �Զ����ɵķ������
				new Thread(new Runnable() {

					@Override
					public void run() {
						String result = HttpUtils
								.getAllSource(UsedPath.api_Sys_GetAllSource);
						Log.d("result", result);
						if (result.equals("��ȡ����ʧ��") || result.equals("")) {
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
	 * ��ȡ��ǰ����İ汾��
	 */
	public String getVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
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
				// �Ի���֪ͨ�û���������
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// ��������ʱ
				Toast.makeText(getApplicationContext(), "��ȡ������������Ϣʧ��", 1)
						.show();
				break;
			case DOWN_ERROR:
				// ����apkʧ��
				Toast.makeText(getApplicationContext(), "�����°汾ʧ��", 1).show();
				break;
			case UPDATE_NO:
				// �������°汾
				Toast.makeText(getApplicationContext(), "�������°汾", 1).show();
				break;
			case UPDATE_SUCCESS:
				Toast.makeText(getApplicationContext(), "���³ɹ�", 1).show();
				break;
			}
		}
	};

	/*
	 * 
	 * �����Ի���֪ͨ�û����³���
	 * 
	 * �����Ի���Ĳ��裺 1.����alertDialog��builder. 2.Ҫ��builder��������, �Ի��������,��ʽ,��ť
	 * 3.ͨ��builder ����һ���Ի��� 4.�Ի���show()����
	 */
	public void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("���°汾,������");
		// ����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ
		builer.setPositiveButton("��������", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "����apk,����");
				downLoadApk();
			}
		});
		// ����ȡ����ťʱ���е�¼
		builer.setNegativeButton("�Ժ���˵", new DialogInterface.OnClickListener() {
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
	 * �ӷ�����������APK
	 */
	public void downLoadApk() {
		final ProgressDialog pd; // �������Ի���
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("�������ظ���");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = HttpUtils.getFileFromServer(info.getUrl(), pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); // �������������Ի���
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// ��װapk
	public void installApk(File file) {
		Intent intent = new Intent();
		// ִ�ж���
		intent.setAction(Intent.ACTION_VIEW);
		// ִ�е���������
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	/*
	 * ��������������
	 */
	public void LoginMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		// ��������ǰ��activity
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
