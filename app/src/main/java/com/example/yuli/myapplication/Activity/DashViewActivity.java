package com.example.yuli.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.yuli.myapplication.CustomView.DashBoardView;
import com.example.yuli.myapplication.R;

public class DashViewActivity extends Activity {

    private DashBoardView dashBoardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashview_activity);
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what!=-1){
                Intent intent = new Intent(DashViewActivity.this,QuaDashViewActivity.class);
                intent.putExtra("whichQua",msg.what);
                startActivity(intent);
            }
        }
    };
}