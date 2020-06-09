//#include "com_hybunion_hrtpayment_utils_EncryptionData.h"
//#include "test.h"
//#include <dlfcn.h>
//
//#include <stddef.h>
//#include <malloc.h>
//#include <string.h>
//#include "des.h"
//
//
////
//// Created by king on 2015/10/8.
////
//
//char* jstringTostr(JNIEnv* env, jstring jstr)
//{
//    char* pStr = NULL;
//    jclass     jstrObj   = (*env)->FindClass(env, "java/lang/String");
//    jstring    encode    = (*env)->NewStringUTF(env, "utf-8");
//    jmethodID  methodId  = (*env)->GetMethodID(env, jstrObj, "getBytes", "(Ljava/lang/String;)[B");
//    jbyteArray byteArray = (jbyteArray)(*env)->CallObjectMethod(env, jstr, methodId, encode);
//    jsize      strLen    = (*env)->GetArrayLength(env, byteArray);
//    jbyte      *jBuf     = (*env)->GetByteArrayElements(env, byteArray, JNI_FALSE);
//    if (jBuf > 0)
//    {
//        pStr = (char*)malloc(strLen + 1);
//        if (!pStr)
//        {
//            return NULL;
//        }
//        memcpy(pStr, jBuf, strLen);
//        pStr[strLen] = 0;
//    }
//    (*env)->ReleaseByteArrayElements(env, byteArray, jBuf, 0);
//    return pStr;
//}
//
//jstring stoJstring(JNIEnv* env, const char* pat)
//{
//    jclass strClass = (*env)->FindClass(env, "Ljava/lang/String;");
//    jmethodID ctorID = (*env)->GetMethodID(env, strClass, "<init>", "([BLjava/lang/String;)V");
//    jbyteArray bytes = (*env)->NewByteArray(env, strlen(pat));
//    (*env)->SetByteArrayRegion(env, bytes, 0, strlen(pat), (jbyte*)pat);
//    jstring encoding = (*env)->NewStringUTF(env,"utf-8");
//    return (jstring)(*env)->NewObject(strClass, ctorID, bytes, encoding);
//}
//
//JNIEXPORT jstring JNICALL Java_com_hybunion_hrtpayment_utils_EncryptionData_calculatePWD
//        (JNIEnv *env, jclass object, jstring pan, jstring pwd){
//
////    void *handle;
////    int (*call_method)(int a, int b);
////    LOGV("abc = %s\n",  "hahaha");
////    handle = dlopen("libjnitest.so", RTLD_LAZY);
////    if(0 == handle){
////        LOGV("handle = null\n");
////        return  (*env)->NewStringUTF(env,"hello world");
////    }else{
////        LOGV("handle is not null\n");
////    }
////
////    call_method =  dlsym(handle, "add");
////
////    LOGV("result = %d", call_method(1, 2));
////    return  (*env)->NewStringUTF(env,"hello world");
//
////    // First get the class that contains the method you need to call
//    jclass clazz = (*env)->FindClass(env, "com/hybunion/hrtpayment/utils/DESUtil");
//    if(clazz == NULL){
//        LOGD("class error!");
//        return  (*env)->NewStringUTF(env,"hello world");
//    }
//    // public static SharedPreferencesUtil getInstance(Context context)
//    jmethodID instance = (*env)->GetStaticMethodID(env, clazz, "des3EncodePwd", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
//    jmethodID instance1 = (*env)->GetStaticMethodID(env, clazz, "test", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
//
//    //public String getKey(String key)
////    jmethodID getKey = (*env)->GetMethodID(env, clazz, "getKey", "(Ljava/lang/String;)Ljava/lang/String;");
//
//    jstring masterKey = (jstring)(*env)->CallStaticObjectMethod(env, clazz, instance, "123456", "1234567890123456",
//                                                          "68336F3474356A2425464026265E4079",
//                                                          "68336F3474356A2425464026265E4079");
////    jstring masterKey = (jstring)(*env)->CallStaticObjectMethod(env, clazz, instance1);
////jthrowable  ex;
////    ex = (*env)->ExceptionOccurred(env);
////    if(0 != ex){
////        jclass cls;
////        cls = ( *env)->FindClass(env,"NumberNotFounded");
////        if(cls == NULL){
////            return "";
////        }
////        (*env)->ThrowNew(env,cls,"code from C");
////    }
//  //  LOGD("masterkey = %s", jstringTostr(env, masterKey));
//    return  masterKey;
//}
//
//JNIEXPORT void JNICALL Java_com_hybunion_hrtpayment_utils_EncryptionData_saveKeys
//        (JNIEnv *env, jclass object, jstring masterKey, jstring pinKey, jstring macKey){
//
//}
//
//JNIEXPORT jstring JNICALL Java_com_hybunion_hrtpayment_utils_EncryptionData_encode3Des
//        (JNIEnv *env, jclass object, jstring data, jstring key){
//    char dest[100];
//    memset(dest, 0, 100);
//    encode(jstringTostr(env, data), dest);
//    LOGD("dest = %s", dest);
//
//}
//
//
//
//
//
//
