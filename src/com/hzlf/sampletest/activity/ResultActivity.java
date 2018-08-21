package com.hzlf.sampletest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.hzlf.sampletest.R;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);

		TextView result = (TextView) findViewById(R.id.text_result);
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			// String title =
			// bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//appname
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			// result.setText(result.getText().toString()+"\n"+content);
			result.setText(content);
		}
	}
}
