package com.hzlf.sampletest.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.activity.AddActivity;
import com.hzlf.sampletest.activity.CaptureActivity;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Info_add2;
import com.hzlf.sampletest.others.MyApplication;

public class fragment_add2 extends Fragment implements OnClickListener {

	private Spinner spinner_suozaidi, spinner_chouyangdidian,
			spinner_quyuleixing, spinner_chouyanghuanjie;

	private List<String> data_suozaidi, data_quyuleixing, data_chouyanghuanjie;

	private String[][] data_chouyangdidian;

	private ArrayAdapter<String> adapter_danweimingcheng, adapter_suozaidi,
			adapter_chouyangdidian, adapter_quyuleixing,
			adapter_chouyanghuanjie;

	private Button btn_back2, btn_next2, btn_scanning_info, btn_queren;

	private String[] danweimingcheng;

	private AutoCompleteTextView textview1;
	private EditText textview2, textview3, textview4, textview5, textview6,
			textview7, textview8, textview9, textview10, textview11,
			textview12, textview13;

	private RadioGroup radio_xukezheng;

	String str_xukezheng, str_nianxiaoshoue, str_chuanzhen, str_youbian;
	int number;

	private AddActivity addActivity;

	private DBManage dbmanage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		addActivity = (AddActivity) getActivity();
		View view = inflater.inflate(R.layout.add_layout2, container, false);
		dbmanage = new DBManage(getActivity());
		number = 0;

		/*------------------------------------------------------------------------------*/
		spinner_suozaidi = (Spinner) view.findViewById(R.id.spinner_suozaidi);
		data_suozaidi = new ArrayList<String>();
		data_suozaidi.add("浙江宁波海曙区");
		data_suozaidi.add("浙江宁波江东区");
		data_suozaidi.add("浙江宁波江北区");
		data_suozaidi.add("浙江宁波鄞州区");
		data_suozaidi.add("浙江宁波镇海区");
		data_suozaidi.add("浙江宁波北仑区");
		data_suozaidi.add("浙江宁波宁海县");
		data_suozaidi.add("浙江宁波象山县");
		data_suozaidi.add("浙江宁波慈溪市");
		data_suozaidi.add("浙江宁波余姚市");
		data_suozaidi.add("浙江宁波奉化市");

