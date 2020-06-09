package com.hybunion.yirongma.valuecard.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/1.
 */
public class CardRuleBean implements Serializable {
    private String chong;
    private String song;

    public String getChong() {
        return chong;
    }

    public void setChong(String chong) {
        this.chong = chong;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
}
