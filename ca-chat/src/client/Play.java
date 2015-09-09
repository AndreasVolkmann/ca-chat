/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Jonas
 */
public class Play {
    
    File Beep = new File("beepbeep.WAV");
    
    public void play()
    {
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Beep));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength()/1000);
        }
        catch(Exception e)
        {
            
        }
    }
    
}
