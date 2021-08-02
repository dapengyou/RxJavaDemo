package com.project.rxjavademo.util;

import retrofit2.Retrofit;
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
    public static Retrofit getRetrofit(){
        return  new Retrofit.Builder()
                .baseUrl(BASE_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();
    }
}
