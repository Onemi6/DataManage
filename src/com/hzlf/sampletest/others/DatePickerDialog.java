package com.hzlf.sampletest.others;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.hzlf.sampletest.R;

public class DatePickerDialog extends AlertDialog implements OnClickListener,
		OnDateChangedListener {

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private final DatePicker mDatePicker;
	private final OnDateSetListener mCallBack;

	public interface OnDateSetListener {

		void onDateSet(DatePicker DatePicker, int Year, int MonthOfYear,
				int DayOfMonth);
	}

	public DatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}

	public DatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth, true);
	}

	public DatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth, boolean isDayVisible) {
		super(context, theme);

		mCallBack = callBack;

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, "确 定", this);
		setButton(BUTTON_NEGATIVE, "取 消", this);
		// setButton(BUTTON_POSITIVE,
		// themeContext.getText(android.R.string.date_time_done), this);
		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker, null);
		setView(view);
		mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
		// updateTitle(year, monthOfYear, dayOfMonth);

		// 如果要隐藏当前日期，则使用下面方法。
		if (!isDayVisible) {
			hidDay(mDatePicker);
		}
	}

	private void hidDay(DatePicker mDatePicker) {
		Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
		for (Field datePickerField : datePickerfFields) {
			if ("mDaySpinner".equals(datePickerField.getName())) {
				datePickerField.setAccessible(true);
				Object dayPicker = new Object();
				try {
					dayPicker = datePickerField.get(mDatePicker);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				// datePicker.getCalendarView().setVisibility(View.GONE);
				((View) dayPicker).setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// Log.d(this.getClass().getSimpleName(), String.format("which:%d",
		// which));
		// 如果是“取 消”按钮，则返回，如果是“确 定”按钮，则往下执行
		if (which == BUTTON_POSITIVE)
			tryNotifyDateSet();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		if (view.getId() == R.id.datePicker)
			mDatePicker.init(year, month, day, this);
	}

	/**
	 * 178 * 获得开始日期的DatePicker 179 * 180 * @return The calendar view. 181
	 */
	public DatePicker getDatePicker() {
		return mDatePicker;
	}

	public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	private void tryNotifyDateSet() {
		if (mCallBack != null) {
			mDatePicker.clearFocus();
			mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
					mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
		}
	}

	@Override
	protected void onStop() {
		// tryNotifyDateSet();
		super.onStop();
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());

		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);

	}
}
