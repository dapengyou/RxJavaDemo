package com.project.rxjavademo.util;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
        //加配http 请求参数
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder
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
