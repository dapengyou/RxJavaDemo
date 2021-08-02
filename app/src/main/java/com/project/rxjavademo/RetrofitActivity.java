package com.project.rxjavademo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.rxjavademo.api.WangAndroidApi;
import com.project.rxjavademo.bean.ProjectBean;
import com.project.rxjavademo.util.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @createTime: 8/2/21
 * @author: lady_zhou
 * @Description:
 */
public class RetrofitActivity extends AppCompatActivity {
   private TextView mTextView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        mTextView = findViewById(R.id.text);
        findViewById(R.id.retrofit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRetrofit();
//                requestRxJava();
            }

        });
    }

    private void requestRxJava() {

    }

    private void requestRetrofit() {
        //retrofit 第五步 创建 网络请求接口 的实例
        WangAndroidApi api = HttpUtil.getRetrofit().create(WangAndroidApi.class);
        Call<ProjectBean> call = api.getProject();

        //第六步 发送网络请求
        call.enqueue(new Callback<ProjectBean>() {
            @Override
            public void onResponse(Call<ProjectBean> call, Response<ProjectBean> response) {
                //第7步 处理返回的数据
                System.out.println(response.body().toString());
                mTextView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<ProjectBean> call, Throwable t) {

            }
        });
    }
}
