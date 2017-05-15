package cs.ratnani.ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


/**
 * A helper class for the UI that contains utility functions.
 *
 * @author Ammar Ratnani
 * @version 0.0
 */
public class UIHelper {

    /**
     * Read a file and return its contents.
     *
     * @param n The name of the file to read
     * @return The file's contents
     */
    public static String readFile(String n){
        try{
            String ret = "";
            Scanner fScan = new Scanner(new File(n));
            // Read each line
            while(fScan.hasNextLine())
                ret += fScan.nextLine();
            return ret;
        } catch(IOException e){
            e.printStackTrace();
            // Return an error string to be displayed
            return "Error: File '" + n + "' not found";
        }
    }

    /**
     * Plays a sound without blocking. Must be a wav file. Do as demonstrated
     * on StackOverflow:
     * http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
     *
     * @param n The name of the wav file to play
     */
    public static void playSoundNonBlocking(String n){
        // Non-blocking
        new Thread(() -> {
            try {
                File soundFile = new File(n);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } catch(IOException | UnsupportedAudioFileException | LineUnavailableException e){
                e.printStackTrace();
            }
        }).start();
    }
}
