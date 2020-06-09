package com.hybunion.yirongma.payment.inteface;

/**
 * @author SunBingbing
 * @date 2017/5/22
 * @email freemars@yeah.net
 * @description 百度 TTS 结果回调（用于分析问题）
 */

public interface ITTSCallback {

    void onSynthesizeStart(String s); // 开始授权

    void onSynthesizeDataArrived(String s, byte[] bytes, int i);

    void onSynthesizeFinish(String s); // 结束授权

    void onSpeechStart(String s); // 开始朗读

    void onSpeechProgressChanged(String s, int i); // 朗读中

    void onSpeechFinish(String s); // 结束朗读

    void onError(String s); // 出错
}
