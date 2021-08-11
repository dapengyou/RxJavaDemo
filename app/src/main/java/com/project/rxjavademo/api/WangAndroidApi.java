package com.project.rxjavademo.api;

import com.project.rxjavademo.bean.ProjectBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @createTime: 7/29/21
 * @author: lady_zhou
 * @Description: 第三步 用于网络请求 的接口
 */
public interface WangAndroidApi {
    @GET("project/tree/json")
    Observable<ProjectBean> getProject();

    @GET("project/tree/json")
    Call<ProjectBean> getProject1();
}
