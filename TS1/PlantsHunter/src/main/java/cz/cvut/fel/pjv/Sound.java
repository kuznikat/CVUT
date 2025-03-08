package cz.cvut.fel.pjv;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;


    public Sound(){
        soundURL[0] = getClass().getResource("/music/back1.wav");
        soundURL[1] = getClass().getResource("/music/laser.wav");
        soundURL[2] = getClass().getResource("/music/water.wav");
        soundURL[3] = getClass().getResource("/music/drink_soda.wav");
        soundURL[4] = getClass().getResource("/music/alient.wav");
        soundURL[5] = getClass().getResource("/music/finish.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(Exception e){
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}
