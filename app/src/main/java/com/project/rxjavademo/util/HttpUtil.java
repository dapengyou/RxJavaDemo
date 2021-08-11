package com.project.rxjavademo.util;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @createTime: 8/2/21
 * @author: lady_zhou
 * @Description:
 */
public class HttpUtil {
    public static String BASE_URL = "https://www.wanandroid.com/";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    //retrofit 第四步  创建Retrofit对象
    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    Log.e("OKHttp-----", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("OKHttp-----", message);
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);// 四个级别：NONE,BASIC,HEADER,BODY; BASEIC:请求/响应行;  HEADER:请求/响应行 + 头;  BODY:请求/响应航 + 头 + 体;

        //加配http 请求参数
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor()) //调试神器
                .readTimeout(10000, TimeUnit.SECONDS)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL) //设置网络请求的Url地址
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                // 添加rxjava处理工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
