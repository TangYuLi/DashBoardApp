package com.example.yuli.myapplication.JNI;

public class NativeHelper {

    static {
        System.load("NativeHelper");
    }

    public native static String getAppKey();

}