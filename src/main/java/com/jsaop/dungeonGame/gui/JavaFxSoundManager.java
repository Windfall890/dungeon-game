package com.jsaop.dungeonGame.gui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class JavaFxSoundManager implements ISoundManager {
    private MediaPlayer ambiance;
    private AudioClip doorClose;
    private AudioClip gameWin;
    private AudioClip gameOver;
    private AudioClip ping;

    //enemy
    private AudioClip beep;
    private AudioClip boop;
    private AudioClip snap;

    private Random random = new Random();

    public JavaFxSoundManager() {
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

        //enemy
        beep = new AudioClip(loadResource("enemy/Robot_blip-Marianne_Gagnon-120342607.wav"));
        boop = new AudioClip(loadResource("enemy/Robot_blip_2-Marianne_Gagnon-299056732.wav"));
        snap = new AudioClip(loadResource("enemy/take_picture-Ellie-155921267.wav"));
    }

    private String loadResource(String file) {
        return this.getClass().getResource("/" + file).toString();
    }

    @Override
    public void playerSpotted() {

        double rate = 0.6 + (0.8 - 0.6) * random.nextDouble();
        if (random.nextDouble() > 0.5) {
            beep.play(1,1,rate,1,1);
        } else boop.play(1,1,rate,1,1);
    }

    @Override
    public void soundTest() {

        //hahaha wtf java
        Arrays.stream(this.getClass().getDeclaredFields()).filter(f -> f.getType() == AudioClip.class)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);

                        // obtain the field value from the object instance
                        Object fieldValue = field.get(this);

                        // get declared method
                        Method myMethod = fieldValue.getClass().getDeclaredMethod("play");

                        // invoke method on the instance of the field from yor object instance
                        myMethod.invoke(fieldValue);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

    }

    @Override
    public void radarPing() {
        ping.play(0.5);
    }

    @Override
    public void playAmbiance() {
        ambiance.play();
    }

    @Override
    public void pauseAmbiance() {
        ambiance.pause();
    }

    @Override
    public void gameOver() {
        gameOver.play();
    }

    @Override
    public void gameWin() {
        gameWin.play();
    }

    @Override
    public void levelTransition() {
        doorClose.play();
    }

    @Override
    public void attack() {
        snap.play();
    }
}
