//
// Created by tangyuli on 2018/3/5.
//
#include "com_example_yuli_myapplication_JNI_NativeHelper.h"
#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_yuli_myapplication_JNI_NativeHelper_getAppKey(JNIEnv *env, jclass instance){
    return (*env).NewStringUTF("I am From Native C");
}
