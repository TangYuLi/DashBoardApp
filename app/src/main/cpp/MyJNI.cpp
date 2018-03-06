//
// Created by tangyuli on 2018/3/5.
//
#include "jni.h"
#include "com_example_yuli_myapplication_JNI_MyJNI.h"

JNIEXPORT jstring JNICALL Java_com_example_yuli_myapplication_JNI_MyJNI_getAppkey
        (JNIEnv *env, jclass instance) {
    return (*env).NewStringUTF("JNITest Successfully");
}
