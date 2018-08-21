package com.hzlf.sampletest.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.UploadImg;
import com.hzlf.sampletest.http.UploadUtil;
import com.hzlf.sampletest.http.UploadUtil.OnUploadProcessListener;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.UsedPath;

public class ImgUploadActivity extends Activity implements OnClickListener,
		OnUploadProcessListener {
	/**
	 * ȥ�ϴ��ļ�
	 */
	protected static final int TO_UPLOAD_FILE = 1;
	/**
	 * �ϴ��ļ���Ӧ
	 */
	protected static final int UPLOAD_FILE_DONE = 2; //
	/**
	 * ѡ���ļ�
	 */
	public static final int TO_SELECT_PHOTO = 3;
	/**
	 * �ϴ���ʼ��
	 */
	private static final int UPLOAD_INIT_PROCESS = 4;
	/**
	 * �ϴ���
	 */
	private static final int UPLOAD_IN_PROCESS = 5;

	private static String requestURL = UsedPath.api_ImgUpload_POST;
	private Button selectButton, uploadButton;

	private GridView gridview_img;
	private ArrayList<HashMap<String, Object>> imagepaths;
	private SimpleAdapter adapter_img_path; // ������
	private int num = 0, fail_num = 0;

	private DBManage dbmanage = new DBManage(this);
	private Spinner spinner_img_type;
	private List<String> data_img_type, list_paths, status;
	private ArrayAdapter<String> adapter_img_type;

	private String picPath = null, img_type = null, number = null;

	private ProgressDialog mypDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_img_upload);
		initView();
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		selectButton = (Button) this.findViewById(R.id.selectImage);
		uploadButton = (Button) this.findViewById(R.id.uploadImage);
		selectButton.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		gridview_img = (GridView) this.findViewById(R.id.gridView_img);
		num = 0;
		number = getIntent().getStringExtra("img_number");
		/*
		 * ����Ĭ��ͼƬ���ͼƬ�Ӻ� ͨ��������ʵ�� SimpleAdapter����imageItemΪ����Դ
		 * R.layout.griditem_addpicΪ����
		 */
		// gridview_img.setEmptyView(findViewById(R.id.text_img));
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridview_addpic); // �Ӻ�

		list_paths = new ArrayList<String>();
		imagepaths = new ArrayList<HashMap<String, Object>>();
		status = new ArrayList<String>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemImage", bmp);
		imagepaths.add(map);
		adapter_img_path = new SimpleAdapter(this, imagepaths,
				R.layout.img_item, new String[] { "itemImage" },
				new int[] { R.id.imageView });

		adapter_img_path.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// TODO Auto-generated method stub
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView i = (ImageView) view;
					i.setImageBitmap((Bitmap) data);
					return true;
				}
				return false;
			}
		});
		/*
		 * ����GridView����¼� ����:�ú���������󷽷� ����Ҫ�ֶ�����import android.view.View;
		 */
		gridview_img.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				dialog(position);
			}
		});
		spinner_img_type = (Spinner) this.findViewById(R.id.spinner_img_type);
		data_img_type = new ArrayList<String>();
		data_img_type.add("�ֳ���Ƭ");
		data_img_type.add("��ҵ��׼");

		// ������
		adapter_img_type = new ArrayAdapter<String>(ImgUploadActivity.this,
				android.R.layout.simple_spinner_item, data_img_type); // ������ʽ
		adapter_img_type
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ����������
		spinner_img_type.setAdapter(adapter_img_type);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			Intent intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, TO_SELECT_PHOTO);
			break;
		case R.id.uploadImage:
			mypDialog = new ProgressDialog(ImgUploadActivity.this);
			// ʵ����
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// ���ý�������񣬷��ΪԲ�Σ���ת��
			mypDialog.setTitle("�ϴ�ͼƬ��...");
			// ����ProgressDialog ����
			mypDialog.setIndeterminate(false);
			// ����ProgressDialog �Ľ������Ƿ���ȷ
			mypDialog.setCancelable(false);
			// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
			mypDialog.show();
			// ��ProgressDialog��ʾ

			img_type = spinner_img_type.getSelectedItem().toString();
			if (list_paths.size() > 0) {
				for (String onepath : list_paths) {
					picPath = onepath;

					if (picPath != null) {
						toUploadFile();
						handler.sendEmptyMessage(TO_UPLOAD_FILE);
						// Log.e("picPath", picPath);
					} else {
						Toast.makeText(this, "�ϴ����ļ�·������", Toast.LENGTH_LONG)
								.show();
					}
				}

			} else if (list_paths.size() == 0) {
				mypDialog.dismiss();
				Toast.makeText(this, "��ͼƬ", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			if (!TextUtils.isEmpty(picPath)) {
				list_paths.add(picPath);
				gridview_img.setVisibility(View.VISIBLE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ˢ��ͼƬ
	@Override
	protected void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(picPath)) {
			Options opt = new Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picPath, opt);
			int imageHeight = opt.outHeight;
			int imageWidth = opt.outWidth;

			Display display = getWindowManager().getDefaultDisplay();
			Point point = new Point();
			// �÷����ѹ�ʱ��ʹ��getRealSize()���������Ҳ����ʹ��getSize()�����ǲ���׼ȷ�Ļ�ȡ���ֱ���
			// int screenHeight = display.getHeight();
			// int screenWidth = display.getWidth();

			display.getRealSize(point);
			int screenHeight = point.y;
			int screenWidth = point.x;

			int scale = 1;
			int scaleWidth = imageWidth / screenWidth;
			int scaleHeigh = imageHeight / screenHeight;
			if (scaleWidth >= scaleHeigh && scaleWidth > 1) {
				scale = scaleWidth;
			} else if (scaleWidth < scaleHeigh && scaleHeigh > 1) {
				scale = scaleHeigh;
			}

			opt.inSampleSize = scale;
			opt.inJustDecodeBounds = false;
			if (num == 0) {
				imagepaths.remove(0);
				num = 1;
			}
			Bitmap bm = BitmapFactory.decodeFile(picPath, opt);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", bm);
			imagepaths.add(map);
			gridview_img.setVisibility(View.VISIBLE);
			adapter_img_path = new SimpleAdapter(this, imagepaths,
					R.layout.img_item, new String[] { "itemImage" },
					new int[] { R.id.imageView });
			adapter_img_path.setViewBinder(new ViewBinder() {
				@Override
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					// TODO Auto-generated method stub
					if (view instanceof ImageView && data instanceof Bitmap) {
						ImageView i = (ImageView) view;
						i.setImageBitmap((Bitmap) data);
						return true;
					}
					return false;
				}
			});
			gridview_img.setAdapter(adapter_img_path);
			adapter_img_path.notifyDataSetChanged();
			// ˢ�º��ͷŷ�ֹ�ֻ����ߺ��Զ����
			picPath = null;
		}
	}

	/*
	 * Dialog�Ի�����ʾ�û�ɾ������ positionΪɾ��ͼƬλ��
	 */
	protected void dialog(final int position) {
		String string = "�Ƿ�ɾ����ͼƬ";
		AlertDialog.Builder builder = new Builder(ImgUploadActivity.this);
		builder.setMessage(string);
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				imagepaths.remove(position);
				list_paths.remove(position);
				if (imagepaths.size() == 0) {
					gridview_img.setVisibility(View.GONE);
				} else {
					adapter_img_path.notifyDataSetChanged();
				}
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * �ϴ���������Ӧ�ص�
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	private void toUploadFile() {

		String fileKey = "pic";
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this); // ���ü����������ϴ�״̬

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", number);
		params.put("type", img_type);
		params.put("name", ((MyApplication) getApplication()).getName());
		uploadUtil.uploadFile(picPath, fileKey, requestURL, params);

		// Log.e("picPath2", picPath);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * case TO_UPLOAD_FILE: toUploadFile(); break;
			 */
			case UPLOAD_FILE_DONE:
				String result = (String) msg.obj;
				if (result.equals("�ϴ�ʧ��")) {
					mypDialog.dismiss();
					Toast.makeText(ImgUploadActivity.this, result,
							Toast.LENGTH_SHORT).show();
					status.add("0");
					fail_num++;
				} else {
					Gson gson = new Gson();
					UploadImg uploadimg = gson
							.fromJson(result, UploadImg.class);
					if (uploadimg.getStatus().equals("success")) {

						status.add("1");

						if (status.size() >= list_paths.size()) {
							mypDialog.dismiss();
							Toast.makeText(
									ImgUploadActivity.this,
									"���ϴ�" + list_paths.size() + "��ͼƬ,����ʧ��"
											+ fail_num + "��",
									Toast.LENGTH_SHORT).show();
							for (int i = 0; i < list_paths.size(); i++) {
								if (status.get(i).equals("1")) {
									dbmanage.addImagePath(number,
											list_paths.get(i));
								}
							}
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									// do something
									Intent intent_details = new Intent();
									intent_details.setClass(
											ImgUploadActivity.this,
											DetailsActivity.class);
									intent_details.putExtra("info_number",
											number);
									finish();// ������ǰ�
									ImgUploadActivity.this
											.startActivity(intent_details);
								}
							}, 2000); // ��ʱ1sִ��
						}
					} else {
						mypDialog.dismiss();
						status.add("0");
						fail_num++;
						Toast.makeText(ImgUploadActivity.this,
								uploadimg.getMessage(), Toast.LENGTH_LONG)
								.show();
					}
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		handler.sendMessage(msg);
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		handler.sendMessage(msg);
	}

}
