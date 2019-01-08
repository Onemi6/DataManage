package com.hzlf.sampletest.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 将字节流转换为字符串的工具类
 */
public class HttpUtils {

    //private  static String serverurl = "http://www.3tpi.com:8016/update.xml";
    private static String serverurl = "http://111.2.23.176:8083/";
    //private  static String ip = "http://www.3tpi.com:8016/";
    //private  static String ip = "http://111.2.23.176:8083/";
    private static String ip = "http://111.2.23.176:8085/";

    public static eLab_API GsonApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(eLab_API.class);
    }

    public static eLab_API XmlApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverurl)
                .addConverterFactory(SimpleXmlConverterFactory.create()) //设置使用SimpleXml解析
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(eLab_API.class);
    }
}