package com.hzlf.sampletest.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzlf.sampletest.R;
import com.hzlf.sampletest.activity.AddActivity;
import com.hzlf.sampletest.db.DBManage;
import com.hzlf.sampletest.entityclass.Info_add;
import com.hzlf.sampletest.entityclass.Info_add1;
import com.hzlf.sampletest.others.MyApplication;

public class fragment_add1 extends Fragment {

	private Spinner spinner_renwuleibie;
	private List<String> data_renwuleibie;
	private ArrayAdapter<String> adapter_renwuleibie, adapter_renwulaiyuan;

	private Button btn_next1;

	private AutoCompleteTextView edittext1;
	private String[] renwulaiyuan;

	// private EditText edittext1;

	private TextView textview1, textview3, textview4, textview5, textview6,
			textview7, textview8;
	private String str_chuanzhen, str_youbian, number;

	private DBManage dbmanage;

	private AddActivity addActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		addActivity = (AddActivity) getActivity();
		number = addActivity.getNumber();
		View view = inflater.inflate(R.layout.add_layout1, container, false);
		dbmanage = new DBManage(getActivity());
		spinner_renwuleibie = (Spinner) view
				.findViewById(R.id.spinner_renwuleibie);
		// 数据
		data_renwuleibie = new ArrayList<String>();
		data_renwuleibie.add("监督抽检");
		data_renwuleibie.add("风险监测");
		// 适配器
		adapter_renwuleibie = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data_renwuleibie); // 设置样式
		adapter_renwuleibie
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 加载适配器
		spinner_renwuleibie.setAdapter(adapter_renwuleibie);

		textview1 = (TextView) view.findViewById(R.id.text_chouyangdanbianhao);
		edittext1 = (AutoCompleteTextView) view
				.findViewById(R.id.text_renwulaiyuan);
		renwulaiyuan = dbmanage.findList_TaskSource().toArray(
				new String[dbmanage.findList_TaskSource().size()]);
		/*
		 * 需要一个适配器 初始化数据源--这个数据源去匹配文本框中输入的内容
		 */
		adapter_renwulaiyuan = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, renwulaiyuan);
		// 将adpter与当前AutoCompleteTextView绑定
		edittext1.setAdapter(adapter_renwulaiyuan);

		textview3 = (TextView) view.findViewById(R.id.text_danweimingcheng);
		textview4 = (TextView) view.findViewById(R.id.text_danweidizhi);
		textview5 = (TextView) view.findViewById(R.id.text_lianxiren);
		textview6 = (TextView) view.findViewById(R.id.text_dianhua);
		textview7 = (TextView) view.findViewById(R.id.text_chuanzhen);
		textview8 = (TextView) view.findViewById(R.id.text_youbian);

		textview1.setText(number);

		initdata();

		btn_next1 = (Button) view.findViewById(R.id.btn_next1);
		btn_next1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (edittext1.getText().toString().equals("")) {
					((MyApplication) addActivity.getApplication()).setAdd1(0);
				} else {
					((MyApplication) addActivity.getApplication()).setAdd1(1);
				}
				if (textview7.getText().toString().equals("")) {
					str_chuanzhen = "/";
				} else {
					str_chuanzhen = textview7.getText().toString();
				}
				if (textview8.getText().toString().equals("")) {
					str_youbian = "/";
				} else {
					str_youbian = textview8.getText().toString();
				}
				Info_add1 info_add1 = new Info_add1(textview1.getText()
						.toString(), edittext1.getText().toString(),
						spinner_renwuleibie.getSelectedItem().toString(),
						textview3.getText().toString(), textview4.getText()
								.toString(), textview5.getText().toString(),
						textview6.getText().toString(), str_chuanzhen,
						str_youbian);
				((MyApplication) addActivity.getApplication())
						.setInfoAdd1(info_add1);
				addActivity.nextFragment();
			}
		});
		return view;
	}

	public void initdata() {
		String info_add_str = addActivity.load();
		Gson gson = new Gson();
		Info_add info = gson.fromJson(info_add_str, Info_add.class);
		if (info != null && info.getInfo_add1() != null) {
			edittext1.setText(info.getInfo_add1().getValue2());
			SpinnerAdapter adapter = spinner_renwuleibie.getAdapter();
			for (int i = 0; i < adapter.getCount(); i++) {
				if (info.getInfo_add1().getValue3()
						.equals(adapter.getItem(i).toString())) {
					spinner_renwuleibie.setSelection(i, true);
					break;
				}
			}
		}
	}

	@Override
	public void onPause() {
		Info_add1 info_add1 = new Info_add1(textview1.getText().toString(),
				edittext1.getText().toString(), spinner_renwuleibie
						.getSelectedItem().toString(), textview3.getText()
						.toString(), textview4.getText().toString(), textview5
						.getText().toString(), textview6.getText().toString(),
				str_chuanzhen, str_youbian);
		((MyApplication) addActivity.getApplication()).setInfoAdd1(info_add1);
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
		Info_add1 info_add1 = new Info_add1(textview1.getText().toString(),
				edittext1.getText().toString(), spinner_renwuleibie
						.getSelectedItem().toString(), textview3.getText()
						.toString(), textview4.getText().toString(), textview5
						.getText().toString(), textview6.getText().toString(),
				str_chuanzhen, str_youbian);
		((MyApplication) getActivity().getApplication()).setInfoAdd1(info_add1);
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
}
