package com.tdm.audiowire;

public class AutovoiceMessageSet {
    private final String group;
    private final String key;
    private final String preWav;
    private final String postWav;
    private final double speed;

    public AutovoiceMessageSet(String group, String key, String preWav, String postWav, double speed) {
        this.group = group;
        this.key = key;
        this.preWav = preWav;
        this.postWav = postWav;
        this.speed = speed;
    }

    public String getGroup() {
        return group;
    }

    public String getKey() {
        return key;
    }

    public String getPreWav() {
        return preWav;
    }

    public String getPostWav() {
        return postWav;
    }

    public double getSpeed() {
        return speed;
    }
}
