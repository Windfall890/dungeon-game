package com.jsaop.dungeonGame.gui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Sounds {
    public MediaPlayer ambiance;
    public AudioClip doorClose;
    public AudioClip yay;
    public AudioClip ping;

    public Sounds() {
        initMediaPlayer();
        initSounds();
    }


    private void initMediaPlayer() {

        String resource = loadResource("Ambient Cave-SoundBible.com-2124899044.wav");
        Media sound = new Media(resource);
        ambiance = new MediaPlayer(sound);
        ambiance.setOnEndOfMedia(() -> {
            ambiance.seek(Duration.ZERO);
            ambiance.play();
        });
    }

    private String loadResource(String file) {
        return this.getClass().getResource("/" + file).toString();
    }

    private void initSounds() {
        doorClose = new AudioClip(loadResource("Big_door_closed-Clemens_F-941522533.wav"));
        yay = new AudioClip(loadResource("1_person_cheering-Jett_Rifkin-1851518140.wav"));
        ping = new AudioClip(loadResource("Sonar-SoundBible.com-354002976.wav"));
    }
}
