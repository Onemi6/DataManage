package com.hzlf.sampletest.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.sign.SignatureView;

public class SignActivity extends Activity {

	private SignatureView mSignaturePad;
	private Button mClearButton;
	private Button mSaveButton;
	private String mark, sign_number;
	private DBManage dbmanage = new DBManage(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign);
		getActionBar().setTitle("被抽样单位签字");

		sign_number = getIntent().getStringExtra("sign_number");

		mSignaturePad = (SignatureView) findViewById(R.id.signature_pad);
		mSignaturePad.setOnSignedListener(new SignatureView.OnSignedListener() {
			@Override
			public void onSigned() {
				mSaveButton.setEnabled(true);
				mClearButton.setEnabled(true);
			}

			@Override
			public void onClear() {
				mSaveButton.setEnabled(false);
				mClearButton.setEnabled(false);
			}
		});

		mClearButton = (Button) findViewById(R.id.clear_button);
		mSaveButton = (Button) findViewById(R.id.save_button);

		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSignaturePad.clear();
			}
		});

		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

				if (getActionBar().getTitle().toString().equals("被抽样单位签字")) {
					/*
					 * mark = ((MyApplication)
					 * SignActivity.this.getApplication())
					 * .getInfoAdd1().getValue1() + "_SUPPLIER";
					 */
					mark = sign_number + "_SUPPLIER";
					if (addSignatureToGallery(signatureBitmap, mark)) {
						Toast.makeText(SignActivity.this, "保存成功",
								Toast.LENGTH_SHORT).show();
						mSignaturePad.clear();
						getActionBar().setTitle("抽样人1签字");
					} else {
						Toast.makeText(SignActivity.this, "保存失败",
								Toast.LENGTH_SHORT).show();
					}
				} else if (getActionBar().getTitle().toString()
						.equals("抽样人1签字")) {
					/*
					 * mark = ((MyApplication)
					 * SignActivity.this.getApplication())
					 * .getInfoAdd1().getValue1() + "_DRAW_MAN_1";
					 */

					mark = sign_number + "_DRAW_MAN_1";
					if (addSignatureToGallery(signatureBitmap, mark)) {
						Toast.makeText(SignActivity.this, "保存成功",
								Toast.LENGTH_SHORT).show();
						mSignaturePad.clear();
						getActionBar().setTitle("抽样人2签字");
					} else {
						Toast.makeText(SignActivity.this, "保存失败",
								Toast.LENGTH_SHORT).show();
					}
				} else if (getActionBar().getTitle().toString()
						.equals("抽样人2签字")) {
					/*
					 * mark = ((MyApplication)
					 * SignActivity.this.getApplication())
					 * .getInfoAdd1().getValue1() + "_DRAW_MAN_2";
					 */
					mark = sign_number + "_DRAW_MAN_2";
					if (addSignatureToGallery(signatureBitmap, mark)) {
						Toast.makeText(SignActivity.this, "保存成功",
								Toast.LENGTH_SHORT).show();

						dbmanage.updateSign(sign_number, 1);

						mSignaturePad.clear();
						Intent intent_sign = new Intent();
						intent_sign.setClass(SignActivity.this,
								MainActivity.class);
						SignActivity.this.finish();// 结束当前活动
						SignActivity.this.startActivity(intent_sign);
					} else {
						Toast.makeText(SignActivity.this, "保存失败",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		});
	}

	public File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			Log.e("SignaturePad", "Directory not created");
		}
		return file;
	}

	public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		OutputStream stream = new FileOutputStream(photo);
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		stream.close();
	}

	public boolean addSignatureToGallery(Bitmap signature, String mark) {
		boolean result = false;
		try {
			File photo = new File(getAlbumStorageDir("Sign_Images"),
					String.format("%s.jpg", mark)); // System.currentTimeMillis()
													// //返回当前系统时间的毫秒数
			saveBitmapToJPG(signature, photo);
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(photo);
			mediaScanIntent.setData(contentUri);
			SignActivity.this.sendBroadcast(mediaScanIntent);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
