package cs.ratnani.ui;

import cs.ratnani.math.ComplexMath;

import javax.imageio.ImageIO;
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

    // Constants: --------------------------------------------------------------

    // For the images we save
    private static final int EXPORT_HEIGHT = 2048;


    // Public Methods: ---------------------------------------------------------

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

    /**
     * Writes a plot to a passed file object, or overwrites if it exists, as a
     * "png".
     *
     * @param F The file to write
     * @param f The function to plot
     * @param reUp The upper bound on the real axis
     * @param reDo The lower bound on the real axis
     * @param imUp The upper bound on the imaginary axis
     * @param imDo The lower bound on the imaginary axis
     */
    public static void writePlotTo(File F, String f, double reUp, double reDo,
                                   double imUp, double imDo){
        int exportWidth = (int) (EXPORT_HEIGHT * (reUp-reDo) / (imUp - imDo));
            // Scale `exportWidth` so it matches the aspect ratio of the bounds

        // Non-blocking
        new Thread(
                () -> {
                    try {
                        ImageIO.write(
                                ComplexMath.plot(
                                        f,
                                        reUp,
                                        reDo,
                                        imUp,
                                        imDo,
                                        exportWidth,
                                        EXPORT_HEIGHT
                                ),
                                "png",
                                F
                        );
                    } catch (IOException e){
                        e.printStackTrace();
                        playSoundNonBlocking(TopWindow.ERROR_SOUND_PATH);
                    }
                }
        ).start();
    }

}
