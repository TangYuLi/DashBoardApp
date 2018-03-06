package com.example.yuli.myapplication.JNI;

/**
 * Created by tangyuli on 2018/3/5.
 */

public class MyJNI {

    static {
        System.load("MyJNI");
    }

    public native static String getAppkey();

}
