package com.zfy.homevr;

import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by xiangzy on 2017/6/23.
 */
public class MainActivity extends WebActivity {

    @Override
    protected String getUrl() {
        return "main/index.html";
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 3000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


}
