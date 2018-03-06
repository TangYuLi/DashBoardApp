LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := MyJNI

LOCAL_SRC_FILES := MyJNI.cpp
LOCAL_SRC_FILES += util.cpp

include $(BUILD_SHARED_LIBRARY)