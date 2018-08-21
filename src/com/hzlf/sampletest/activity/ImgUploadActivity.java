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
	 * 去上传文件
	 */
	protected static final int TO_UPLOAD_FILE = 1;
	/**
	 * 上传文件响应
	 */
	protected static final int UPLOAD_FILE_DONE = 2; //
	/**
	 * 选择文件
	 */
	public static final int TO_SELECT_PHOTO = 3;
	/**
	 * 上传初始化
	 */
	private static final int UPLOAD_INIT_PROCESS = 4;
	/**
	 * 上传中
	 */
	private static final int UPLOAD_IN_PROCESS = 5;

	private static String requestURL = UsedPath.api_ImgUpload_POST;
	private Button selectButton, uploadButton;

	private GridView gridview_img;
	private ArrayList<HashMap<String, Object>> imagepaths;
	private SimpleAdapter adapter_img_path; // 适配器
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
	 * 初始化数据
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
		 * 载入默认图片添加图片加号 通过适配器实现 SimpleAdapter参数imageItem为数据源
		 * R.layout.griditem_addpic为布局
		 */
		// gridview_img.setEmptyView(findViewById(R.id.text_img));
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridview_addpic); // 加号

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
		 * 监听GridView点击事件 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
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
		data_img_type.add("现场照片");
		data_img_type.add("企业标准");

		// 适配器
		adapter_img_type = new ArrayAdapter<String>(ImgUploadActivity.this,
				android.R.layout.simple_spinner_item, data_img_type); // 设置样式
		adapter_img_type
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
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
			// 实例化
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置进度条风格，风格为圆形，旋转的
			mypDialog.setTitle("上传图片中...");
			// 设置ProgressDialog 标题
			mypDialog.setIndeterminate(false);
			// 设置ProgressDialog 的进度条是否不明确
			mypDialog.setCancelable(false);
			// 设置ProgressDialog 是否可以按退回按键取消
			mypDialog.show();
			// 让ProgressDialog显示

			img_type = spinner_img_type.getSelectedItem().toString();
			if (list_paths.size() > 0) {
				for (String onepath : list_paths) {
					picPath = onepath;

					if (picPath != null) {
						toUploadFile();
						handler.sendEmptyMessage(TO_UPLOAD_FILE);
						// Log.e("picPath", picPath);
					} else {
						Toast.makeText(this, "上传的文件路径出错", Toast.LENGTH_LONG)
								.show();
					}
				}

			} else if (list_paths.size() == 0) {
				mypDialog.dismiss();
				Toast.makeText(this, "无图片", Toast.LENGTH_LONG).show();
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

	// 刷新图片
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
			// 该方法已过时，使用getRealSize()方法替代。也可以使用getSize()，但是不能准确的获取到分辨率
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
			// 刷新后释放防止手机休眠后自动添加
			picPath = null;
		}
	}

	/*
	 * Dialog对话框提示用户删除操作 position为删除图片位置
	 */
	protected void dialog(final int position) {
		String string = "是否删除该图片";
		AlertDialog.Builder builder = new Builder(ImgUploadActivity.this);
		builder.setMessage(string);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
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
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 上传服务器响应回调
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
		uploadUtil.setOnUploadProcessListener(this); // 设置监听器监听上传状态

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
				if (result.equals("上传失败")) {
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
									"共上传" + list_paths.size() + "张图片,其中失败"
											+ fail_num + "张",
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
									finish();// 结束当前活动
									ImgUploadActivity.this
											.startActivity(intent_details);
								}
							}, 2000); // 延时1s执行
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
