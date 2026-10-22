package com.tdm.audiowire;

public class AudioPlayerTest {
    public static void main(String[] args) throws Exception {
        AutovoicePlayer player = new AutovoicePlayer();
        player.play("audio/pre.wav");
        System.out.println("Pre-scan audio finished.");
    }
}
