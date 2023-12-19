package com.example.one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.example.one.fragments.baidu;
import com.example.one.fragments.bilibili;
import com.githang.statusbar.StatusBarCompat;

import com.example.one.activity.functionactivity;
import com.example.one.fragments.douyin;
import com.example.one.fragments.toutiao;
import com.example.one.fragments.weibo;
import com.example.one.fragments.zhihu;
import com.example.one.textcolor.textcolor1;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// 这里一定要继承 extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener
public class hotNews extends AppCompatActivity implements  ViewPager.OnPageChangeListener {

    //4个fragment
    private douyin douyintFragment;
    private weibo weiboFragment;
    private zhihu zhihuFragment;
    private toutiao toutiaoFragment;
    private baidu baiduFragment;

    private bilibili bilibiliFragment;

    // 中间内容区域
    private ViewPager viewPager; // 这个viewpage就是用来监听滑动的

    private List<Fragment> fragments; // 定义一个viewpage的碎片

    private String[] tabs = {"抖音", "微博", "知乎", "头条", "百度", "b站"};

    private int[] images = new int[]{
            R.mipmap.douyin_1,
            R.mipmap.weibo_2,
            R.mipmap.touxiang_zhihu,
            R.mipmap.toutiao,
            R.mipmap.baidu,
            R.mipmap.bilibili
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotnews);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.top_color),false);
        textcolor();
        getdouyin();
        getweibo();
        getzhihu();
        gettoutiao();
        getbaidu();
        getbilibili();
        initView(); //控件初始
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager,true);
        new Thread(mRunnable).start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 要重写的方法
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initView() {  // 对我们的UI界面进行初始化

        this.viewPager = findViewById(R.id.vp_content); // 重点

        //初始化4个fragment
        douyintFragment = new douyin(); // 这些碎片就是界面来的
        weiboFragment = new weibo();
        zhihuFragment = new zhihu();
        toutiaoFragment = new toutiao();
        baiduFragment = new baidu();
        bilibiliFragment = new bilibili();

        fragments = new ArrayList<>(); // 将我们定义的fragmaent对象存在list列表里面
        fragments.add(douyintFragment);
        fragments.add(weiboFragment);
        fragments.add(zhihuFragment);
        fragments.add(toutiaoFragment);
        fragments.add(baiduFragment);
        fragments.add(bilibiliFragment);

        FragmentManager fragmentManager = getSupportFragmentManager(); // 配置适配器
        viewPager.setAdapter(new PageAdapters(fragmentManager,0,fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    // 这个是viewpage来适配我们每一个fragment
    public class PageAdapters extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public PageAdapters(FragmentManager fm, int behavior,List<Fragment> fragmentList) {
            super(fm, behavior);
            this.fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = hotNews.this.getResources().getDrawable(images[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth()/7, image.getIntrinsicHeight()/7);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            SpannableString ss = new SpannableString(" "+tabs[position]);
            ss.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
    }

    private void textcolor()
    {
        // 作用是修改字体的颜色
        TextView function1_text = (TextView) findViewById(R.id.function1_text);
        textcolor1.setTextViewStyles(function1_text);
    }

////    热搜界面返回键
//    public void main_function(View view)
//    {
//        Intent intent = new Intent(this, functionactivity.class);
//        startActivity(intent);
//        finish();
//    }

    //api请求抖音热搜并存放SharedPreferences中
    private void getdouyin()
    {
        List<String> douyin_titles = new ArrayList<>();
        List<String> douyin_hots = new ArrayList<>();
        String url = "https://tenapi.cn/douyinresou/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                douyin_titles.add("网络错误！");
                douyin_hots.add("网络错误!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseString = response.body().string();
                try {
                    JSONObject jsonobject = new JSONObject(responseString);
                    if(jsonobject.optString("data").equals("200"))
                    {
                        JSONArray jsonArray = jsonobject.optJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobjects = jsonArray.optJSONObject(i);
                            douyin_titles.add(jsonobjects.optString("name"));
                            douyin_hots.add(jsonobjects.optString("hot"));
                        }
                        SharedPreferences sp = getSharedPreferences("douyin", Activity.MODE_PRIVATE);
                        Gson douyin_gson = new Gson();
                        String douyin_jsontitle = douyin_gson.toJson(douyin_titles);
                        String douyin_jsonhot = douyin_gson.toJson(douyin_hots);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("douyin_json_title", douyin_jsontitle);
                        edit.putString("douyin_json_hot", douyin_jsonhot);
                        edit.commit();

                    }else{
                        douyin_titles.add("接口失效！");
                        douyin_hots.add("接口失效!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }

    //api请求微博热搜并存放SharedPreferences中
    private void getweibo()
    {
        List<String> weibo_titles = new ArrayList<>();
        List<String> weibo_hots = new ArrayList<>();
        List<String> weibo_urls = new ArrayList<>();
        String url = "https://tenapi.cn/resou/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                weibo_titles.add("网络错误！");
                weibo_hots.add("网络错误!");
                weibo_urls.add("https://tenapi.cn/resou/");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseString = response.body().string();
                try {
                    JSONObject jsonobject = new JSONObject(responseString);
                    if(jsonobject.optString("data").equals("200"))
                    {
                        JSONArray jsonArray = jsonobject.optJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobjects = jsonArray.optJSONObject(i);
                            weibo_titles.add(jsonobjects.optString("name"));
                            weibo_hots.add(jsonobjects.optString("hot"));
                            weibo_urls.add(jsonobjects.optString("url"));
                        }
                        SharedPreferences sp = getSharedPreferences("weibo", Activity.MODE_PRIVATE);
                        Gson douyin_gson = new Gson();
                        String weibo_jsontitle = douyin_gson.toJson(weibo_titles);
                        String weibo_jsonhot = douyin_gson.toJson(weibo_hots);
                        String weibo_jsonurl = douyin_gson.toJson(weibo_urls);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("weibo_json_title", weibo_jsontitle);
                        edit.putString("weibo_json_hot", weibo_jsonhot);
                        edit.putString("weibo_json_url", weibo_jsonurl);
                        edit.commit();
                    }else{
                        weibo_titles.add("接口失效！");
                        weibo_hots.add("接口失效!");
                        weibo_urls.add("https://tenapi.cn/resou/");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }
    
    //api请求百度热搜并存放SharedPreferences中
    private void getbaidu() {
        List<String> baidu_titles = new ArrayList<>();
        List<String> baidu_hots = new ArrayList<>();
        List<String> baidu_urls = new ArrayList<>();
        String url = "https://tenapi.cn/v2/baiduhot/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                baidu_titles.add("网络错误！");
                baidu_hots.add("网络错误!");
                baidu_urls.add("https://tenapi.cn/v2/baiduhot/");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseString = response.body().string();
                try {
                    JSONObject jsonobject = new JSONObject(responseString);
                    if(jsonobject.optString("code").equals("200") &&
                            jsonobject.optString("msg").equals("success"))
                    {
                        JSONArray jsonArray = jsonobject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobjects = jsonArray.optJSONObject(i);
                            baidu_titles.add(jsonobjects.optString("name"));
                            baidu_hots.add(jsonobjects.optString("hot"));
                            baidu_urls.add(jsonobjects.optString("url"));
                        }
                        SharedPreferences sp = getSharedPreferences("baidu", Activity.MODE_PRIVATE);
                        Gson douyin_gson = new Gson();
                        String baidu_jsontitle = douyin_gson.toJson(baidu_titles);
                        String baidu_jsonhot = douyin_gson.toJson(baidu_hots);
                        String baidu_jsonurl = douyin_gson.toJson(baidu_urls);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("baidu_json_title", baidu_jsontitle);
                        edit.putString("baidu_json_hot", baidu_jsonhot);
                        edit.putString("baidu_json_url", baidu_jsonurl);
                        edit.commit();
                    }
                    else{
                        baidu_titles.add("接口失效！");
                        baidu_hots.add("接口失效!");
                        baidu_urls.add("https://tenapi.cn/v2/baiduhot/");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }

    //api请求知乎热搜并存放SharedPreferences中
    private void getzhihu()
    {
        List<String> zhihu_titles = new ArrayList<>();
        List<String> zhihu_hots = new ArrayList<>();
        List<String> zhihu_urls = new ArrayList<>();
        String url = "https://tenapi.cn/v2/zhihuhot/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                zhihu_titles.add("网络错误！");
                zhihu_hots.add("网络错误!");
                zhihu_urls.add("https://tenapi.cn/zhihuresou/");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseString = response.body().string();
                try {
                    JSONObject jsonobject = new JSONObject(responseString);
                    if(jsonobject.optString("code").equals("200") &&
                            jsonobject.optString("msg").equals("success"))
                    {
                        JSONArray jsonArray = jsonobject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobjects = jsonArray.optJSONObject(i);
                            String title = jsonobjects.optString("name");
                            if (title.length() > 15) {
                                title = title.substring(0, 15) + "...";
                            }
                            zhihu_titles.add(title);
                            zhihu_hots.add(jsonobjects.optString("hot"));
                            zhihu_urls.add(jsonobjects.optString("url"));
                        }
                        SharedPreferences sp = getSharedPreferences("zhihu", Activity.MODE_PRIVATE);
                        Gson douyin_gson = new Gson();
                        String zhihu_jsontitle = douyin_gson.toJson(zhihu_titles);
                        String zhihu_jsonhot = douyin_gson.toJson(zhihu_hots);
                        String zhihu_jsonurl = douyin_gson.toJson(zhihu_urls);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("zhihu_json_title", zhihu_jsontitle);
                        edit.putString("zhihu_json_hot", zhihu_jsonhot);
                        edit.putString("zhihu_json_url", zhihu_jsonurl);
                        edit.commit();
                    }else{
                        zhihu_titles.add("接口失效！");
                        zhihu_hots.add("接口失效!");
                        zhihu_urls.add("https://tenapi.cn/zhihuresou/");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }

    //api请求头条热搜并存放SharedPreferences中
    private void gettoutiao() {
        List<String> toutiao_titles = new ArrayList<>();
        List<String> toutiao_urls = new ArrayList<>();
        String url = "https://tenapi.cn/v2/toutiaohot/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toutiao_titles.add("网络错误！");
                toutiao_urls.add("https://tenapi.cn/v2/toutiaohot/");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseString = response.body().string();
                try {
                    JSONObject jsonobject = new JSONObject(responseString);
                    if(jsonobject.optString("code").equals("200") &&
                            jsonobject.optString("msg").equals("success"))
                    {
                        JSONArray jsonArray = jsonobject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobjects = jsonArray.optJSONObject(i);
                            toutiao_titles.add(jsonobjects.optString("name"));
                            toutiao_urls.add(jsonobjects.optString("url"));
                        }
                        SharedPreferences sp = getSharedPreferences("toutiao", Activity.MODE_PRIVATE);
                        Gson douyin_gson = new Gson();
                        String toutiao_jsontitle = douyin_gson.toJson(toutiao_titles);
                        String toutiao_jsonurl = douyin_gson.toJson(toutiao_urls);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("toutiao_json_title", toutiao_jsontitle);
                        edit.putString("toutiao_json_url", toutiao_jsonurl);
                        edit.commit();
                    }else{
                        toutiao_titles.add("接口失效！");
                        toutiao_urls.add("https://tenapi.cn/v2/toutiaohot/");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }

    //api请求b站热搜并存放SharedPreferences中
    private void getbilibili() {
        List<String> bilibili_titles = new ArrayList<>();
        List<String> bilibili_urls = new ArrayList<>();
        String url = "https://tenapi.cn/v2/bilihot/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                bilibili_titles.add("网络错误！");
                bilibili_urls.add("https://tenapi.cn/v2/bilihot/");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseString = response.body().string();
                try {
                    JSONObject jsonobject = new JSONObject(responseString);
                    if(jsonobject.optString("code").equals("200") &&
                            jsonobject.optString("msg").equals("success"))
                    {
                        JSONArray jsonArray = jsonobject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobjects = jsonArray.optJSONObject(i);
                            bilibili_titles.add(jsonobjects.optString("name"));
                            bilibili_urls.add(jsonobjects.optString("url"));
                        }
                        SharedPreferences sp = getSharedPreferences("bilibili", Activity.MODE_PRIVATE);
                        Gson douyin_gson = new Gson();
                        String bilibili_jsontitle = douyin_gson.toJson(bilibili_titles);
                        String bilibili_jsonurl = douyin_gson.toJson(bilibili_urls);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("bilibili_json_title", bilibili_jsontitle);
                        edit.putString("bilibili_json_url", bilibili_jsonurl);
                        edit.commit();
                    }else{
                        bilibili_titles.add("接口失效！");
                        bilibili_urls.add("https://tenapi.cn/v2/bilihot/");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        });
    }

    //刷新UI界面
    private void refreshView() {
        this.viewPager = findViewById(R.id.vp_content); // 重点
        int position = viewPager.getCurrentItem();
        //初始化4个fragment
        douyintFragment = new douyin(); // 这些碎片就是界面来的
        weiboFragment = new weibo();
        zhihuFragment = new zhihu();
        toutiaoFragment = new toutiao();
        baiduFragment = new baidu();
        bilibiliFragment = new bilibili();

        fragments = new ArrayList<>(); // 将我们定义的fragmaent对象存在list列表里面
        fragments.add(douyintFragment);
        fragments.add(weiboFragment);
        fragments.add(zhihuFragment);
        fragments.add(toutiaoFragment);
        fragments.add(baiduFragment);
        fragments.add(bilibiliFragment);

        FragmentManager fragmentManager = getSupportFragmentManager(); // 配置适配器
        viewPager.setAdapter(new PageAdapters(fragmentManager,0,fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(position);
    }

    //定时刷新函数
    private void refresh() {
        getdouyin();
        getweibo();
        getzhihu();
        gettoutiao();
        getbaidu();
        getbilibili();
        refreshView();
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager,true);
    }

    //调用定时刷新函数
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            refresh();//编写的定时刷新函数
        }
    };

    //实现定时刷新
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    //sleep60秒
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(mHandler.obtainMessage());
            }
        }
    };
}