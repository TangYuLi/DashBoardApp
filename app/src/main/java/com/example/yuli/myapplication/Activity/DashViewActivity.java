package com.example.yuli.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yuli.myapplication.CustomView.DashBoardView;
import com.example.yuli.myapplication.CustomView.QuaDashBoardView;
import com.example.yuli.myapplication.R;

public class DashViewActivity extends Activity {

    private DashBoardView dashBoardView;
    private QuaDashBoardView quaDashBoardView;
    private LinearLayout linearLayout;
    private AlphaAnimation outAnim;
    private AlphaAnimation inAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashview_activity);
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        if (dashBoardView == null){
            dashBoardView = new DashBoardView(DashViewActivity.this);
        }
        linearLayout.addView(dashBoardView);
        outAnim = new AlphaAnimation(1,0);
        outAnim.setDuration(800);
        inAnim = new AlphaAnimation(0,1);
        inAnim.setDuration(800);
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == -2){
                int rotateRound = (int)msg.obj;
                Toast.makeText(DashViewActivity.this, ""+rotateRound+"°", Toast.LENGTH_LONG).show();
                dashBoardView.setRotateRound(rotateRound);
                updateView(quaDashBoardView,dashBoardView);
            }else if (msg.what!=-1){
                int whichQua = msg.what;
                quaDashBoardView = new QuaDashBoardView(DashViewActivity.this,whichQua);
                updateView(dashBoardView,quaDashBoardView);
            }else ;

        }
    };

    private void updateView(View out,View in){
        out.startAnimation(outAnim);
        linearLayout.removeView(out);
        in.startAnimation(inAnim);
        linearLayout.addView(in);
    }
}