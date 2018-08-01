package com.hzlf.sampletest.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hzlf.sampletest.R;

public class SelectPicActivity extends Activity implements OnClickListener {

	/***
	 * ʹ����������ջ�ȡͼƬ
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * ʹ������е�ͼƬ
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

	/***
	 * ��Intent��ȡͼƬ·����KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	private static final String TAG = "SelectPicActivity";
	private LinearLayout dialogLayout;
	private Button takePhotoBtn, pickPhotoBtn, cancelBtn;

	/** ��ȡ����ͼƬ·�� */
	private String picPath;
	private Intent lastIntent;
	private Uri photoUri;
	private Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_pic);
		_context = this;
		initView();
	}

	/**
	 * ��ʼ������View
	 */
	private void initView() {
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);

		lastIntent = getIntent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo:
			takePhoto();
			break;
		case R.id.btn_pick_photo:
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * ���ջ�ȡͼƬ
	 */
	private void takePhoto() {
		// ִ������ǰ��Ӧ�����ж�SD���Ƿ����
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * ��Ҫ˵��һ�£����²���ʹ����������գ����պ��ͼƬ����������е� ����ʹ�õ����ַ�ʽ��һ���ô����ǻ�ȡ��ͼƬ�����պ��ԭͼ
			 * �����ʵ��ContentValues�����Ƭ·���Ļ������պ��ȡ��ͼƬΪ����ͼ������
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "�ڴ濨������", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * �������ȡͼƬ
	 */
	private void pickPhoto() {
		/*
		 * Intent intent = new Intent(); intent.setType("image/*");
		 * intent.setAction(Intent.ACTION_GET_CONTENT);
		 * startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
		 */
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			startActivityForResult(
					new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
					SELECT_PIC_BY_PICK_PHOTO);
		} else {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			doPhoto(requestCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ѡ��ͼƬ�󣬻�ȡͼƬ��·��
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		switch (requestCode) {
		case SELECT_PIC_BY_PICK_PHOTO:
			if (data != null) {
				photoUri = data.getData();
				if (photoUri != null) {
					int sdkVersion = Build.VERSION.SDK_INT;
					if (sdkVersion >= 19) { // api >= 19
						// return getRealPathFromUriAboveApi19(_context,
						// photoUri);
						if (DocumentsContract.isDocumentUri(_context, photoUri)) {
							// �����document���͵� uri, ��ͨ��document id�����д���
							String documentId = DocumentsContract
									.getDocumentId(photoUri);
							if ("com.android.providers.media.documents"
									.equals(photoUri.getAuthority())) { // MediaProvider
								// ʹ��':'�ָ�
								String id = documentId.split(":")[1];
								String selection = MediaStore.Images.Media._ID
										+ "=?";
								String[] selectionArgs = { id };
								picPath = getDataColumn(
										_context,
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										selection, selectionArgs);
							} else if ("com.android.providers.downloads.documents"
									.equals(photoUri.getAuthority())) { // DownloadsProvider
								Uri contentUri = ContentUris
										.withAppendedId(
												Uri.parse("content://downloads/public_downloads"),
												Long.valueOf(documentId));
								picPath = getDataColumn(_context, contentUri,
										null, null);
							}
						} else if ("content".equalsIgnoreCase(photoUri
								.getScheme())) {
							// ����� content ���͵� Uri
							picPath = getDataColumn(_context, photoUri, null,
									null);
						} else if ("file".equals(photoUri.getScheme())) {
							// ����� file ���͵� Uri,ֱ�ӻ�ȡͼƬ��Ӧ��·��
							picPath = photoUri.getPath();
						}
					} else { // api < 19
						picPath = getDataColumn(_context, photoUri, null, null);
					}
				} else {
					Toast.makeText(this, "ѡ��ͼƬ�ļ�����", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "ѡ��ͼƬ�ļ�����", Toast.LENGTH_LONG).show();
			}
			Log.i("imagePath", picPath);
			if (picPath != null
					&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
							|| picPath.endsWith(".jpg") || picPath
								.endsWith(".JPG"))) {
				lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
				setResult(Activity.RESULT_OK, lastIntent);
				finish();
			} else {
				Toast.makeText(this, "ѡ��ͼƬ�ļ�����ȷ", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	/**
	 * ��ȡ���ݿ���е� _data �У�������Uri��Ӧ���ļ�·��
	 * 
	 * @return
	 */
	private static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		String path = null;

		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
				path = cursor.getString(columnIndex);
				cursor.close();
			}
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
			}
		}
		return path;
	}

}
