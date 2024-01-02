package com.example.one;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
public class baidu_webview extends AppCompatActivity {
    private List<String> baidu_urls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_webview);
        SharedPreferences sps = baidu_webview.this.getSharedPreferences("baidu", Activity.MODE_PRIVATE);
        String urls_listJsons = sps.getString("baidu_json_url","");
        if(!urls_listJsons.equals(""))
        {
            Gson gson = new Gson();
            baidu_urls = gson.fromJson(urls_listJsons,new TypeToken<List<String>>(){}.getType());
        }
        Intent intent = getIntent();
        Bundle bundles = intent.getBundleExtra("baidu");
        int position =  bundles.getInt("position");
        view_webview(position);
    }

    private void view_webview(int position){

        WebView webView = (WebView) findViewById(R.id.baidu_webview);
        WebSettings webSettings = webView.getSettings();
        String url = baidu_urls.get(position);
        webView.loadUrl(url);


        //清除网页访问留下的缓存 ,由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        webView.clearCache(true);
        //进行配置-利用WebSettings子类
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        //webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提。
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        //webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, WebResourceRequest request) {
                String url =request.getUrl().toString();
                try {
                    if(url.startsWith("http") || url.startsWith("https")){
                        //使用WebView加载显示url
                        view.loadUrl(url);
                    }
                    else{
                        Uri uri = Uri.parse(url); //url为你要链接的地址
                        Intent intent =new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                    // 返回结果
                    return true;
                }catch (Exception e)
                {
                    return  false;
                }
            }
        });


        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //按返回键操作并且能回退网页
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        //后退
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });


        webView.setWebViewClient(new WebViewClient(){
            @Override

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                Toast.makeText(baidu_webview.this,"加载中...",Toast.LENGTH_SHORT).show();
                if(url.contains("baidu://"))
                {
                    view.loadUrl("https://www.baidu.com/");
                }
            }
        });
    }


    public void baidu_listview(View view)
    {
        Intent intent = new Intent(this, hotNews.class);
        startActivity(intent);
        finish();
    }
}
