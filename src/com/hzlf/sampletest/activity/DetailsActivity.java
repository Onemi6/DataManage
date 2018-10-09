package com.hzlf.sampletest.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Upload;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.MyGridView;
import com.hzlf.sampletest.others.UsedPath;

public class DetailsActivity extends Activity {

	private DBManage dbmanage = new DBManage(this);
	private Button btn_preview, btn_printf, btn_update1, btn_update2,
			btn_update3, btn_sign, btn_upload_data, btn_uploadimg;
	private Context _context;
	private Info_add info_add, info_add_new;
	private String number, str_dayinriqi, str_filename;
	private EditText textview0, textview1_1, textview1_2, textview1_4,
			textview1_5, textview1_6, textview1_7, textview1_8, textview1_9,
			textview2_5, textview2_6, textview2_7, textview2_9, textview2_10,
			textview2_11, textview2_12, textview2_13, textview2_14,
			textview2_15, textview2_16, textview2_17, textview2_18,
			textview3_1, textview3_5, textview3_7, textview3_8, textview3_9,
			textview3_10, textview3_11, textview3_12, textview3_13,
			textview3_15, textview3_16, textview3_21, textview3_22,
			textview3_23, textview3_24, textview3_25, textview3_26,
			textview3_27, textview3_29;
	private Spinner spinner1_3, spinner2_1, spinner2_2, spinner2_3, spinner2_4,
			spinner2_8, spinner3_2, spinner3_3, spinner3_4, spinner3_6,
			spinner3_28, spinner3_14, spinner3_17, spinner3_18, spinner3_19,
			spinner3_20;
	private String[][] data2_2;
	private ArrayAdapter adapter1_3, adapter2_1, adapter2_2, adapter2_3,
			adapter2_4, adapter2_8, adapter3_2, adapter3_3, adapter3_4,
			adapter3_6, adapter3_28, adapter3_14, adapter3_17, adapter3_18,
			adapter3_19, adapter3_20;

	private ProgressDialog mypDialog;
	private Upload upload;

	private MyGridView gridview_upload_img;
	private List<String> list_paths;
	private ArrayList<HashMap<String, Object>> imagepaths;
	private SimpleAdapter adapter_img_path; // 适配器

	private static final int UPLOAD_TRUE = 1;
	private static final int UPLOAD_FLASE = 0;
	private static final int UPLOAD_NO = 2;
	private Map<String, String> body = new HashMap<String, String>();

	private int sign = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_TRUE:
				upload = (Upload) msg.obj;
				dbmanage.updateId(upload.getId(), number);

