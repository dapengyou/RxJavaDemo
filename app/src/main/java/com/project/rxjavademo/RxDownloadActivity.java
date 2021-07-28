package com.project.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxDownloadActivity extends AppCompatActivity {
    private static final String TAG = "RxDownloadActivity";
    // 网络图片的链接地址
    private final static String PATH = "https://img-blog.csdnimg.cn/20190304140934631.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpa2VfbGdn,size_16,color_FFFFFF,t_70";

    // 弹出加载框
    private ProgressDialog mProgressDialog;

    private ImageView mImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_download);
        mImage = findViewById(R.id.image);
    }

    public void rxJavaDownloadImageAction(View view) {
        Observable.just(PATH)// todo 第二步
                //todo 第三步
                .map(new Function<String, Bitmap>() {

                    @NonNull
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        URL url = new URL(PATH);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setConnectTimeout(5000);
                        int responseCode = httpURLConnection.getResponseCode(); // 才开始 request
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            return bitmap;
                        }
                        return null;
                    }
                })
                //切换线程
                .compose(rxud())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //todo 第一步
                        Log.d(TAG, "方法：onSubscribe: ");
                        mProgressDialog = new ProgressDialog(RxDownloadActivity.this);
                        mProgressDialog.setTitle("run");
                        mProgressDialog.show();
                    }

                    // TODO 第四步
                    // 拿到事件
                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.d(TAG, "方法：onNext: ");
                        mImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "方法：onError: "+e.getMessage());
                    }

                    // TODO 第五步
                    // 完成事件
                    @Override
                    public void onComplete() {
                        if (mProgressDialog != null)
                            mProgressDialog.dismiss();
                    }
                });
    }

    /**
     * 封装我们的操作
     * upstream   上游
     * d
     */
    public final static <UD> ObservableTransformer<UD, UD> rxud() {
        return new ObservableTransformer<UD, UD>() {
            @Override
            public ObservableSource<UD> apply(Observable<UD> upstream) {
                return upstream.subscribeOn(Schedulers.io())     // 给上面代码分配异步线程
                        .observeOn(AndroidSchedulers.mainThread()) // 给下面代码分配主线程;
                        .map(new Function<UD, UD>() {
                            @Override
                            public UD apply(UD ud) throws Exception {
                                Log.d(TAG, "apply: 我监听到你了，居然再执行");
                                return ud;
                            }
                        });
            }
        };
    }
}