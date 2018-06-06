package com.hzlf.sampletest.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.activity.AddActivity;
import com.hzlf.sampletest.activity.CaptureActivity;
import com.hzlf.sampletest.activity.MainActivity;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.AAQI;
import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Info_add3;
import com.hzlf.sampletest.http.HttpUtils;
import com.hzlf.sampletest.http.NetworkUtil;
import com.hzlf.sampletest.others.DatePickerDialog;
import com.hzlf.sampletest.others.MyApplication;
import com.hzlf.sampletest.others.UsedPath;

public class fragment_add3 extends Fragment {

	private Spinner spinner_yangpinleixing, spinner_yangpinlaiyuan,
			spinner_yangpinshuxing, spinner_baozhuangfenlei,
			spinner_riqileixing, spinner_chouyangfangshi,
			spinner_yangpinxingtai, spinner_yangpinbaozhuang,
			spinner_chucuntiaojian;

	private List<String> data_yangpinleixing, data_yangpinlaiyuan,
			data_yangpinshuxing, data_baozhuangfenlei, data_riqileixing,
			data_chouyangfangshi, data_yangpinxingtai, data_yangpinbaozhuang,
			data_chucuntiaojian;

	private ArrayAdapter<String> adapter_yangpinleixing,
			adapter_yangpinlaiyuan, adapter_yangpinshuxing,
			adapter_baozhuangfenlei, adapter_riqileixing,
			adapter_chouyangfangshi, adapter_yangpinxingtai,
			adapter_yangpinbaozhuang, adapter_chucuntiaojian;

	private EditText textview1, textview2, textview3, textview4, textview5,
			textview6, textview7, textview8, textview9, textview10, textview11,
			textview12, textview13, textview14, textview15, textview16;

	private TextView text_shengchanriqi, text_chouyangriqi;
	private RadioGroup radio_chukou;
	private Button btn_back3, brn_save, btn_scanning;
	final int DATE_DIALOG = 1;
	private int mYear, mMonth, mDay;
	private String str_chukou, str_beizhu, str_shengchanriqi, str_chouyangriqi,
			number;
	private DBManage dbmanage;

