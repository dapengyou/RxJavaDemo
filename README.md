# RxJavaDemo
- RxJava接入
- RxJava 简单用法
- Retrofit 简单使用
- RxJava + Retrofit

## RxJava接入
在 gradle 里接入 RxJava2，RxJava3 已经出了，[RxJava 地址](https://github.com/ReactiveX/RxJava)
```
  implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
  implementation 'io.reactivex.rxjava2:rxjava:2.0.7'
```
## RxJava 简单用法
### 简单用法
- 创建被观察者
- 创建观察者
- 订阅
#### 创建被观察者
常用的创建操作符
  - creat
  - just
creat 操作符：
```
 Observable observable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                     e.onNext("hello");
                     e.onNext("RxJava");
                     e.onComplete();
            }
        });
```
如果有类型需求可以用泛型加入需要的类型，例如：
```
 Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

            }
        });
```
ObservableEmitter 作为发射器，一共有三种发射方式
- void onNext(T value) ---------- 可以无限调用
- void onComplete()-------------- 可以重复调用，但是观察者只会接收一次
- void onError(Throwable error)------ 与onComplete 互斥，且不能重复调用，第二次调用会报异常

just 操作符:
```
 String url = "http://www.baidu.com";
 Observable<String> observable1 = Observable.just(url);
```

#### 创建观察者
```
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
```

#### 订阅
```
        observable.subscribe(observer);
```

### 操作符有哪些
1. [创建操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Creating-Observables.html) Create, Defer, Empty/Never/Throw, From, Interval, Just, Range, Repeat, Start, Timer
2. [变换操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Transforming-Observables.html) Buffer, FlatMap, GroupBy, Map, Scan和Window
3. [过滤操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Filtering-Observables.html) Debounce, Distinct, ElementAt, Filter, First, IgnoreElements, Last, Sample, Skip, SkipLast, Take, TakeLast
4. [组合操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Combining-Observables.html) And/Then/When, CombineLatest, Join, Merge, StartWith, Switch, Zip
5. [错误处理](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Error-Handling-Operators.html) Catch和Retry
6. [辅助操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Observable-Utility-Operators.html) Delay, Do, Materialize/Dematerialize, ObserveOn, Serialize, Subscribe, SubscribeOn, TimeInterval, Timeout, Timestamp, Using
7. [条件和布尔操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Conditional-and-Boolean-Operators.html) All, Amb, Contains, DefaultIfEmpty, SequenceEqual, SkipUntil, SkipWhile, TakeUntil, TakeWhile
8. [算术和集合操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Mathematical-and-Aggregate-Operators.html) Average, Concat, Count, Max, Min, Reduce, Sum
9. [转换操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/To.html) To
10. [连接操作](https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Connectable-Observable-Operators.html) Connect, Publish, RefCount, Replay
11. [反压操作](https://mcxiaoke.gitbooks.io/rxdocs/content/topics/Backpressure.html) 用于增加特殊的流程控制策略的操作符

### 举个栗子
下面以从Url 中下载图片为例，展示简单的使用方法。
```
public class RxDownloadActivity extends AppCompatActivity {
    private static final String TAG = "RxDownloadActivity";
    // 网络图片的链接地址
    private final static String PATH = "https://alifei03.cfp.cn/creative/vcg/veer/1600water/veer-132668332.jpg";

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
}

```
xml：
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".RxDownloadActivity">

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:onClick="rxJavaDownloadImageAction"
        android:text="RxJava方式下载图片功能" />

    <ImageView
        android:id="@+id/image"
        android:layout_marginTop="10dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</LinearLayout>
```

遇到  No Network Security Config specified, using platform default 问题   提示你没有使用网络安全配置

android9.0（28）以后，系统默认不在支持http请求，当我们在发送http请求时会出现以下提示，并且请求网络失败。

## Retrofit 简单使用
### gradle 引用 Retrofit
```
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"
```
### 创建网络请求接口
```
public interface WangAndroidApi {
    @GET("project/tree/json")
    Call<ProjectBean> getProject1();
}
```
### 创建 Retrofit 对象
```
public class HttpUtil {
    public static String BASE_URL = "https://www.wanandroid.com/";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    //retrofit 第四步  创建Retrofit对象
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();
    }
}
```

### 加入log日志
```
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
```
### 加入请求OkHttp
```
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
                .build();
```
### 创建 网络请求接口 的实例
```
        WangAndroidApi api = HttpUtil.getRetrofit().create(WangAndroidApi.class);
        Call<ProjectBean> call = api.getProject1();
```
### 发送网络请求
```
     call.enqueue(new Callback<ProjectBean>() {
            @Override
            public void onResponse(Call<ProjectBean> call, Response<ProjectBean> response) {
                // 处理返回的数据
                System.out.println(response.body().toString());
                mTextView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<ProjectBean> call, Throwable t) {

            }
        });
```
以上是Retrofit 进行网络请求的简单操作
## RxJava + Retrofit
RxJava + Retrofit  与 纯Retrofit 的差别：
### 创建网络请求接口
```
public interface WangAndroidApi {
    @GET("project/tree/json")
    Observable<ProjectBean> getProject();
}
```
返回的从 Call 变成了 Observable
###  获取网络数据
```
  WangAndroidApi api = HttpUtil.getRetrofit().create(WangAndroidApi.class);
  api.getProject().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProjectBean>() {
                    @Override
                    public void accept(@NonNull ProjectBean projectBean) throws Exception {
                        // 处理返回的数据
                        System.out.println(projectBean.toString());
                        mTextView.setText(projectBean.toString());
                    }
                });
```
