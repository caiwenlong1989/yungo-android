package com.caiwl.yungo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.caiwl.yungo.fragment.HomeFragment;
import com.caiwl.yungo.fragment.IntroduceFragment;
import com.caiwl.yungo.fragment.MyFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private Fragment[] fragments;

    private boolean hasGotToken = false;

    private AlertDialog.Builder alertDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction
                            .show(fragments[0])
                            .hide(fragments[1])
                            .hide(fragments[2])
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction
                            .show(fragments[1])
                            .hide(fragments[0])
                            .hide(fragments[2])
                            .commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentTransaction
                            .show(fragments[2])
                            .hide(fragments[0])
                            .hide(fragments[1])
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment homeFragment = new HomeFragment();
        Fragment introduceFragment = new IntroduceFragment();
        Fragment myFragment = new MyFragment();
        fragments = new Fragment[]{homeFragment, introduceFragment, myFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.navigation_home, homeFragment)
                .add(R.id.navigation_dashboard, introduceFragment)
                .add(R.id.navigation_notifications, myFragment)
                .show(homeFragment)
                .hide(introduceFragment)
                .hide(myFragment)
                .commit();

        alertDialog = new AlertDialog.Builder(this);
        initAccessToken();
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

}