		adapter_suozaidi = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_suozaidi); // 设置样式
		adapter_suozaidi
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_suozaidi.setAdapter(adapter_suozaidi);

		/*------------------------------------------------------------------------------*/
		spinner_chouyangdidian = (Spinner) view
				.findViewById(R.id.spinner_chouyangdidian);

		// 生产环节 // 流通环节 // 餐饮环节
		data_chouyangdidian = new String[][] {
				{ "原辅料库", "生产线", "半成品库", "成品库(待检区)", "成品库(已检区)" },
				{ "农贸市场", "菜市场", "批发市场", "商场", "超市", "网购", "小杂食店", "其他" },
				{ "餐馆(特大型餐馆)", "餐馆(大型餐馆)", "餐馆(中型餐馆)", "餐馆(小型餐馆)", "食堂(机关食堂)",
						"食堂(学校/托幼食堂)", "食堂(企事业单位食堂)", "食堂(建筑工地食堂)", "小吃店",
						"快餐店", "饮品店", "集体用餐配送单位", "中央厨房", "其他" } };

		adapter_chouyangdidian = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_chouyangdidian[0]); // 设置样式
		adapter_chouyangdidian
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		// adapter_chouyangdidian.notifyDataSetChanged();
		spinner_chouyangdidian.setAdapter(adapter_chouyangdidian);

		/*------------------------------------------------------------------------------*/
		spinner_quyuleixing = (Spinner) view
				.findViewById(R.id.spinner_quyuleixing);
		data_quyuleixing = new ArrayList<String>();
		data_quyuleixing.add("城市");
		data_quyuleixing.add("乡村");
		data_quyuleixing.add("景点");
		data_quyuleixing.add("其他");
		adapter_quyuleixing = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_quyuleixing); // 设置样式
		adapter_quyuleixing
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_quyuleixing.setAdapter(adapter_quyuleixing);

		/*------------------------------------------------------------------------------*/
		spinner_chouyanghuanjie = (Spinner) view
				.findViewById(R.id.spinner_chouyanghuanjie);
		data_chouyanghuanjie = new ArrayList<String>();
		data_chouyanghuanjie.add("生产环节");
		data_chouyanghuanjie.add("流通环节");
		data_chouyanghuanjie.add("餐饮环节");
		adapter_chouyanghuanjie = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_chouyanghuanjie); // 设置样式
		adapter_chouyanghuanjie
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_chouyanghuanjie.setAdapter(adapter_chouyanghuanjie);

		spinner_chouyanghuanjie
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO 自动生成的方法存根

						// 使用ArrayAdapter转换数据
						adapter_chouyangdidian = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_spinner_item,
								data_chouyangdidian[position]);
						adapter_chouyangdidian
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
						// 使用adapter_chouyangdidian数据适配器填充spinner_chouyangdidian
						spinner_chouyangdidian
								.setAdapter(adapter_chouyangdidian);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO 自动生成的方法存根
					}
				});

		textview1 = (AutoCompleteTextView) view
				.findViewById(R.id.input_danweimingcheng);

		danweimingcheng = dbmanage.findList_Info_add2().toArray(
				new String[dbmanage.findList_Info_add2().size()]);

		/*
		 * 需要一个适配器 初始化数据源--这个数据源去匹配文本框中输入的内容
		 */
		adapter_danweimingcheng = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, danweimingcheng);

		// 将adpter与当前AutoCompleteTextView绑定
		textview1.setAdapter(adapter_danweimingcheng);

		textview2 = (EditText) view.findViewById(R.id.input_danweidizhi);
		textview3 = (EditText) view.findViewById(R.id.input_yingyezhizhao);
		textview4 = (EditText) view.findViewById(R.id.input_xukezhenghao);
		textview5 = (EditText) view.findViewById(R.id.input_danweifaren);
		textview6 = (EditText) view.findViewById(R.id.input_nianxiaoshoue);
		textview7 = (EditText) view.findViewById(R.id.input_lianxiren);
		textview8 = (EditText) view.findViewById(R.id.input_dianhua);
		textview9 = (EditText) view.findViewById(R.id.input_chuanzhen);
		textview10 = (EditText) view.findViewById(R.id.input_youbian);

		textview11 = (EditText) view
				.findViewById(R.id.input_shengchanzhemingcheng);
		textview12 = (EditText) view.findViewById(R.id.input_shengchanzhedizhi);
		textview13 = (EditText) view.findViewById(R.id.input_dianhua2);

		radio_xukezheng = (RadioGroup) view.findViewById(R.id.radio_xukezheng);
		for (int i = 0; i < radio_xukezheng.getChildCount(); i++) {
			RadioButton r = (RadioButton) radio_xukezheng.getChildAt(i);
			if (r.isChecked()) {
				str_xukezheng = r.getText().toString();
				break;
			}
		}

		btn_scanning_info = (Button) view.findViewById(R.id.btn_scanning_info);
		btn_scanning_info.setOnClickListener(this);

		btn_back2 = (Button) view.findViewById(R.id.btn_back2);
		btn_back2.setOnClickListener(this);

		btn_next2 = (Button) view.findViewById(R.id.btn_next2);
		btn_next2.setOnClickListener(this);

		btn_queren = (Button) view.findViewById(R.id.btn_queren);
		btn_queren.setOnClickListener(this);
		initdata();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scanning_info:
			Intent intent_scan = new Intent(addActivity,
					CaptureActivity.class);
			startActivityForResult(intent_scan, 2);
			break;
		case R.id.btn_next2:
			if (textview1.getText().toString().equals("")
					|| textview2.getText().toString().equals("")
					|| textview3.getText().toString().equals("")
					|| textview4.getText().toString().equals("")
					|| textview5.getText().toString().equals("")
					|| textview7.getText().toString().equals("")
					|| textview8.getText().toString().equals("")
					|| textview11.getText().toString().equals("")
					|| textview12.getText().toString().equals("")
					|| textview13.getText().toString().equals("")) {
				((MyApplication) getActivity().getApplication()).setAdd2(0);
			} else {
				if (textview6.getText().toString().equals("")) {
					str_nianxiaoshoue = "/";
				} else {
					str_nianxiaoshoue = textview6.getText().toString();
				}
				if (textview9.getText().toString().equals("")) {
					str_chuanzhen = "/";
				} else {
					str_chuanzhen = textview9.getText().toString();
				}
				if (textview10.getText().toString().equals("")) {
					str_youbian = "/";
				} else {
					str_youbian = textview10.getText().toString();
				}
				Info_add2 info_add2 = new Info_add2(spinner_suozaidi
						.getSelectedItem().toString(), spinner_chouyangdidian
						.getSelectedItem().toString(), spinner_quyuleixing
						.getSelectedItem().toString(), spinner_chouyanghuanjie
						.getSelectedItem().toString(), textview1.getText()
						.toString(), textview2.getText().toString(), textview3
						.getText().toString(), str_xukezheng, textview4
						.getText().toString(), textview5.getText().toString(),
						str_nianxiaoshoue, textview7.getText().toString(),
						textview8.getText().toString(), str_chuanzhen,
						str_youbian, textview11.getText().toString(),
						textview12.getText().toString(), textview13.getText()
								.toString());
				((MyApplication) getActivity().getApplication())
						.setInfoAdd2(info_add2);
				((MyApplication) getActivity().getApplication()).setAdd2(1);
			}
			AddActivity addActivity = (AddActivity) getActivity();
			addActivity.nextFragment();
			break;
		case R.id.btn_back2:
			AddActivity backFragment = (AddActivity) getActivity();
			backFragment.backFragment();
			break;
		case R.id.btn_queren:

			if (textview1.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "单位名称未填写", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (dbmanage.findInfo_add2(textview1.getText().toString()) == null) {
					Toast.makeText(getActivity(), "无填写记录", Toast.LENGTH_SHORT)
							.show();
				} else {
					Info_add2 info_add2 = dbmanage.findInfo_add2(textview1
							.getText().toString());

					// spinner_suozaidi .getSelectedItem().toString();
					// spinner_suozaidi.setSelection(2,true);

					textview2.setText(info_add2.getValue6());
					textview3.setText(info_add2.getValue7());

					if (info_add2.getValue8().equals("流通许可证")) {
						radio_xukezheng.check(R.id.radio_xukezheng_liutong);
					} else if (info_add2.getValue8().equals("餐饮服务许可证")) {
						radio_xukezheng.check(R.id.radio_xukezheng_canyinfuwu);
					} else if (info_add2.getValue8().equals("食品经营许可证")) {
						radio_xukezheng
								.check(R.id.radio_xukezheng_shipinjingying);
					} else if (info_add2.getValue8().equals("食品生产许可证")) {
						radio_xukezheng
								.check(R.id.radio_xukezheng_shipinshengchan);
					}
					textview4.setText(info_add2.getValue9());
					textview5.setText(info_add2.getValue10());
					textview6.setText(info_add2.getValue11());
					textview7.setText(info_add2.getValue12());
					textview8.setText(info_add2.getValue13());
					textview9.setText(info_add2.getValue14());
					textview10.setText(info_add2.getValue15());
					textview11.setText(info_add2.getValue16());
					textview12.setText(info_add2.getValue17());
					textview13.setText(info_add2.getValue18());
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data); // 这个super可不能落下，否则可能回调不了

		if (requestCode == 2) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
				Matcher isNum = pattern.matcher(scanResult);
				if (!isNum.matches()) {
					String[] info = scanResult.split("\r\n");
					for (String oneinfo : info) {

						if (oneinfo.indexOf("企业名称") != -1) {
							textview1.setText(oneinfo.substring(5));
						} else if (oneinfo.indexOf("注册号") != -1) {
							textview3.setText(oneinfo.substring(4));
						} else if (oneinfo.indexOf("住所") != -1) {
							textview2.setText(oneinfo.substring(3));
						} else if (oneinfo.indexOf("法定代表人") != -1) {
							textview5.setText(oneinfo.substring(6));
						}
					}
				} else {
					Toast.makeText(getActivity(), "扫描失败,重新扫描",
							Toast.LENGTH_SHORT).show();
					Intent intent_scan = new Intent(getActivity(),
							CaptureActivity.class);
					startActivityForResult(intent_scan, 2);
				}
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
		} else {

		}
	}

	@Override
	public void onPause() {
		for (int i = 0; i < radio_xukezheng.getChildCount(); i++) {
			RadioButton r = (RadioButton) radio_xukezheng.getChildAt(i);
			if (r.isChecked()) {
				str_xukezheng = r.getText().toString();
				break;
			}
		}
		Info_add2 info_add2 = new Info_add2(spinner_suozaidi.getSelectedItem()
				.toString(), spinner_chouyangdidian.getSelectedItem()
				.toString(), spinner_quyuleixing.getSelectedItem().toString(),
				spinner_chouyanghuanjie.getSelectedItem().toString(), textview1
						.getText().toString(), textview2.getText().toString(),
				textview3.getText().toString(), str_xukezheng, textview4
						.getText().toString(), textview5.getText().toString(),
				str_nianxiaoshoue, textview7.getText().toString(), textview8
						.getText().toString(), str_chuanzhen, str_youbian,
				textview11.getText().toString(), textview12.getText()
						.toString(), textview13.getText().toString());
		((MyApplication) getActivity().getApplication()).setInfoAdd2(info_add2);
		Info_add info = new Info_add();
		info.setInfo_add1(((MyApplication) addActivity.getApplication())
				.getInfoAdd1());
		info.setInfo_add2(((MyApplication) addActivity.getApplication())
				.getInfoAdd2());
		info.setInfo_add3(((MyApplication) addActivity.getApplication())
				.getInfoAdd3());
		addActivity.save(info);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		for (int i = 0; i < radio_xukezheng.getChildCount(); i++) {
			RadioButton r = (RadioButton) radio_xukezheng.getChildAt(i);
			if (r.isChecked()) {
				str_xukezheng = r.getText().toString();
				break;
			}
		}
		Info_add2 info_add2 = new Info_add2(spinner_suozaidi.getSelectedItem()
				.toString(), spinner_chouyangdidian.getSelectedItem()
				.toString(), spinner_quyuleixing.getSelectedItem().toString(),
				spinner_chouyanghuanjie.getSelectedItem().toString(), textview1
						.getText().toString(), textview2.getText().toString(),
				textview3.getText().toString(), str_xukezheng, textview4
						.getText().toString(), textview5.getText().toString(),
				str_nianxiaoshoue, textview7.getText().toString(), textview8
						.getText().toString(), str_chuanzhen, str_youbian,
				textview11.getText().toString(), textview12.getText()
						.toString(), textview13.getText().toString());
		((MyApplication) getActivity().getApplication()).setInfoAdd2(info_add2);
		Info_add info = new Info_add();
		info.setInfo_add1(((MyApplication) addActivity.getApplication())
				.getInfoAdd1());
		info.setInfo_add2(((MyApplication) addActivity.getApplication())
				.getInfoAdd2());
		info.setInfo_add3(((MyApplication) addActivity.getApplication())
				.getInfoAdd3());
		addActivity.save(info);
		super.onDestroy();
	}

	public void initdata() {
		String info_add_str = addActivity.load();
		Gson gson = new Gson();
		Info_add info = gson.fromJson(info_add_str, Info_add.class);
		if (info != null && info.getInfo_add2() != null) {
			SpinnerAdapter adapter1 = spinner_suozaidi.getAdapter();
			for (int i = 0; i < adapter1.getCount(); i++) {
				if (info.getInfo_add2().getValue1()
						.equals(adapter1.getItem(i).toString())) {
					spinner_suozaidi.setSelection(i, true);
					break;
				}
			}
			SpinnerAdapter adapter2 = spinner_chouyangdidian.getAdapter();
			for (int i = 0; i < adapter2.getCount(); i++) {
				if (info.getInfo_add2().getValue2()
						.equals(adapter2.getItem(i).toString())) {
					spinner_chouyangdidian.setSelection(i, true);
					break;
				}
			}
			SpinnerAdapter adapter3 = spinner_quyuleixing.getAdapter();
			for (int i = 0; i < adapter3.getCount(); i++) {
				if (info.getInfo_add2().getValue3()
						.equals(adapter3.getItem(i).toString())) {
					spinner_quyuleixing.setSelection(i, true);
					break;
				}
			}
			SpinnerAdapter adapter4 = spinner_chouyanghuanjie.getAdapter();
			for (int i = 0; i < adapter4.getCount(); i++) {
				if (info.getInfo_add2().getValue4()
						.equals(adapter4.getItem(i).toString())) {
					spinner_chouyanghuanjie.setSelection(i, true);
					break;
				}
			}

			textview1.setText(info.getInfo_add2().getValue5());
			textview2.setText(info.getInfo_add2().getValue6());
			textview3.setText(info.getInfo_add2().getValue7());
			if (info.getInfo_add2().getValue8().equals("流通许可证")) {
				radio_xukezheng.check(R.id.radio_xukezheng_liutong);
			} else if (info.getInfo_add2().getValue8().equals("餐饮服务许可证")) {
				radio_xukezheng.check(R.id.radio_xukezheng_canyinfuwu);
			} else if (info.getInfo_add2().getValue8().equals("食品经营许可证")) {
				radio_xukezheng.check(R.id.radio_xukezheng_shipinjingying);
			} else if (info.getInfo_add2().getValue8().equals("食品生产许可证")) {
				radio_xukezheng.check(R.id.radio_xukezheng_shipinshengchan);
			}
			textview4.setText(info.getInfo_add2().getValue9());
			textview5.setText(info.getInfo_add2().getValue10());
			textview6.setText(info.getInfo_add2().getValue11());
			textview7.setText(info.getInfo_add2().getValue12());
			textview8.setText(info.getInfo_add2().getValue13());
			textview9.setText(info.getInfo_add2().getValue14());
			textview10.setText(info.getInfo_add2().getValue15());
			textview11.setText(info.getInfo_add2().getValue16());
			textview12.setText(info.getInfo_add2().getValue17());
			textview13.setText(info.getInfo_add2().getValue18());
		}
	}
}
