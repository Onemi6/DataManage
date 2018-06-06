package com.hzlf.sampletest.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * 网络检查 
 * @author 00 
 * 
 */
public class NetworkUtil {
	/**
	 * 没有网络
	 */
	public static final int NONETWORK = 0;
	/**
	 * 当前是wifi连接
	 */
	public static final int WIFI = 1;
	/**
	 * 不是wifi连接
	 */
	public static final int NOWIFI = 2;

	/**
	 * 检测当前网络的类型 是否是wifi
	 * 
	 * @param context
	 * @return
	 */
	public static int checkedNetWorkType(Context context) {
		if (!checkedNetWork(context)) {
			return NONETWORK;
		}
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting()) {
			return WIFI;
		} else {
			return NOWIFI;
		}
	}

	/**
	 * 检查是否连接网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkedNetWork(Context context) {
		// 1.获得连接设备管理器
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		/**
		 * 获取网络连接对象
		 */
		else {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();

			if (networkInfo == null || !networkInfo.isAvailable()
					|| !networkInfo.isConnected()) {
				return false;
			} else {
				if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
				return false;
			}
		}
		// return true;
	}

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
