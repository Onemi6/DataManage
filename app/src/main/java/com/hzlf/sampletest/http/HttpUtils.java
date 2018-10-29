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
 * 将字节流转换为字符串的工具类
 */
public class HttpUtils {

    // 同步用户信息
    public static String getUsers(String path) {
        int code;
        try {
            URL url = new URL(path);
            /**
             * 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
             */
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");// 使用GET方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
            code = conn.getResponseCode();
            if (code == 200) {
                /**
                 * 如果获取的code为200，则证明数据获取是正确的。
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

    // 同步任务来源
    public static String getAllSource(String path) {
        int code;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");// 使用GET方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
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

    // 登录检查
    public static String LoginCheck(final String path,
                                    final Map<String, String> body) {
        int code;
        try {
            URL url = new URL(path);
            String content = String.valueOf(getjsonData(body));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// 使用POST方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType
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

    // 获得抽样单编号
    public static String getCode(String path) {
        int code;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");// 使用GET方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
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

    // 上传数据
    public static String UploadData(final String path,
                                    final Map<String, String> body) {
        int code;
        try {
            URL url = new URL(path);
            String content = String.valueOf(getjsonData(body));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// 使用POST方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType
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

    // 获得ClientByName
    public static String getClientByName(String path) {
        int code;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");// 使用GET方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
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


    public static String getAAQI(String path) {
        int code;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");// 使用GET方法获取
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
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
     * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
     */
    public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");// 设置解析的数据源
        int type = parser.getEventType();
        UpdateInfo info = new UpdateInfo();// 实体
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.nextText()); // 获取版本号
                    } else if ("url".equals(parser.getName())) {
                        info.setUrl(parser.nextText()); // 获取要升级的APK文件
                    }
                    break;
            }
            type = parser.next();
        }
        return info;
    }

    public static File getFileFromServer(String path, ProgressDialog pd)
            throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
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
                // 获取当前下载量
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
            String errorStr = "获取数据失败";
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