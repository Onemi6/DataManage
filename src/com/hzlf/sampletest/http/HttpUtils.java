package com.hzlf.sampletest.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Xml;

import com.hzlf.sampletest.entityclass.UpdateInfo;

/**
 * ���ֽ���ת��Ϊ�ַ����Ĺ�����
 */
public class HttpUtils {

	// ͬ���û���Ϣ
	public static String getUsers(String path) {
		int code;
		try {
			URL url = new URL(path);
			/**
			 * ������������ʹ�õ�����HttpURLConnection������һ�ֿ���ѡ��ʹ����HttpClient��
			 */
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");// ʹ��GET������ȡ
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			code = conn.getResponseCode();
			if (code == 200) {
				/**
				 * �����ȡ��codeΪ200����֤�����ݻ�ȡ����ȷ�ġ�
				 */
				String result = readMyInputStream(conn.getInputStream());
				return result;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
		return "";
	}

	// ͬ��������Դ
	public static String getAllSource(String path) {
		int code;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");// ʹ��GET������ȡ
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			code = conn.getResponseCode();
			if (code == 200) {
				String result = readMyInputStream(conn.getInputStream());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}

	// ��¼���
	public static String LoginCheck(final String path,
			final Map<String, String> body) {
		int code;
		try {
			URL url = new URL(path);
			String content = String.valueOf(getjsonData(body));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// ʹ��POST������ȡ
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			// �����������
			conn.setDoOutput(true);
			conn.setRequestProperty("ser-Agent", "Fiddler");
			// ����contentType
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(content.getBytes());
			os.flush();
			code = conn.getResponseCode();
			if (code == 200) {
				String result = readMyInputStream(conn.getInputStream());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
		return "";
	}

	// ��ó��������
	public static String getCode(String path) {
		int code;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");// ʹ��GET������ȡ
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			code = conn.getResponseCode();
			if (code == 200) {
				String result = readMyInputStream(conn.getInputStream());
				return result;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}

	// �ϴ�����
	public static String UploadData(final String path,
			final Map<String, String> body) {
		int code;
		try {
			URL url = new URL(path);
			String content = String.valueOf(getjsonData(body));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// ʹ��POST������ȡ
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			// �����������
			conn.setDoOutput(true);
			conn.setRequestProperty("ser-Agent", "Fiddler");
			// ����contentType
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(content.getBytes());
			os.flush();
			code = conn.getResponseCode();
			if (code == 200) {
				String result = readMyInputStream(conn.getInputStream());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
	
	
	// ���ClientByName
		public static String getClientByName(String path) {
			int code;
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");// ʹ��GET������ȡ
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(2000);
				code = conn.getResponseCode();
				if (code == 200) {
					String result = readMyInputStream(conn.getInputStream());
					return result;

				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
			return "";
		}
		
		
		// ͬ���û���Ϣ
		public static String getAAQI(String path) {
			int code;
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");// ʹ��GET������ȡ
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(2000);
				code = conn.getResponseCode();
				if (code == 200) {
					String result = readMyInputStream(conn.getInputStream());
					return result;

				}
			} catch (Exception e) {
				e.printStackTrace();
				return "1";
			}
			return "";
		}

	/*
	 * ��pull�������������������ص�xml�ļ� (xml��װ�˰汾��)
	 */
	public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// ���ý���������Դ
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// ʵ��
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText()); // ��ȡ�汾��
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText()); // ��ȡҪ������APK�ļ�
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}

	public static File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// �����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// ��ȡ���ļ��Ĵ�С
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					"datamanage.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// ��ȡ��ǰ������
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	public static String readMyInputStream(InputStream is) {
		byte[] result;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			is.close();
			baos.close();
			result = baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
			String errorStr = "��ȡ����ʧ��";
			return errorStr;
		}
		return new String(result);
	}

	public static JSONObject getjsonData(Map<String, String> body) {
		JSONObject js = new JSONObject();
		for (Map.Entry<String, String> entry : body.entrySet()) {

			try {
				js.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(entry.getKey() + "," + entry.getValue());
		}
		return js;
	}
}
