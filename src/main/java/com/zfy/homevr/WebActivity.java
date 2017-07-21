package com.zfy.homevr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/6/23.
 */

public   class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView webView = (WebView)findViewById(R.id.webView01);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        String url = "file:///android_asset/www/"+getUrl();
        webView.loadUrl(url);
        webView.addJavascriptInterface(new JSUtils(this),"jsutil");
        webView.addJavascriptInterface(this,"webActivity");
    }

    @JavascriptInterface
    public String getParams(){
        Intent intent = getIntent();
        String params = intent.getStringExtra("params");
        return params;

    }
    protected  String getUrl(){
        Intent intent = getIntent();
        String params = intent.getStringExtra("url");
        return params;

    }


}
