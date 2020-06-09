//
// Created by XY on 16/1/4.
//
#include "com_hybunion_member_utils_ndk_JniUtils.h"
#include <string.h>
/*
 * Class:     Java_com_wobiancao_ndkjnidemo_ndk_JniUtils
 * Method:    getStringFormC
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_hybunion_member_utils_ndk_JniUtils_getStringFormC
        (JNIEnv *env, jobject obj){

    return (*env)->NewStringUTF(env,"这里是来自c的string");
}
//const char keyValue[] = {
//        0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
//        0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38
//};
//const char keyValue[] = "HRThyb6235pay311";
const char keyValue[] = {
        0x48,0x52,0x54,0x68,0x79,0x62,0x36,0x32,0x33,0x35,0x70,0x61,0x79,0x33,0x31,0x31

};
//const char keyValue[] = "HRThyb6235pay3131ment86union400O";

//const char keyValue[] = {
//        0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c, 0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07,
//        0x0a,0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c, 0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07,
//        0x0a
//
//};

//const char iv[] =  {    //16 bit
//            -33, 32, -25, 25, 35, -27, 55, -12, -15,32,
//                    23, 45, -26, 32, 5,16
//};
const char iv[] =  {    //16 bit
        -33, 32, -25, 25, 35, -27, 55, -12, -15,32,
        23, 45, -26, 32, 5,16
};

jbyteArray Java_com_hybunion_member_utils_ndk_JniUtils_getKeyValue(JNIEnv *env, jobject obj)
{

    jbyteArray kvArray = (*env)->NewByteArray(env, sizeof(keyValue));
    jbyte *bytes = (*env)->GetByteArrayElements(env,kvArray,0);

    int i;
    for (i = 0; i < sizeof(keyValue);i++){
        bytes[i] = (jbyte)keyValue[i];
    }

    (*env)->SetByteArrayRegion(env,kvArray, 0, sizeof(keyValue),bytes);
    (*env)->ReleaseByteArrayElements(env,kvArray,bytes,0);

    return kvArray;
}

//JNIEXPORT JNICALL
jbyteArray Java_com_hybunion_member_utils_ndk_JniUtils_getIv(JNIEnv *env, jobject obj)
{

    jbyteArray ivArray = (*env)->NewByteArray(env, sizeof(iv));
    jbyte *bytes = (*env)->GetByteArrayElements(env,ivArray, 0);

    int i;
    for (i = 0; i < sizeof(iv); i++){
        bytes[i] = (jbyte)iv[i];
    }

    (*env)->SetByteArrayRegion(env,ivArray, 0, sizeof(iv), bytes);
    (*env)->ReleaseByteArrayElements(env,ivArray,bytes,0);

    return ivArray;
}

