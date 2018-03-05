package com.example.yuli.myapplication.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.example.yuli.myapplication.CustomView.QuaDashBoardView;
import com.example.yuli.myapplication.R;

public class QuaDashViewActivity extends Activity {

    private LinearLayout layout_quaview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quadashview_activity);
        int whichQua = getIntent().getIntExtra("whichQua",0);
        layout_quaview = (LinearLayout)findViewById(R.id.layout_quaview);
        if (whichQua!=0){
            layout_quaview.addView(new QuaDashBoardView(QuaDashViewActivity.this,whichQua));
        }
    }
}