				dbmanage.updateNumber(number, 1, 1, 1);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// do something
						mypDialog.dismiss();
						Toast.makeText(_context,
								"上传" + number + upload.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}, 1000); // 延时1s执行

				break;
			case UPLOAD_FLASE:
				mypDialog.dismiss();
				Toast.makeText(_context, "该" + (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case UPLOAD_NO:
				mypDialog.dismiss();
				Toast.makeText(_context, (String) msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_details);
		_context = this;

		number = getIntent().getStringExtra("info_number");
		info_add = dbmanage.findInfo_details(number);
		str_filename = info_add.getInfo_add1().getValue1() + ".doc";

		list_paths = dbmanage.findList_UploadImage(number);
		imagepaths = new ArrayList<HashMap<String, Object>>();
		gridview_upload_img = (MyGridView) this
				.findViewById(R.id.gridView_opload_img);
		gridview_upload_img.setEmptyView(findViewById(R.id.text_upload_img));
		if (list_paths.size() > 0) {
			for (String path : list_paths) {
				Options opt = new Options();
				opt.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, opt);
				int imageHeight = opt.outHeight;
				int imageWidth = opt.outWidth;
				Display display = getWindowManager().getDefaultDisplay();
				Point point = new Point();
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
				Bitmap bm = BitmapFactory.decodeFile(path, opt);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("itemImage", bm);
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
			}
		}
		gridview_upload_img.setAdapter(adapter_img_path);
		gridview_upload_img.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.img_item,
						(ViewGroup) findViewById(R.id.dialog_img));
				ImageView imageview = (ImageView) layout
						.findViewById(R.id.imageView);
				Bitmap bm = (Bitmap) imagepaths.get(position).get("itemImage");
				imageview.setImageBitmap(bm);

				AlertDialog.Builder dialog_img = new AlertDialog.Builder(
						DetailsActivity.this).setView(layout)
						.setPositiveButton("确定", null);
				dialog_img.show();
			}
		});

		textview0 = (EditText) findViewById(R.id.details_choujianyuanbianhao);

		textview1_1 = (EditText) findViewById(R.id.details_choujiandanbianhao);
		textview1_2 = (EditText) findViewById(R.id.details_renwulaiyuan);
		textview1_4 = (EditText) findViewById(R.id.details_danweimingcheng1);
		textview1_5 = (EditText) findViewById(R.id.details_danweidizhi1);
		textview1_6 = (EditText) findViewById(R.id.details_lianxiren1);
		textview1_7 = (EditText) findViewById(R.id.details_dianhua1);
		textview1_8 = (EditText) findViewById(R.id.details_chuanzhen1);
		textview1_9 = (EditText) findViewById(R.id.details_youbian1);

		textview2_5 = (EditText) findViewById(R.id.details_danweimingcheng2);
		textview2_6 = (EditText) findViewById(R.id.details_danweidizhi2);
		textview2_7 = (EditText) findViewById(R.id.details_yingyezhizhao);
		textview2_9 = (EditText) findViewById(R.id.details_xukezhenghao);
		textview2_10 = (EditText) findViewById(R.id.details_danweifaren);
		textview2_11 = (EditText) findViewById(R.id.details_nianxiaoshoue);
		textview2_12 = (EditText) findViewById(R.id.details_lianxiren2);
		textview2_13 = (EditText) findViewById(R.id.details_dianhua2);
		textview2_14 = (EditText) findViewById(R.id.details_chuanzhen2);
		textview2_15 = (EditText) findViewById(R.id.details_youbian2);
		textview2_16 = (EditText) findViewById(R.id.details_shengchanzhemingcheng);
		textview2_17 = (EditText) findViewById(R.id.details_shengchanzhedizhi);
		textview2_18 = (EditText) findViewById(R.id.details_dianhua3);

		textview3_1 = (EditText) findViewById(R.id.details_yangpinmingcheng);
		textview3_5 = (EditText) findViewById(R.id.details_yangpinshangbiao);
		textview3_7 = (EditText) findViewById(R.id.details_guigexinghao);
		textview3_8 = (EditText) findViewById(R.id.details_zhiliangdengji);
		textview3_9 = (EditText) findViewById(R.id.details_yangpintiaoma);
		textview3_10 = (EditText) findViewById(R.id.details_shengchanriqi);
		textview3_11 = (EditText) findViewById(R.id.details_baozhiqi);
		textview3_12 = (EditText) findViewById(R.id.details_chanpinpihao);
		textview3_13 = (EditText) findViewById(R.id.details_yangpindanjia);
		textview3_15 = (EditText) findViewById(R.id.details_yuanchandi);
		textview3_16 = (EditText) findViewById(R.id.details_chouyangriqi);
		textview3_21 = (EditText) findViewById(R.id.details_chouyangren);
		textview3_22 = (EditText) findViewById(R.id.details_zhixingbiaozhun);
		textview3_23 = (EditText) findViewById(R.id.details_chouyangshuliangdanwei);
		textview3_24 = (EditText) findViewById(R.id.details_chouyangjishu);
		textview3_25 = (EditText) findViewById(R.id.details_beiyangshuliang);
		textview3_26 = (EditText) findViewById(R.id.details_chouyangshuliang);
		textview3_27 = (EditText) findViewById(R.id.details_beizhu);
		textview3_29 = (EditText) findViewById(R.id.details_yangpinxukezheng);

		spinner1_3 = (Spinner) findViewById(R.id.details_renwuleibie);
		spinner2_1 = (Spinner) findViewById(R.id.details_suozaidi);
		spinner2_2 = (Spinner) findViewById(R.id.details_chouyangdidian);
		spinner2_3 = (Spinner) findViewById(R.id.details_quyuleixing);
		spinner2_4 = (Spinner) findViewById(R.id.details_chouyanghuanjie);
		spinner2_8 = (Spinner) findViewById(R.id.details_xukezhengleixing);
		spinner3_2 = (Spinner) findViewById(R.id.details_yangpinleixing);
		spinner3_3 = (Spinner) findViewById(R.id.details_yangpinlaiyuan);
		spinner3_4 = (Spinner) findViewById(R.id.details_yangpinshuxing);
		spinner3_6 = (Spinner) findViewById(R.id.details_baozhuangfenlei);
		spinner3_14 = (Spinner) findViewById(R.id.details_shifouchukou);
		spinner3_17 = (Spinner) findViewById(R.id.details_chouyangfangshi);
		spinner3_18 = (Spinner) findViewById(R.id.details_yangpinxingtai);
		spinner3_19 = (Spinner) findViewById(R.id.details_yangpinbaozhuang);
		spinner3_20 = (Spinner) findViewById(R.id.details_chucuntiaojian);
		spinner3_28 = (Spinner) findViewById(R.id.details_riqileixing);

		adapter1_3 = ArrayAdapter.createFromResource(_context,
				R.array.array1_3, android.R.layout.simple_spinner_item);
		spinner1_3.setAdapter(adapter1_3);

		adapter2_1 = ArrayAdapter.createFromResource(_context,
				R.array.array2_1, android.R.layout.simple_spinner_item);
		spinner2_1.setAdapter(adapter2_1);

		adapter2_3 = ArrayAdapter.createFromResource(_context,
				R.array.array2_3, android.R.layout.simple_spinner_item);
		spinner2_3.setAdapter(adapter2_3);

		adapter2_4 = ArrayAdapter.createFromResource(_context,
				R.array.array2_4, android.R.layout.simple_spinner_item);
		spinner2_4.setAdapter(adapter2_4);

		adapter2_8 = ArrayAdapter.createFromResource(_context,
				R.array.array2_8, android.R.layout.simple_spinner_item);
		spinner2_8.setAdapter(adapter2_8);

		adapter3_2 = ArrayAdapter.createFromResource(_context,
				R.array.array3_2, android.R.layout.simple_spinner_item);
		spinner3_2.setAdapter(adapter3_2);

		adapter3_3 = ArrayAdapter.createFromResource(_context,
				R.array.array3_3, android.R.layout.simple_spinner_item);
		spinner3_3.setAdapter(adapter3_3);

		adapter3_4 = ArrayAdapter.createFromResource(_context,
				R.array.array3_4, android.R.layout.simple_spinner_item);
		spinner3_4.setAdapter(adapter3_4);

		adapter3_6 = ArrayAdapter.createFromResource(_context,
				R.array.array3_6, android.R.layout.simple_spinner_item);
		spinner3_6.setAdapter(adapter3_6);

		adapter3_28 = ArrayAdapter.createFromResource(_context,
				R.array.array3_28, android.R.layout.simple_spinner_item);
		spinner3_28.setAdapter(adapter3_28);

		adapter3_14 = ArrayAdapter.createFromResource(_context,
				R.array.array3_14, android.R.layout.simple_spinner_item);
		spinner3_14.setAdapter(adapter3_14);

		adapter3_17 = ArrayAdapter.createFromResource(_context,
				R.array.array3_17, android.R.layout.simple_spinner_item);
		spinner3_17.setAdapter(adapter3_17);

		adapter3_18 = ArrayAdapter.createFromResource(_context,
				R.array.array3_18, android.R.layout.simple_spinner_item);
		spinner3_18.setAdapter(adapter3_18);

		adapter3_19 = ArrayAdapter.createFromResource(_context,
				R.array.array3_19, android.R.layout.simple_spinner_item);
		spinner3_19.setAdapter(adapter3_19);

		adapter3_20 = ArrayAdapter.createFromResource(_context,
				R.array.array3_20, android.R.layout.simple_spinner_item);
		spinner3_20.setAdapter(adapter3_20);

		data2_2 = new String[][] {
				{ "原辅料库", "生产线", "半成品库", "成品库(待检区)", "成品库(已检区)" },
				{ "农贸市场", "菜市场", "批发市场", "商场", "超市", "网购", "小杂食店", "其他" },
				{ "餐馆(特大型餐馆)", "餐馆(大型餐馆)", "餐馆(中型餐馆)", "餐馆(小型餐馆)", "食堂(机关食堂)",
						"食堂(学校/托幼食堂)", "食堂(企事业单位食堂)", "食堂(建筑工地食堂)", "小吃店",
						"快餐店", "饮品店", "集体用餐配送单位", "中央厨房", "其他" } };
		adapter2_2 = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, data2_2[0]); // 设置样式
		adapter2_2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner2_2.setAdapter(adapter2_2);
		spinner2_4
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO 自动生成的方法存根
						// 使用ArrayAdapter转换数据
						adapter2_2 = new ArrayAdapter<String>(_context,
								android.R.layout.simple_spinner_item,
								data2_2[position]);
						adapter2_2
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
						spinner2_2.setAdapter(adapter2_2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO 自动生成的方法存根
					}
				});

		textview0.setText(info_add.getNO());
		textview0.setEnabled(false);

		textview1_1.setText(info_add.getInfo_add1().getValue1());
		textview1_2.setText(info_add.getInfo_add1().getValue2());
		// textview1_3.setText(info_add.getInfo_add1().getValue3());
		textview1_4.setText(info_add.getInfo_add1().getValue4());
		textview1_5.setText(info_add.getInfo_add1().getValue5());
		textview1_6.setText(info_add.getInfo_add1().getValue6());
		textview1_7.setText(info_add.getInfo_add1().getValue7());
		textview1_8.setText(info_add.getInfo_add1().getValue8());
		textview1_9.setText(info_add.getInfo_add1().getValue9());
		textview1_1.setEnabled(false);
		textview1_2.setEnabled(false);
		spinner1_3.setEnabled(false);
		textview1_4.setEnabled(false);
		textview1_5.setEnabled(false);
		textview1_6.setEnabled(false);
		textview1_7.setEnabled(false);
		textview1_8.setEnabled(false);
		textview1_9.setEnabled(false);

		// textview2_1.setText(info_add.getInfo_add2().getValue1());
		// textview2_2.setText(info_add.getInfo_add2().getValue2());
		// textview2_3.setText(info_add.getInfo_add2().getValue3());
		// textview2_4.setText(info_add.getInfo_add2().getValue4());
		textview2_5.setText(info_add.getInfo_add2().getValue5());
		textview2_6.setText(info_add.getInfo_add2().getValue6());
		textview2_7.setText(info_add.getInfo_add2().getValue7());
		// textview2_8.setText(info_add.getInfo_add2().getValue8());
		textview2_9.setText(info_add.getInfo_add2().getValue9());
		textview2_10.setText(info_add.getInfo_add2().getValue10());
		textview2_11.setText(info_add.getInfo_add2().getValue11());
		textview2_12.setText(info_add.getInfo_add2().getValue12());
		textview2_13.setText(info_add.getInfo_add2().getValue13());
		textview2_14.setText(info_add.getInfo_add2().getValue14());
		textview2_15.setText(info_add.getInfo_add2().getValue15());

		textview2_16.setText(info_add.getInfo_add2().getValue16());
		textview2_17.setText(info_add.getInfo_add2().getValue17());
		textview2_18.setText(info_add.getInfo_add2().getValue18());

		spinner2_1.setEnabled(false);
		spinner2_2.setEnabled(false);
		spinner2_3.setEnabled(false);
		spinner2_4.setEnabled(false);
		textview2_5.setEnabled(false);
		textview2_6.setEnabled(false);
		textview2_7.setEnabled(false);
		spinner2_8.setEnabled(false);
		textview2_9.setEnabled(false);
		textview2_10.setEnabled(false);
		textview2_11.setEnabled(false);
		textview2_12.setEnabled(false);
		textview2_13.setEnabled(false);
		textview2_14.setEnabled(false);
		textview2_15.setEnabled(false);
		textview2_16.setEnabled(false);
		textview2_17.setEnabled(false);
		textview2_18.setEnabled(false);

		textview3_1.setText(info_add.getInfo_add3().getValue1());
		// textview3_2.setText(info_add.getInfo_add3().getValue2());
		// textview3_3.setText(info_add.getInfo_add3().getValue3());
		// textview3_4.setText(info_add.getInfo_add3().getValue4());
		textview3_5.setText(info_add.getInfo_add3().getValue5());
		// textview3_6.setText(info_add.getInfo_add3().getValue6());
		textview3_7.setText(info_add.getInfo_add3().getValue7());
		textview3_8.setText(info_add.getInfo_add3().getValue8());
		textview3_9.setText(info_add.getInfo_add3().getValue9());
		textview3_10.setText(info_add.getInfo_add3().getValue10());
		textview3_11.setText(info_add.getInfo_add3().getValue11());
		textview3_12.setText(info_add.getInfo_add3().getValue12());
		textview3_13.setText(info_add.getInfo_add3().getValue13());
		// textview3_14.setText(info_add.getInfo_add3().getValue14());
		textview3_15.setText(info_add.getInfo_add3().getValue15());
		textview3_16.setText(info_add.getInfo_add3().getValue16());
		// textview3_17.setText(info_add.getInfo_add3().getValue17());
		// textview3_18.setText(info_add.getInfo_add3().getValue18());
		// textview3_19.setText(info_add.getInfo_add3().getValue19());
		// textview3_20.setText(info_add.getInfo_add3().getValue20());
		textview3_21.setText(info_add.getInfo_add3().getValue21());

		textview3_22.setText(info_add.getInfo_add3().getValue22());
		textview3_23.setText(info_add.getInfo_add3().getValue23());
		textview3_24.setText(info_add.getInfo_add3().getValue24());
		textview3_25.setText(info_add.getInfo_add3().getValue25());
		textview3_26.setText(info_add.getInfo_add3().getValue26());
		textview3_27.setText(info_add.getInfo_add3().getValue27());
		textview3_29.setText(info_add.getInfo_add3().getValue29());

		textview3_1.setEnabled(false);
		spinner3_2.setEnabled(false);
		spinner3_3.setEnabled(false);
		spinner3_4.setEnabled(false);
		textview3_5.setEnabled(false);
		spinner3_6.setEnabled(false);
		textview3_7.setEnabled(false);
		textview3_8.setEnabled(false);
		textview3_9.setEnabled(false);
		textview3_10.setEnabled(false);
		textview3_11.setEnabled(false);
		textview3_12.setEnabled(false);
		textview3_13.setEnabled(false);
		spinner3_14.setEnabled(false);
		textview3_15.setEnabled(false);
		textview3_16.setEnabled(false);
		spinner3_17.setEnabled(false);
		spinner3_18.setEnabled(false);
		spinner3_19.setEnabled(false);
		spinner3_20.setEnabled(false);
		textview3_21.setEnabled(false);
		textview3_22.setEnabled(false);
		textview3_23.setEnabled(false);
		textview3_24.setEnabled(false);
		textview3_25.setEnabled(false);
		textview3_26.setEnabled(false);
		textview3_27.setEnabled(false);
		spinner3_28.setEnabled(false);
		textview3_29.setEnabled(false);

		SpinnerAdapter adapter1_3 = spinner1_3.getAdapter();
		for (int i = 0; i < adapter1_3.getCount(); i++) {
			if (info_add.getInfo_add1().getValue3()
					.equals(adapter1_3.getItem(i).toString())) {
				spinner1_3.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter2_1 = spinner2_1.getAdapter();
		for (int i = 0; i < adapter2_1.getCount(); i++) {
			if (info_add.getInfo_add2().getValue1()
					.equals(adapter2_1.getItem(i).toString())) {
				spinner2_1.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter2_4 = spinner2_4.getAdapter();
		for (int i = 0; i < adapter2_4.getCount(); i++) {
			if (info_add.getInfo_add2().getValue4()
					.equals(adapter2_4.getItem(i).toString())) {
				spinner2_4.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter2_2 = spinner2_2.getAdapter();
		for (int i = 0; i < adapter2_2.getCount(); i++) {
			if (info_add.getInfo_add2().getValue2()
					.equals(adapter2_2.getItem(i).toString())) {
				spinner2_2.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter2_3 = spinner2_3.getAdapter();
		for (int i = 0; i < adapter2_3.getCount(); i++) {
			if (info_add.getInfo_add2().getValue3()
					.equals(adapter2_3.getItem(i).toString())) {
				spinner2_3.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter2_8 = spinner2_8.getAdapter();
		for (int i = 0; i < adapter2_8.getCount(); i++) {
			if (info_add.getInfo_add2().getValue8()
					.equals(adapter2_8.getItem(i).toString())) {
				spinner2_8.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_2 = spinner3_2.getAdapter();
		for (int i = 0; i < adapter3_2.getCount(); i++) {
			if (info_add.getInfo_add3().getValue2()
					.equals(adapter3_2.getItem(i).toString())) {
				spinner3_2.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_3 = spinner3_3.getAdapter();
		for (int i = 0; i < adapter3_3.getCount(); i++) {
			if (info_add.getInfo_add3().getValue3()
					.equals(adapter3_3.getItem(i).toString())) {
				spinner3_3.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_4 = spinner3_4.getAdapter();
		for (int i = 0; i < adapter3_4.getCount(); i++) {
			if (info_add.getInfo_add3().getValue4()
					.equals(adapter3_4.getItem(i).toString())) {
				spinner3_4.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_6 = spinner3_6.getAdapter();
		for (int i = 0; i < adapter3_6.getCount(); i++) {
			if (info_add.getInfo_add3().getValue6()
					.equals(adapter3_6.getItem(i).toString())) {
				spinner3_6.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_28 = spinner3_28.getAdapter();
		for (int i = 0; i < adapter3_28.getCount(); i++) {
			if (info_add.getInfo_add3().getValue28()
					.equals(adapter3_28.getItem(i).toString())) {
				spinner3_28.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_14 = spinner3_14.getAdapter();
		for (int i = 0; i < adapter3_14.getCount(); i++) {
			if (info_add.getInfo_add3().getValue14()
					.equals(adapter3_14.getItem(i).toString())) {
				spinner3_14.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_17 = spinner3_17.getAdapter();
		for (int i = 0; i < adapter3_17.getCount(); i++) {
			if (info_add.getInfo_add3().getValue17()
					.equals(adapter3_17.getItem(i).toString())) {
				spinner3_17.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_18 = spinner3_18.getAdapter();
		for (int i = 0; i < adapter3_18.getCount(); i++) {
			if (info_add.getInfo_add3().getValue18()
					.equals(adapter3_18.getItem(i).toString())) {
				spinner3_18.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_19 = spinner3_19.getAdapter();
		for (int i = 0; i < adapter3_19.getCount(); i++) {
			if (info_add.getInfo_add3().getValue19()
					.equals(adapter3_19.getItem(i).toString())) {
				spinner3_19.setSelection(i, true);
				break;
			}
		}
		SpinnerAdapter adapter3_20 = spinner3_20.getAdapter();
		for (int i = 0; i < adapter3_20.getCount(); i++) {
			if (info_add.getInfo_add3().getValue20()
					.equals(adapter3_20.getItem(i).toString())) {
				spinner3_20.setSelection(i, true);
				break;
			}
		}
		
		btn_update1 = (Button) findViewById(R.id.btn_update1);
		btn_update1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (btn_update1.getText().toString().equals("修改")) {
					textview1_2.setEnabled(true);
					spinner1_3.setEnabled(true);
					textview1_6.setEnabled(true);
					textview1_7.setEnabled(true);
					textview1_8.setEnabled(true);
					textview1_9.setEnabled(true);
					btn_update1.setText("确认");
				} else if (btn_update1.getText().toString().equals("确认")) {
					textview1_2.setEnabled(false);
					spinner1_3.setEnabled(false);
					textview1_6.setEnabled(false);
					textview1_7.setEnabled(false);
					textview1_8.setEnabled(false);
					textview1_9.setEnabled(false);
					btn_update1.setText("修改");
					info_add.getInfo_add1().setValue2(
							textview1_2.getText().toString());
					info_add.getInfo_add1().setValue3(
							spinner1_3.getSelectedItem().toString());
					info_add.getInfo_add1().setValue6(
							textview1_6.getText().toString());
					info_add.getInfo_add1().setValue7(
							textview1_7.getText().toString());
					info_add.getInfo_add1().setValue8(
							textview1_8.getText().toString());
					info_add.getInfo_add1().setValue9(
							textview1_9.getText().toString());
					dbmanage.updateinfo(info_add);
				}
			}
		});
		btn_update2 = (Button) findViewById(R.id.btn_update2);
		btn_update2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (btn_update2.getText().toString().equals("修改")) {
					spinner2_1.setEnabled(true);
					spinner2_2.setEnabled(true);
					spinner2_3.setEnabled(true);
					spinner2_4.setEnabled(true);
					textview2_5.setEnabled(true);
					textview2_6.setEnabled(true);
					textview2_7.setEnabled(true);
					spinner2_8.setEnabled(true);
					textview2_9.setEnabled(true);
					textview2_10.setEnabled(true);
					textview2_11.setEnabled(true);
					textview2_12.setEnabled(true);
					textview2_13.setEnabled(true);
					textview2_14.setEnabled(true);
					textview2_15.setEnabled(true);
					textview2_16.setEnabled(true);
					textview2_17.setEnabled(true);
					textview2_18.setEnabled(true);
					btn_update2.setText("确认");
				} else if (btn_update2.getText().toString().equals("确认")) {
					spinner2_1.setEnabled(false);
					spinner2_2.setEnabled(false);
					spinner2_3.setEnabled(false);
					spinner2_4.setEnabled(false);
					textview2_5.setEnabled(false);
					textview2_6.setEnabled(false);
					textview2_7.setEnabled(false);
					spinner2_8.setEnabled(false);
					textview2_9.setEnabled(false);
					textview2_10.setEnabled(false);
					textview2_11.setEnabled(false);
					textview2_12.setEnabled(false);
					textview2_13.setEnabled(false);
					textview2_14.setEnabled(false);
					textview2_15.setEnabled(false);
					textview2_16.setEnabled(false);
					textview2_17.setEnabled(false);
					textview2_18.setEnabled(false);
					btn_update2.setText("修改");
					info_add.getInfo_add2().setValue1(
							spinner2_1.getSelectedItem().toString());
					info_add.getInfo_add2().setValue2(
							spinner2_2.getSelectedItem().toString());
					info_add.getInfo_add2().setValue3(
							spinner2_3.getSelectedItem().toString());
					info_add.getInfo_add2().setValue4(
							spinner2_4.getSelectedItem().toString());
					info_add.getInfo_add2().setValue5(
							textview2_5.getText().toString());
					info_add.getInfo_add2().setValue6(
							textview2_6.getText().toString());
					info_add.getInfo_add2().setValue7(
							textview2_7.getText().toString());
					info_add.getInfo_add2().setValue8(
							spinner2_8.getSelectedItem().toString());
					info_add.getInfo_add2().setValue9(
							textview2_9.getText().toString());
					info_add.getInfo_add2().setValue10(
							textview2_10.getText().toString());
					info_add.getInfo_add2().setValue11(
							textview2_11.getText().toString());
					info_add.getInfo_add2().setValue12(
							textview2_12.getText().toString());
					info_add.getInfo_add2().setValue13(
							textview2_13.getText().toString());
					info_add.getInfo_add2().setValue14(
							textview2_14.getText().toString());
					info_add.getInfo_add2().setValue15(
							textview2_15.getText().toString());
					info_add.getInfo_add2().setValue16(
							textview2_16.getText().toString());
					info_add.getInfo_add2().setValue17(
							textview2_17.getText().toString());
					info_add.getInfo_add2().setValue18(
							textview2_18.getText().toString());

					dbmanage.updateinfo(info_add);
				}

			}
		});
		btn_update3 = (Button) findViewById(R.id.btn_update3);
		btn_update3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (btn_update3.getText().toString().equals("修改")) {
					textview3_1.setEnabled(true);
					spinner3_2.setEnabled(true);
					spinner3_3.setEnabled(true);
					spinner3_4.setEnabled(true);
					textview3_5.setEnabled(true);
					spinner3_6.setEnabled(true);
					textview3_7.setEnabled(true);
					textview3_8.setEnabled(true);
					textview3_9.setEnabled(true);
					textview3_10.setEnabled(true);
					textview3_11.setEnabled(true);
					textview3_12.setEnabled(true);
					textview3_13.setEnabled(true);
					spinner3_14.setEnabled(true);
					textview3_15.setEnabled(true);
					// textview3_16.setEnabled(true);
					spinner3_17.setEnabled(true);
					spinner3_18.setEnabled(true);
					spinner3_19.setEnabled(true);
					spinner3_20.setEnabled(true);
					textview3_21.setEnabled(true);
					textview3_22.setEnabled(true);
					textview3_23.setEnabled(true);
					textview3_24.setEnabled(true);
					textview3_25.setEnabled(true);
					textview3_26.setEnabled(true);
					textview3_27.setEnabled(true);
					spinner3_28.setEnabled(true);
					textview3_29.setEnabled(true);
					btn_update3.setText("确认");
				} else if (btn_update3.getText().toString().equals("确认")) {
					textview3_1.setEnabled(false);
					spinner3_2.setEnabled(false);
					spinner3_3.setEnabled(false);
					spinner3_4.setEnabled(false);
					textview3_5.setEnabled(false);
					spinner3_6.setEnabled(false);
					textview3_7.setEnabled(false);
					textview3_8.setEnabled(false);
					textview3_9.setEnabled(false);
					textview3_10.setEnabled(false);
					textview3_11.setEnabled(false);
					textview3_12.setEnabled(false);
					textview3_13.setEnabled(false);
					spinner3_14.setEnabled(false);
					textview3_15.setEnabled(false);
					// textview3_16.setEnabled(false);
					spinner3_17.setEnabled(false);
					spinner3_18.setEnabled(false);
					spinner3_19.setEnabled(false);
					spinner3_20.setEnabled(false);
					textview3_21.setEnabled(false);
					textview3_22.setEnabled(false);
					textview3_23.setEnabled(false);
					textview3_24.setEnabled(false);
					textview3_25.setEnabled(false);
					textview3_26.setEnabled(false);
					textview3_27.setEnabled(false);
					spinner3_28.setEnabled(false);
					textview3_29.setEnabled(false);
					btn_update3.setText("修改");
					info_add.getInfo_add3().setValue1(
							textview3_1.getText().toString());
					info_add.getInfo_add3().setValue2(
							spinner3_2.getSelectedItem().toString());
					info_add.getInfo_add3().setValue3(
							spinner3_3.getSelectedItem().toString());
					info_add.getInfo_add3().setValue4(
							spinner3_4.getSelectedItem().toString());
					info_add.getInfo_add3().setValue5(
							textview3_5.getText().toString());
					info_add.getInfo_add3().setValue6(
							spinner3_6.getSelectedItem().toString());
					info_add.getInfo_add3().setValue7(
							textview3_7.getText().toString());
					info_add.getInfo_add3().setValue8(
							textview3_8.getText().toString());
					info_add.getInfo_add3().setValue9(
							textview3_9.getText().toString());
					info_add.getInfo_add3().setValue10(
							textview3_10.getText().toString());
					info_add.getInfo_add3().setValue11(
							textview3_11.getText().toString());
					info_add.getInfo_add3().setValue12(
							textview3_12.getText().toString());
					info_add.getInfo_add3().setValue13(
							textview3_13.getText().toString());
					info_add.getInfo_add3().setValue14(
							spinner3_14.getSelectedItem().toString());
					info_add.getInfo_add3().setValue15(
							textview3_15.getText().toString());
					// info_add.getInfo_add3().setValue16(textview3_16.getText().toString());
					info_add.getInfo_add3().setValue17(
							spinner3_17.getSelectedItem().toString());
					info_add.getInfo_add3().setValue18(
							spinner3_18.getSelectedItem().toString());
					info_add.getInfo_add3().setValue19(
							spinner3_19.getSelectedItem().toString());
					info_add.getInfo_add3().setValue20(
							spinner3_20.getSelectedItem().toString());
					info_add.getInfo_add3().setValue21(
							textview3_21.getText().toString());
					info_add.getInfo_add3().setValue22(
							textview3_22.getText().toString());
					info_add.getInfo_add3().setValue23(
							textview3_23.getText().toString());
					info_add.getInfo_add3().setValue24(
							textview3_24.getText().toString());
					info_add.getInfo_add3().setValue25(
							textview3_25.getText().toString());
					info_add.getInfo_add3().setValue26(
							textview3_26.getText().toString());
					info_add.getInfo_add3().setValue27(
							textview3_27.getText().toString());
					info_add.getInfo_add3().setValue28(
							spinner3_28.getSelectedItem().toString());
					info_add.getInfo_add3().setValue29(
							textview3_29.getText().toString());
					dbmanage.updateinfo(info_add);
				}

			}
		});

		btn_preview = (Button) findViewById(R.id.btn_preview);
		btn_preview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (btn_update1.getText().toString().equals("修改")
						&& btn_update2.getText().toString().equals("修改")
						&& btn_update3.getText().toString().equals("修改")) {
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					str_dayinriqi = year + "年" + (month + 1) + "月" + day + "日";
					info_add_new = dbmanage.findInfo_details(number);
					doScan();

					Toast.makeText(DetailsActivity.this, "预览" + str_filename,
							Toast.LENGTH_SHORT).show();
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							doOpenWord();
						}
					}, 1500);// 延时1.5s执行
				} else {
					Toast.makeText(DetailsActivity.this, "部分修改未确认",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		btn_printf = (Button) findViewById(R.id.btn_print);
		btn_printf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (btn_update1.getText().toString().equals("修改")
						&& btn_update2.getText().toString().equals("修改")
						&& btn_update3.getText().toString().equals("修改")) {
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					str_dayinriqi = year + "年" + (month + 1) + "月" + day + "日";
					info_add_new = dbmanage.findInfo_details(number);

					doScan();
					Toast.makeText(DetailsActivity.this,
							"生成" + str_filename + "成功", Toast.LENGTH_SHORT)
							.show();
					dbmanage.updateNumber(number, 1, 1, 0);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// do something
							if (appIsInstalled(_context,
									"com.dynamixsoftware.printershare")) {
								Intent intent = new Intent();
								ComponentName comp = new ComponentName(
										"com.dynamixsoftware.printershare",
										"com.dynamixsoftware.printershare.ActivityPrintDocuments");
								intent = new Intent();
								intent.setComponent(comp);
								intent.setAction("android.intent.action.VIEW");
								intent.setType("application/doc");
								intent.setData(Uri.fromFile(new File(
										Environment
												.getExternalStorageDirectory()
												+ "/doc/" + str_filename)));
								startActivity(intent);
							} else {
								Toast.makeText(DetailsActivity.this,
										"未找到PrinterShare软件", Toast.LENGTH_LONG)
										.show();
								// 安装apk
								Intent intent = new Intent(Intent.ACTION_VIEW);
								File file = getAssetFileToCacheDir(_context,
										"PrinterShare.apk");
								intent.setDataAndType(Uri.fromFile(file),
										"application/vnd.android.package-archive");
								DetailsActivity.this.startActivity(intent);

							}
						}
					}, 3000); // 延时3s执行
				} else {
					Toast.makeText(DetailsActivity.this, "部分修改未确认",
							Toast.LENGTH_SHORT).show();

				}
			}
		});
		if (dbmanage.findSign(number) == null) {
			dbmanage.updateSign(number, 0);
		} else {
			if (dbmanage.findSign(number).equals("1")) {
				sign = 1;
			}
		}

		btn_sign = (Button) findViewById(R.id.btn_sign);
		btn_sign.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sign == 0) {
					Intent intent_sign = new Intent();
					intent_sign.setClass(DetailsActivity.this,
							SignActivity.class);
					intent_sign.putExtra("sign_number", number);
					// finish();// 结束当前活动
					startActivity(intent_sign);
				} else {
					Toast.makeText(_context, "已经签名", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_upload_data = (Button) findViewById(R.id.btn_upload_data);
		btn_upload_data.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isNetworkAvailable(_context)) {
					mypDialog = new ProgressDialog(DetailsActivity.this);
					// 实例化
					mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					// 设置进度条风格，风格为圆形，旋转的
					mypDialog.setTitle("上传数据中");
					// 设置ProgressDialog 标题
					mypDialog.setIndeterminate(false);
					// 设置ProgressDialog 的进度条是否不明确
					mypDialog.setCancelable(false);
					// 设置ProgressDialog 是否可以按退回按键取消
					mypDialog.show();
					// 让ProgressDialog显示

					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							Info_add info_add_upload = dbmanage
									.findInfo_details(number);
							body.put("GOODS_NAME", info_add_upload
									.getInfo_add3().getValue1());
							body.put("BUSINESS_SOURCE", info_add_upload
									.getInfo_add1().getValue2());
							body.put("SAMPLE_SOURCE", info_add_upload
									.getInfo_add3().getValue3());
							body.put("I_AND_O", info_add_upload.getInfo_add3()
									.getValue14());
							body.put("DOMESTIC_AREA", info_add_upload
									.getInfo_add2().getValue3());
							body.put("SAMPLE_STATUS", info_add_upload
									.getInfo_add3().getValue18());

							body.put("MANU_COMPANY", info_add_upload
									.getInfo_add2().getValue16());
							body.put("MANU_COMPANY_ADDR", info_add_upload
									.getInfo_add2().getValue17());
							body.put("MANU_COMPANY_PHONE", info_add_upload
									.getInfo_add2().getValue18());

							body.put("DRAW_MAN", info_add_upload.getInfo_add3()
									.getValue21());
							body.put("DRAW_DATE", info_add_upload
									.getInfo_add3().getValue16());

							body.put("REMARK", info_add_upload.getInfo_add3()
									.getValue27());
							body.put("RECORDER",
									((MyApplication) getApplication())
											.getName());
							body.put("TRADEMARK", info_add_upload
									.getInfo_add3().getValue5());
							body.put("PACK", info_add_upload.getInfo_add3()
									.getValue19());
							body.put("SAMPLE_CLASS", info_add_upload
									.getInfo_add3().getValue8());
							body.put("SAMPLE_MODEL", info_add_upload
									.getInfo_add3().getValue7());
							body.put("DRAW_ORG", info_add_upload.getInfo_add1()
									.getValue4());
							body.put("DRAW_NUM", info_add_upload.getInfo_add3()
									.getValue24()
									+ info_add_upload.getInfo_add3()
											.getValue23());
							body.put("DRAW_ADDR", info_add_upload
									.getInfo_add2().getValue2());
							body.put("DATE_PRODUCT", info_add_upload
									.getInfo_add3().getValue10());
							body.put("SUPPLIER", info_add_upload.getInfo_add2()
									.getValue5());
							body.put("SAMPLING_NO", info_add_upload
									.getInfo_add1().getValue1());
							body.put("EXPIRATIONDATE", info_add_upload
									.getInfo_add3().getValue11());
							body.put("SUPPLIER_PHONE", info_add_upload
									.getInfo_add2().getValue13());
							body.put("SAVE_MODE", info_add_upload
									.getInfo_add3().getValue20());
							body.put("STORAGESITE", info_add_upload
									.getInfo_add3().getValue25()
									+ info_add_upload.getInfo_add3()
											.getValue23());
							body.put("SUPPLIER_PERSON", info_add_upload
									.getInfo_add2().getValue12());
							body.put("SUPPLIER_ADDR", info_add_upload
									.getInfo_add2().getValue6());
							body.put("SUPPLIER_LEGAL", info_add_upload
									.getInfo_add2().getValue10());
							body.put("SUPPLIER_FAX", info_add_upload
									.getInfo_add2().getValue14());
							body.put("SAMPLE_TYPE", info_add_upload
									.getInfo_add1().getValue3());
							body.put("ANNUAL_SALES", info_add_upload
									.getInfo_add2().getValue11());
							body.put("BUSINESS_LICENCE", info_add_upload
									.getInfo_add2().getValue7());
							body.put("PERMIT_TYPE", info_add_upload
									.getInfo_add2().getValue8());
							body.put("PERMIT_NUM", info_add_upload
									.getInfo_add2().getValue9());
							body.put("SUPPLIER_ZIPCODE", info_add_upload
									.getInfo_add2().getValue15());
							body.put("SAMPLE_PROPERTY", info_add_upload
									.getInfo_add3().getValue4());
							body.put("SAMPLE_STYLE", info_add_upload
									.getInfo_add3().getValue2());
							body.put("SAMPLE_NUMBER", info_add_upload
									.getInfo_add3().getValue12());
							body.put("PRODUCTION_CERTIFICATE", info_add_upload
									.getInfo_add2().getValue9());
							body.put("UNIVALENT", info_add_upload
									.getInfo_add3().getValue13());
							body.put("PACK_TYPE", info_add_upload
									.getInfo_add3().getValue6());
							// body.put("DRAW_METHOD", info_add_upload
							// .getInfo_add3().getValue17());
							body.put("DRAW_METHOD", info_add_upload
									.getInfo_add3().getValue17());
							body.put("DRAW_PERSON", info_add_upload
									.getInfo_add1().getValue6());
							body.put("DRAW_PHONE", info_add_upload
									.getInfo_add1().getValue7());
							body.put("DRAW_FAX", info_add_upload.getInfo_add1()
									.getValue8());
							body.put("DRAW_ZIPCODE", info_add_upload
									.getInfo_add1().getValue9());
							body.put("DRAW_ORG_ADDR", info_add_upload
									.getInfo_add1().getValue5());
							body.put("DRAW_AMOUNT", info_add_upload
									.getInfo_add3().getValue26()
									+ info_add_upload.getInfo_add3()
											.getValue23());
							body.put("TEST_FILE_NO", info_add_upload
									.getInfo_add3().getValue22());
							body.put("DATE_PRODUCT_TYPE", info_add_upload
									.getInfo_add3().getValue28());
							String result = HttpUtils.LoginCheck(
									UsedPath.api_DataUpload_POST, body);

							if (result.equals("获取数据失败") || result.equals("")) {
								Message message = new Message();
								message.what = UPLOAD_FLASE;
								message.obj = "操作失败";
								handler.sendMessage(message);
							} else {
								Gson gson = new Gson();
								upload = gson.fromJson(result, Upload.class);
								if (upload.getStatus().equals("success")) {
									Message message = new Message();
									message.what = UPLOAD_TRUE;
									message.obj = upload;
									handler.sendMessage(message);
								} else {
									Message message = new Message();
									message.what = UPLOAD_FLASE;
									message.obj = upload.getMessage();
									handler.sendMessage(message);
								}
							}
						}
					});
					thread.start();

				} else {
					// Looper.prepare();
					Toast.makeText(_context, "当前无网络,请稍后再上传", Toast.LENGTH_SHORT)
							.show();
					// Looper.loop();
				}
			}
		});

		btn_uploadimg = (Button) findViewById(R.id.btn_upload_img);
		btn_uploadimg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_uplod_img = new Intent();
				intent_uplod_img.setClass(DetailsActivity.this,
						ImgUploadActivity.class);
				intent_uplod_img.putExtra("img_number", number);
				finish();// 结束当前活动
				startActivity(intent_uplod_img);
			}
		});
	}

	// 生成Word-----------------------------------------------------------------------
	private void doScan() {
		String ALBUM_PATH = null;
		boolean sdCardExist = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			ALBUM_PATH = Environment.getExternalStorageDirectory() + "/doc/";
		} else {
			ALBUM_PATH = this.getCacheDir().toString() + "/";
		}
		File dir = new File(ALBUM_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 获取模板文件
		// File demoFile=new
		// File("E:\\Android\\java\\code\\PoiWordModel\\assets//aa.doc");
		// 创建生成的文件

		/*
		 * File newFile = new File(Environment.getExternalStorageDirectory() +
		 * "/doc/" + str_filename);
		 */

		String targetPath = Environment.getExternalStorageDirectory() + "/doc/"
				+ str_filename;

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("$CJDBH$", info_add_new.getInfo_add1().getValue1());
		map.put("$RWLY$", info_add_new.getInfo_add1().getValue2());
		map.put("$RWLB$", info_add_new.getInfo_add1().getValue3());
		map.put("$DWMC1$", info_add_new.getInfo_add1().getValue4());
		map.put("$DZ1$", info_add_new.getInfo_add1().getValue5());
		map.put("$LXR1$", info_add_new.getInfo_add1().getValue6());
		map.put("$DH1$", info_add_new.getInfo_add1().getValue7());
		map.put("$CZ1$", info_add_new.getInfo_add1().getValue8());
		map.put("$YB1$", info_add_new.getInfo_add1().getValue9());

		// 所在地
		map.put("$CYDD$", info_add_new.getInfo_add2().getValue2());
		map.put("$QYLX$", info_add_new.getInfo_add2().getValue3());
		map.put("$CYHJ$", info_add_new.getInfo_add2().getValue4());
		map.put("$DWMC2$", info_add_new.getInfo_add2().getValue5());
		map.put("$DWDZ2$", info_add_new.getInfo_add2().getValue6());
		map.put("$YYZZ$", info_add_new.getInfo_add2().getValue7());
		map.put("$QYXKZ$", info_add_new.getInfo_add2().getValue8()
				+ info_add_new.getInfo_add2().getValue9());
		map.put("$FRDB$", info_add_new.getInfo_add2().getValue10());
		map.put("$NXSE$", info_add_new.getInfo_add2().getValue11());
		map.put("$LXR2$", info_add_new.getInfo_add2().getValue12());
		map.put("$DH2$", info_add_new.getInfo_add2().getValue13());
		map.put("$CZ2$", info_add_new.getInfo_add2().getValue14());
		map.put("$YB2$", info_add_new.getInfo_add2().getValue15());

		map.put("$SCZMC$", info_add_new.getInfo_add2().getValue16());
		map.put("$SCZDZ$", info_add_new.getInfo_add2().getValue17());
		map.put("$DH3$", info_add_new.getInfo_add2().getValue18());

		// 样品条码 原产地 抽样日期 抽样人
		map.put("$YPMC$", info_add_new.getInfo_add3().getValue1());
		map.put("$YPLX$", info_add_new.getInfo_add3().getValue2());
		map.put("$YPLY$", info_add_new.getInfo_add3().getValue3());
		map.put("$YPSX$", info_add_new.getInfo_add3().getValue4());
		map.put("$SB$", info_add_new.getInfo_add3().getValue5());
		map.put("$BZFL$", info_add_new.getInfo_add3().getValue6());
		map.put("$GGXH$", info_add_new.getInfo_add3().getValue7());
		map.put("$ZLDJ$", info_add_new.getInfo_add3().getValue8());
		map.put("$SCRQ$", info_add_new.getInfo_add3().getValue10());
		map.put("$BZQ$", info_add_new.getInfo_add3().getValue11());
		map.put("$CPPH$", info_add_new.getInfo_add3().getValue12());
		map.put("$DJ$", info_add_new.getInfo_add3().getValue13());
		map.put("$SFCK$", info_add_new.getInfo_add3().getValue14());
		// map.put("$CYRQ$", info_add_new.getInfo_add3().getValue16());
		map.put("$CYFS$", info_add_new.getInfo_add3().getValue17());
		map.put("$YPXT$", info_add_new.getInfo_add3().getValue18());
		map.put("$YPBZ$", info_add_new.getInfo_add3().getValue19());
		map.put("$CCTJ$", info_add_new.getInfo_add3().getValue20());
		map.put("$ZXBZ$", info_add_new.getInfo_add3().getValue22());
		map.put("$CYJS$", info_add_new.getInfo_add3().getValue24()
				+ info_add_new.getInfo_add3().getValue23());
		map.put("$BYSL$", info_add_new.getInfo_add3().getValue25()
				+ info_add_new.getInfo_add3().getValue23());
		map.put("$CYSL$", info_add_new.getInfo_add3().getValue26()
				+ info_add_new.getInfo_add3().getValue23());
		map.put("$BZ$", info_add_new.getInfo_add3().getValue27());
		map.put("$RQLX$", info_add_new.getInfo_add3().getValue28());
		map.put("$DYRQ$", str_dayinriqi);

		map.put("$YPXKZ$", info_add_new.getInfo_add3().getValue29());

		// android无法插入图片
		writeDoc("yuan.doc", targetPath, map);
		// 查看
		// doOpenWord();
	}

	/**
	 * 调用手机中安装的可打开word的软件
	 */

	private void doOpenWord() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String fileMimeType = "application/msword";
		intent.setDataAndType(
				Uri.fromFile(new File("/mnt/sdcard/doc/" + str_filename)),
				fileMimeType);
		try {
			DetailsActivity.this.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// 检测到系统尚未安装OliveOffice的apk程序
			Toast.makeText(DetailsActivity.this, "未找到软件", Toast.LENGTH_LONG)
					.show();
			// 请先到www.olivephone.com/e.apk下载并安装
		}
	}

	/**
	 * demoFile 模板文件 newFile 生成文件 map 要填充的数据
	 * */
	public void writeDoc(String demopath, String targetPath,
			Map<String, Object> map) {
		try {
			InputStream in = getClass().getResourceAsStream(
					"/assets/" + demopath);
			HWPFDocument hdt = new HWPFDocument(in);
			Range range = hdt.getRange();
			// 替换文本内容
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if ((entry.getValue()) instanceof String) {
					// 替换文本
					range.replaceText(entry.getKey(), entry.getValue()
							.toString());
				}
			}
			// 获取doc中的图片数
			List<Picture> pics = hdt.getPicturesTable().getAllPictures();
			System.out.printf(pics.size() + "\n");
			/*
			 * for (Picture pic : pics) { // 图片在doc文件中的位置,分析Doc 转化成其他文本时需要用到 int
			 * start = pic.getStartOffset(); int width = pic.getWidth(); int
			 * height = pic.getHeight(); String mimeType = pic.getMimeType();
			 * System.out.printf("开始位置%d\t图片大小度%d,高%d,\t图片类型%s\r\n", start,
			 * width, height, mimeType); }
			 */
			OutputStream os = new FileOutputStream(targetPath);
			// ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			// FileOutputStream out = new FileOutputStream(newFile, true);
			hdt.write(os);

			// 1.通过Picture的writeImageContent方法 写文件
			// 2.获取Picture的byte 自己写
			copyPic2Disk(pics, os);

			this.closeStream(os);
			this.closeStream(in);
			// 输出字节流
			// out.write(ostream.toByteArray());
			// out.close();
			// ostream.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过Picture 自己类中的读写方法
	 * 
	 * @param pics
	 * @param path
	 */
	public static void copyPic2Disk(List<Picture> pics, OutputStream os) {

		if (pics == null || pics.size() <= 0) {
			return;
		}
		/*
		 * if(!path.isDirectory()){ throw new RuntimeException("路径填写不正确"); }
		 * //当文件夹路径不存在的情况下，我们自己创建文件夹目录 if(!path.exists() ){ path.mkdirs(); }
		 */

		try {
			for (Picture pic : pics) {
				// 写出数据，我们使用的是Poi类中，Picture自己所带的函数
				pic.writeImageContent(os);
				/*
				 * byte [] picBytes = pic.getContent(); //获取字节流，也可以自己写入数据
				 * copyByteToFile(picBytes);
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭输入流
	 * 
	 * @param is
	 */
	private void closeStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @param os
	 */
	private void closeStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 打印----------------------------------------------------------------------------

	// 判断apk是否安装
	public static boolean appIsInstalled(Context context, String pageName) {
		try {
			context.getPackageManager().getPackageInfo(pageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}

	}

	// 把Asset下的apk拷贝到sdcard下 /Android/data/你的包名/cache 目录下
	public static File getAssetFileToCacheDir(Context context, String fileName) {
		try {
			File cacheDir = getCacheDir(context);
			final String cachePath = cacheDir.getAbsolutePath()
					+ File.separator + fileName;
			InputStream is = context.getAssets().open(fileName);
			File file = new File(cachePath);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];

			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取sdcard中的缓存目录
	public static File getCacheDir(Context context) {
		String APP_DIR_NAME = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Android/data/";
		File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	// --------------------------------------------------------------------------------

	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 * 
	 * @param context
	 *            Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}
}
