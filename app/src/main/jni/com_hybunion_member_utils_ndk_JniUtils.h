/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include <android/log.h>
/* Header for class com_hybunion_member_utils_ndk_JniUtils */

#ifndef _Included_com_hybunion_member_utils_ndk_JniUtils
#define _Included_com_hybunion_member_utils_ndk_JniUtils
#ifdef __cplusplus
extern "C" {
#endif

#define  TAG   "king"

#define LOGV(...)   __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...)    __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...)    __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...)   __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...)   __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
/*
 * Class:     com_hybunion_member_utils_ndk_JniUtils
 * Method:    getStringFormC
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_hybunion_member_utils_ndk_JniUtils_getStringFormC
  (JNIEnv *, jclass);

/*
 * Class:     com_hybunion_member_utils_ndk_JniUtils
 * Method:    getKeyValue
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_hybunion_member_utils_ndk_JniUtils_getKeyValue
  (JNIEnv *, jclass);

/*
 * Class:     com_hybunion_member_utils_ndk_JniUtils
 * Method:    getIv
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_hybunion_member_utils_ndk_JniUtils_getIv
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif