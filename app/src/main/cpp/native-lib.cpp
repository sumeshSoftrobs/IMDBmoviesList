#include <jni.h>
#include <string>



extern "C"
JNIEXPORT jstring JNICALL
Java_com_softrobs_imdb_1movies_1list_di_Module_getBaseUrl(JNIEnv *env, jobject thiz) {
    std::string base_url = "https://omdbapi.com";
        return env->NewStringUTF(base_url.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_softrobs_imdb_1movies_1list_ui_activities_MainActivity_getAPIKey(JNIEnv *env,
                                                                          jobject thiz) {
    std::string api_key = "e9647eaa";
    return env->NewStringUTF(api_key.c_str());
}