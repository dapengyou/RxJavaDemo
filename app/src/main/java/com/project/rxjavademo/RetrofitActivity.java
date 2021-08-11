package com.project.rxjavademo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.rxjavademo.api.WangAndroidApi;
import com.project.rxjavademo.bean.ProjectBean;
import com.project.rxjavademo.util.HttpUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @createTime: 8/2/21
 * @author: lady_zhou
 * @Description:
 */
public class RetrofitActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        mTextView = findViewById(R.id.text);
        findViewById(R.id.retrofit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestRetrofit();
                requestRxJava();
//                testRxJava();
            }

        });
    }

    String temp = "";

    private void testRxJava() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("hello");
                e.onNext("RxJava");
                e.onComplete();
            }
        });
        String url = "http://www.baidu.com";
        Observable<String> observable1 = Observable.just(url);

        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("rxjava", "方法：onSubscribe: ");
            }

            @Override
            public void onNext(String string) {
                Log.d("rxjava", "方法：onNext: " + string);
                temp += string + "\t";
                mTextView.setText(temp);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("rxjava", "方法：onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("rxjava", "方法：onComplete: ");
            }
        };

//        observable.subscribe(observer);
        observable1.subscribe(observer);
    }

    private void requestRxJava() {
        //retrofit 第五步 创建 网络请求接口 的实例
        WangAndroidApi api = HttpUtil.getRetrofit().create(WangAndroidApi.class);

        //第六步  获取网络数据
        api.getProject().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProjectBean>() {
                    @Override
                    public void accept(@NonNull ProjectBean projectBean) throws Exception {
                        //第7步 处理返回的数据
                        System.out.println(projectBean.toString());
                        mTextView.setText(projectBean.toString());
                    }
                });
    }

    private void requestRetrofit() {
        //retrofit 第五步 创建 网络请求接口 的实例
        WangAndroidApi api = HttpUtil.getRetrofit().create(WangAndroidApi.class);
        Call<ProjectBean> call = api.getProject1();

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