	private Info_add info_add_now;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_layout3, container, false);
		dbmanage = new DBManage(getActivity());

		AddActivity addActivity = (AddActivity) getActivity();
		number = addActivity.getNumber();

		/*--------------------------------------------------------------------*/
		spinner_yangpinleixing = (Spinner) view
				.findViewById(R.id.spinner_yangpinleixing);
		data_yangpinleixing = new ArrayList<String>();
		data_yangpinleixing.add("食用农产品");
		data_yangpinleixing.add("工业加工食品");
		data_yangpinleixing.add("餐饮加工食品");
		data_yangpinleixing.add("食品添加剂");
		data_yangpinleixing.add("食品相关产品");
		data_yangpinleixing.add("其他");
		adapter_yangpinleixing = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_yangpinleixing); // 设置样式
		adapter_yangpinleixing
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配�?
		spinner_yangpinleixing.setAdapter(adapter_yangpinleixing);

		/*--------------------------------------------------------------------*/
		spinner_yangpinlaiyuan = (Spinner) view
				.findViewById(R.id.spinner_yangpinlaiyuan);
		data_yangpinlaiyuan = new ArrayList<String>();
		data_yangpinlaiyuan.add("加工/自制");
		data_yangpinlaiyuan.add("委托生产");
		data_yangpinlaiyuan.add("外购");
		data_yangpinlaiyuan.add("其他");
		adapter_yangpinlaiyuan = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_yangpinlaiyuan); // 设置样式
		adapter_yangpinlaiyuan
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配�?
		spinner_yangpinlaiyuan.setAdapter(adapter_yangpinlaiyuan);

		/*--------------------------------------------------------------------*/
		spinner_yangpinshuxing = (Spinner) view
				.findViewById(R.id.spinner_yangpinshuxing);
		data_yangpinshuxing = new ArrayList<String>();
		data_yangpinshuxing.add("普通食品");
		data_yangpinshuxing.add("特殊膳食食品");
		data_yangpinshuxing.add("节令食品");
		data_yangpinshuxing.add("重大活动保障食品");
		adapter_yangpinshuxing = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_yangpinshuxing); // 设置样式
		adapter_yangpinshuxing
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配�?
		spinner_yangpinshuxing.setAdapter(adapter_yangpinshuxing);

		/*--------------------------------------------------------------------*/
		spinner_baozhuangfenlei = (Spinner) view
				.findViewById(R.id.spinner_baozhuangfenlei);
		data_baozhuangfenlei = new ArrayList<String>();
		data_baozhuangfenlei.add("散装");
		data_baozhuangfenlei.add("预包装");
		adapter_baozhuangfenlei = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_baozhuangfenlei); // 设置样式
		adapter_baozhuangfenlei
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配�?
		spinner_baozhuangfenlei.setAdapter(adapter_baozhuangfenlei);

		/*--------------------------------------------------------------------*/

		spinner_riqileixing = (Spinner) view
				.findViewById(R.id.spinner_riqileixing);
		data_riqileixing = new ArrayList<String>();

		data_riqileixing.add("生产");
		data_riqileixing.add("加工");
		data_riqileixing.add("购进日期");

		adapter_riqileixing = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_riqileixing); // 设置样式
		adapter_riqileixing
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配�?
		spinner_riqileixing.setAdapter(adapter_riqileixing);

		/*--------------------------------------------------------------------*/
		spinner_chouyangfangshi = (Spinner) view
				.findViewById(R.id.spinner_chouyangfangshi);
		data_chouyangfangshi = new ArrayList<String>();
		data_chouyangfangshi.add("非无菌抽样");
		data_chouyangfangshi.add("无菌抽样");
		adapter_chouyangfangshi = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_chouyangfangshi); // 设置样式
		adapter_chouyangfangshi
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_chouyangfangshi.setAdapter(adapter_chouyangfangshi);

		/*--------------------------------------------------------------------*/
		spinner_yangpinxingtai = (Spinner) view
				.findViewById(R.id.spinner_yangpinxingtai);
		data_yangpinxingtai = new ArrayList<String>();
		data_yangpinxingtai.add("固体");
		data_yangpinxingtai.add("半固体");
		data_yangpinxingtai.add("液体");
		data_yangpinxingtai.add("气体");
		adapter_yangpinxingtai = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_yangpinxingtai); // 设置样式
		adapter_yangpinxingtai
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_yangpinxingtai.setAdapter(adapter_yangpinxingtai);

		/*--------------------------------------------------------------------*/
		spinner_yangpinbaozhuang = (Spinner) view
				.findViewById(R.id.spinner_yangpinbaozhuang);
		data_yangpinbaozhuang = new ArrayList<String>();
		data_yangpinbaozhuang.add("玻璃瓶");
		data_yangpinbaozhuang.add("塑料瓶");
		data_yangpinbaozhuang.add("塑料袋");
		data_yangpinbaozhuang.add("无菌袋");
		data_yangpinbaozhuang.add("其他");
		adapter_yangpinbaozhuang = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_yangpinbaozhuang); // 设置样式
		adapter_yangpinbaozhuang
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_yangpinbaozhuang.setAdapter(adapter_yangpinbaozhuang);

		/*--------------------------------------------------------------------*/
		spinner_chucuntiaojian = (Spinner) view
				.findViewById(R.id.spinner_chucuntiaojian);
		data_chucuntiaojian = new ArrayList<String>();
		data_chucuntiaojian.add("常温");
		data_chucuntiaojian.add("冷藏");
		data_chucuntiaojian.add("冷冻");
		data_chucuntiaojian.add("避光");
		data_chucuntiaojian.add("密闭");
		data_chucuntiaojian.add("其他");
		adapter_chucuntiaojian = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_chucuntiaojian); // 设置样式
		adapter_chucuntiaojian
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_chucuntiaojian.setAdapter(adapter_chucuntiaojian);

		textview1 = (EditText) view.findViewById(R.id.input_yangpinmingcheng);
		textview2 = (EditText) view.findViewById(R.id.input_yangpinshangbiao);
		textview3 = (EditText) view.findViewById(R.id.input_guigexinghao);
		textview4 = (EditText) view.findViewById(R.id.input_zhiliangdengji);
		textview5 = (EditText) view.findViewById(R.id.input_yangpintiaoma);
		textview6 = (EditText) view.findViewById(R.id.input_baozhiqi);
		textview7 = (EditText) view.findViewById(R.id.input_chanpinpihao);
		textview8 = (EditText) view.findViewById(R.id.input_yangpindanjia);
		textview9 = (EditText) view.findViewById(R.id.input_yuanchandi);

		textview10 = (EditText) view.findViewById(R.id.input_chouyangren);

		textview11 = (EditText) view.findViewById(R.id.input_zhixingbiaozhun);
		textview12 = (EditText) view
				.findViewById(R.id.input_chouyangshuliangdanwei);
		textview13 = (EditText) view.findViewById(R.id.input_chouyangjishu);
		textview14 = (EditText) view.findViewById(R.id.input_beiyangshuliang);
		textview15 = (EditText) view.findViewById(R.id.input_chouyangshuliang);
		textview16 = (EditText) view.findViewById(R.id.input_beizhu);

		radio_chukou = (RadioGroup) view.findViewById(R.id.radio_chukou);

		Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);

		str_chouyangriqi = str_shengchanriqi = new SimpleDateFormat(
				"yyyy-MM-dd").format(Calendar.getInstance().getTime());

		text_shengchanriqi = (TextView) view
				.findViewById(R.id.text_shengchanriqi);
		text_shengchanriqi.setText(str_shengchanriqi);
		text_shengchanriqi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存�?
				new DatePickerDialog(getActivity(), 0,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker DatePicker,
									int Year, int MonthOfYear, int DayOfMonth) {
								str_shengchanriqi = Year
										+ "-"
										+ String.format("%02d",
												(MonthOfYear + 1)) + "-"
										+ String.format("%02d", DayOfMonth);
								text_shengchanriqi.setText(str_shengchanriqi);
							}
						}, mYear, mMonth, mDay, true).show();
			}
		});

		text_chouyangriqi = (TextView) view
				.findViewById(R.id.text_chouyangriqi);
		text_chouyangriqi.setText(str_chouyangriqi);
		text_chouyangriqi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存�?
				new DatePickerDialog(getActivity(), 0,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker DatePicker,
									int Year, int MonthOfYear, int DayOfMonth) {
								str_chouyangriqi = Year
										+ "-"
										+ String.format("%02d",
												(MonthOfYear + 1)) + "-"
										+ String.format("%02d", DayOfMonth);
								text_chouyangriqi.setText(str_chouyangriqi);
							}
						}, mYear, mMonth, mDay, true).show();
			}
		});

		btn_scanning = (Button) view.findViewById(R.id.btn_scanning);
		btn_scanning.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存�?
				Intent intent_scan = new Intent(getActivity(),
						CaptureActivity.class);
				startActivityForResult(intent_scan, 3);
			}
		});

		brn_save = (Button) view.findViewById(R.id.btn_save);
		brn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存�?

				if (textview1.getText().toString().equals("")
						|| textview2.getText().toString().equals("")
						|| textview3.getText().toString().equals("")
						|| textview4.getText().toString().equals("")
						|| textview8.getText().toString().equals("")
						|| textview10.getText().toString().equals("")
						|| textview11.getText().toString().equals("")
						|| textview12.getText().toString().equals("")
						|| textview13.getText().toString().equals("")
						|| textview14.getText().toString().equals("")
						|| textview15.getText().toString().equals("")) {

					((MyApplication) getActivity().getApplication()).setAdd3(0);
					Toast.makeText(getActivity(), "带*的为必填项", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (textview5.getText().toString().equals("")) {
						textview5.setText("/");
					}
					if (textview7.getText().toString().equals("")) {
						textview7.setText("/");
					}
					if (textview9.getText().toString().equals("")) {
						textview9.setText("/");
					}

					if (textview16.getText().toString().equals("")) {
						str_beizhu = "/";
					} else {
						str_beizhu = textview16.getText().toString();
					}
					for (int i = 0; i < radio_chukou.getChildCount(); i++) {
						RadioButton r = (RadioButton) radio_chukou
								.getChildAt(i);
						if (r.isChecked()) {
							str_chukou = r.getText().toString();
							break;
						}
					}

					Info_add3 info_add3 = new Info_add3(
							textview1.getText().toString(),
							spinner_yangpinleixing.getSelectedItem().toString(),
							spinner_yangpinlaiyuan.getSelectedItem().toString(),
							spinner_yangpinshuxing.getSelectedItem().toString(),
							textview2.getText().toString(),
							spinner_baozhuangfenlei.getSelectedItem()
									.toString(),
							textview3.getText().toString(),
							textview4.getText().toString(),
							textview5.getText().toString(),
							str_shengchanriqi,
							textview6.getText().toString(),
							textview7.getText().toString(),
							textview8.getText().toString(),
							str_chukou,
							textview9.getText().toString(),
							/* spinner_yuanchandi.getSelectedItem().toString(), */
							str_chouyangriqi,
							spinner_chouyangfangshi.getSelectedItem()
									.toString(),
							spinner_yangpinxingtai.getSelectedItem().toString(),
							spinner_yangpinbaozhuang.getSelectedItem()
									.toString(), spinner_chucuntiaojian
									.getSelectedItem().toString(), textview10
									.getText().toString(), textview11.getText()
									.toString(), textview12.getText()
									.toString(), textview13.getText()
									.toString(), textview14.getText()
									.toString(), textview15.getText()
									.toString(), str_beizhu,
							spinner_riqileixing.getSelectedItem().toString());
					((MyApplication) getActivity().getApplication()).setAdd3(1);
					((MyApplication) getActivity().getApplication())
							.setInfoAdd3(info_add3);
					if (((MyApplication) getActivity().getApplication())
							.getAdd1() == 1
							&& ((MyApplication) getActivity().getApplication())
									.getAdd2() == 1
							&& ((MyApplication) getActivity().getApplication())
									.getAdd3() == 1) {
						dbmanage.addInfo(
								((MyApplication) getActivity().getApplication())
										.getNo(),
								((MyApplication) getActivity().getApplication())
										.getInfoAdd1(),
								((MyApplication) getActivity().getApplication())
										.getInfoAdd2(),
								((MyApplication) getActivity().getApplication())
										.getInfoAdd3());

						dbmanage.updateNumber(number, 1, 0, 0);
						dbmanage.updateSign(number, 0);

						Toast.makeText(getActivity(), "保存成功!",
								Toast.LENGTH_SHORT).show();
						info_add_now = new Info_add();
						info_add_now
								.setInfo_add1(((MyApplication) getActivity()
										.getApplication()).getInfoAdd1());
						info_add_now
								.setInfo_add2(((MyApplication) getActivity()
										.getApplication()).getInfoAdd2());
						info_add_now
								.setInfo_add3(((MyApplication) getActivity()
										.getApplication()).getInfoAdd3());

						Intent intent_main = new Intent();
						intent_main.setClass(getActivity(), MainActivity.class);
						getActivity().finish();// 结束当前活动
						startActivity(intent_main);

					} else {
						Toast.makeText(getActivity(), "带*的为必填项",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		btn_back3 = (Button) view.findViewById(R.id.btn_back3);
		btn_back3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存�?
				AddActivity backFragment = (AddActivity) getActivity();
				backFragment.backFragment();
			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data); // 这个super可不能落下，否则可能回调不了

		if (requestCode == 3) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				textview5.setText(scanResult);

				if (NetworkUtil.checkedNetWork(getActivity())) {
					new Thread() {
						@Override
						public void run() {
							String result = HttpUtils
									.getAAQI(UsedPath.api_AAQI
											+ textview5.getText().toString());
							if (result.equals("获取数据失败") || result.equals("")) {

								Toast.makeText(getActivity(), "获取商品信息失败",
										Toast.LENGTH_SHORT).show();
							} else if (result.equals("1")) {
								Toast.makeText(getActivity(), "当前网络不可用",
										Toast.LENGTH_SHORT).show();

							} else {
								Gson gson = new Gson();
								final AAQI aaqi = gson.fromJson(result,
										AAQI.class);
								if (aaqi.getStatus().equals("success")) {
									textview1.post(new Runnable() {

										@Override
										public void run() {
											textview1.setText(aaqi.getMessage()
													.getGOODS_NAME());
										}
									});

									textview2.post(new Runnable() {

										@Override
										public void run() {
											textview2.setText(aaqi.getMessage()
													.getTRADEMARK());
										}
									});

									textview3.post(new Runnable() {

										@Override
										public void run() {
											textview3.setText(aaqi.getMessage()
													.getSAMPLE_MODEL());
										}
									});

								} else {
									Toast.makeText(getActivity(), "条码扫描错误，请重试",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					}.start();
				} else {
					Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}
}
