package com.zfy.homevr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by xiangzy on 2017/6/23.
 */
public class HouseTypeListActivity extends WebActivity {

    @Override
    protected String getUrl() {
        return "housetype_list/index.html";
    }

}
