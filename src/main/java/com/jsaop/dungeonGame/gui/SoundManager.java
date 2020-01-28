package com.jsaop.dungeonGame.gui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundManager {
    public MediaPlayer ambiance;
    public AudioClip doorClose;
    public AudioClip gameWin;
    public AudioClip gameOver;
    public AudioClip ping;

    public SoundManager() {
        initMediaPlayer();
        initSounds();
    }


    private void initMediaPlayer() {

        Media sound = new Media(loadResource("Ambient Cave-SoundBible.com-2124899044.wav"));
        ambiance = new MediaPlayer(sound);
        ambiance.setOnEndOfMedia(() -> {
            ambiance.seek(Duration.ZERO);
            ambiance.play();
        });
    }

    private void initSounds() {
        doorClose = new AudioClip(loadResource("Big_door_closed-Clemens_F-941522533.wav"));
        gameWin = new AudioClip(loadResource("1_person_cheering-Jett_Rifkin-1851518140.wav"));
        gameOver = new AudioClip(loadResource("Ambiance-SoundBible.com-1535680949.wav"));
        ping = new AudioClip(loadResource("Sonar-SoundBible.com-354002976.wav"));
    }

    private String loadResource(String file) {
        return this.getClass().getResource("/" + file).toString();
    }
}
