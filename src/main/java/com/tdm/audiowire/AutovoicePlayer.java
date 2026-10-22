package com.tdm.audiowire;

import javax.sound.sampled.*;
import java.io.File;

public class AutovoicePlayer {
    public void play(String wavPath) throws Exception {
        File audioFile = new File(wavPath);
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            Clip clip = AudioSystem.getClip();
            try {
                clip.open(audioStream);
                clip.start();
                long durationMillis = clip.getMicrosecondLength() / 1000;
                Thread.sleep(durationMillis);
            } finally {
                clip.close();
            }
        }
    }
}

