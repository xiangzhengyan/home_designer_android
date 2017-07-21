package com.zfy.homevr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by xiangzy on 2017/6/23.
 */

public class JSUtils {
    private Activity activity;
    public JSUtils(Activity activity) {
        this.activity = activity;
    }


    @JavascriptInterface
    public void toActivity(String activityName,String params){
        try {
            Class cls = Class.forName(activityName);
            Intent intent = new Intent(activity, cls);
            if(params!=null) {
                intent.putExtra("params", params);
            }
            activity.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void toActivityAndFinish(String activityName,String params){
        this.toActivity(activityName,params);
//        System.exit(0);
        activity.finish();
    }

    @JavascriptInterface
    public void toVRActivity(String params){
        Intent intent = new Intent(activity, UnityPlayerActivity.class);
    }

//    @JavascriptInterface
//    public void toWebActivity(String url,String params){
//        try {
//
//            Intent intent = new Intent(activity, WebViewActivity.class);
//            intent.putExtra("url",url);
//            if(params!=null) {
//                intent.putExtra("params", params);
//            }
//            activity.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @JavascriptInterface
    public void toBack(){
        activity.finish();
    }

    //将显示Toast和对话框的方法暴露给JS脚本调用
    @JavascriptInterface
    public void showToast(String name) {
        Toast.makeText(activity, name, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("标题")
                .setMessage("我是Java不带参").create().show();
    }
}
