package com.blue.startallplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAllPlayer(View view) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2016/07/19/mp4/160719095812990469.mp4"),"video/*");
        startActivity(intent);
    }
}
