package cs.ratnani.ui;

import cs.ratnani.math.Complex;
import cs.ratnani.math.ComplexMath;
import cs.ratnani.util.TriggerList;
import cs.ratnani.util.TriggerListener;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * This class represents the top level window / main window. It contains all
 * the other components, and is the main ui for this application. Its subclasses
 * are all of its components.
 *
 * @author Ammar Ratnani
 * @version 0.0
 */
public class TopWindow extends JFrame {

    // Constants: --------------------------------------------------------------

    private final String ABOUT_TEXT_PATH = "src\\about.txt";
    private final String ABOUT_TEXT;

    private final String ERROR_SOUND_FILE_PATH = "src\\error.wav";


    // Private Variables: ------------------------------------------------------

    // We put these here as their values are needed thorough-out the UI
    private JTextField funcField;
    private JTextField reUp;
    private JTextField reDo;
    private JTextField imUp;
    private JTextField imDo;

    // For when the "Plot" button is clicked
    private TriggerList plotList = new TriggerList();


    // Constructors: -----------------------------------------------------------

    public TopWindow(String n){
        // Set the window title to the name passed
        super(n);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(700,700);
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            // So it starts full-screen

        // Read the about page
        String abtTmp = "";
        try {
            Scanner abtScanner = new Scanner(new File(ABOUT_TEXT_PATH));
            while(abtScanner.hasNextLine())
                abtTmp += abtScanner.nextLine() + "\n";
        } catch(FileNotFoundException e){
            // Do Nothing
        } finally {
            // Assigns to the file contents if it exists, empty string otherwise
            ABOUT_TEXT = abtTmp;
        }

        // Add all the components
        this.add(new FuncBar(), BorderLayout.PAGE_START);
        this.add(new ButtonBar(), BorderLayout.PAGE_END);
        this.add(new MidPanel(), BorderLayout.CENTER);
    }


    // Public Classes: ---------------------------------------------------------

    /**
     * This class is the function input bar that appears at the top of the
     * window.
     */
    public class FuncBar extends JPanel{


        // Constructors: -------------------------------------------------------

        public FuncBar(){
            this.setLayout(new GridBagLayout());

            // This consists of a JLabel, a JButton, and a JTextField

            // JLabel
            JLabel funcLabel = new JLabel("f(z) = ");
            GridBagConstraints funcLabelC = new GridBagConstraints();
            funcLabelC.gridx = 0;
            funcLabelC.gridy = 0;
            funcLabelC.insets = new Insets(10,10,10,10);
            funcLabelC.anchor = GridBagConstraints.LINE_START;
            this.add(funcLabel, funcLabelC);

            // JButton
            JButton plotButton = new JButton("Plot...");
            plotButton.addActionListener(
                    e -> {
                        // Error checking
                        try{
                            // Try to cause an exception
                            Double.parseDouble(reUp.getText());
                            Double.parseDouble(reDo.getText());
                            Double.parseDouble(imUp.getText());
                            Double.parseDouble(imDo.getText());
                            ComplexMath.parsePostfix(funcField.getText(),new Complex());

                            // If none of those caused an error, we're good to
                            //  go
                            plotList.trigger();
                        } catch(IllegalArgumentException f){
                            // Play a sound
                            new Thread(() -> errorSound()).start();
                        }
                    }
            );
            GridBagConstraints plotButtonC = new GridBagConstraints();
            plotButtonC.gridx = 2;
            plotButtonC.gridy = 0;
            plotButtonC.insets = new Insets(10,10,10,10);
            plotButtonC.anchor = GridBagConstraints.LINE_END;
            this.add(plotButton, plotButtonC);

            // JTextField
            funcField = new JTextField(Integer.MAX_VALUE);
                // No bound on the columns
            GridBagConstraints funcFieldC = new GridBagConstraints();
            funcFieldC.gridx = 1;
            funcFieldC.gridy = 0;
            funcFieldC.fill = GridBagConstraints.HORIZONTAL;
            funcFieldC.weightx = 1.0;
            funcFieldC.insets = new Insets(10,0,10,0);
                // No margin on the left or right as the label and button
                //  already have it
            this.add(funcField, funcFieldC);

        }


        // Private Methods: ----------------------------------------------------

        /**
         * Plays an error sound.
         */
        private void errorSound() {
            try {
                File soundFile = new File(ERROR_SOUND_FILE_PATH);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } catch(IOException | UnsupportedAudioFileException | LineUnavailableException e){
                // Do nothing
            }
        }
    }

    /**
     * This class contains the buttons that appear at the bottom of the page.
     */
    public class ButtonBar extends JPanel{

        // Constructors: -------------------------------------------------------

        public ButtonBar(){
            // This bar consists of an About and Export button aligned to the
            //  right
            this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

            // TODO: Add export functionality
            JButton export = new JButton("Export...");
            this.add(export);

            JButton about = new JButton("About...");
            // ActionListeners for buttons handle clicks
            about.addActionListener(
                    e -> JOptionPane.showMessageDialog(null,
                            ABOUT_TEXT,
                            "About",
                            JOptionPane.PLAIN_MESSAGE
                    )
            );
            this.add(about);
        }

    }

    /**
     * This class contains the middle part of the program and is the largest
     * part. It contains the plot and additional information about the complex
     * number being pointed to. It has subclasses for each of its components.
     */
    public class MidPanel extends JPanel{

        // Constructors: -------------------------------------------------------

        public MidPanel(){
            this.setLayout(new GridBagLayout());

            // We add the plot to the layout
            GridBagConstraints plotC = new GridBagConstraints();
            plotC.gridx = 0;
            plotC.gridy = 0;
            plotC.gridheight = 2;
                // The info boxes each take one row
            plotC.fill = GridBagConstraints.BOTH;
            plotC.weighty = 1.0;
            plotC.weightx = .6;
            plotC.insets = new Insets(10,10,10,10);
            this.add(new Plot(), plotC);

            // TODO: Add Info Boxes
        }


        // Public classes: -----------------------------------------------------

        /**
         * This class contains the plot of the function inputted.
         */
        public class Plot extends JPanel{

            // Constructors: ---------------------------------------------------

            public Plot(){
                this.setLayout(new GridBagLayout());

                // We have four JTextFields surrounding a central plot
                // We make all of them instance variables as we will have to
                //  access them later.
                imUp = new JTextField(5);
                imUp.setText("15");
                imUp.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints imUpC = new GridBagConstraints();
                imUpC.gridx = 1;
                imUpC.gridy = 0;
                imUpC.anchor = GridBagConstraints.PAGE_START;
                this.add(imUp, imUpC);

                imDo = new JTextField(5);
                imDo.setText("-15");
                imDo.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints imDoC = new GridBagConstraints();
                imDoC.gridx = 1;
                imDoC.gridy = 2;
                imDoC.anchor = GridBagConstraints.PAGE_END;
                this.add(imDo, imDoC);

                reUp = new JTextField(5);
                reUp.setText("15");
                reUp.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints reUpC = new GridBagConstraints();
                reUpC.gridx = 2;
                reUpC.gridy = 1;
                reUpC.anchor = GridBagConstraints.LINE_END;
                this.add(reUp, reUpC);

                reDo = new JTextField(5);
                reDo.setText("-15");
                reDo.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints reDoC = new GridBagConstraints();
                reDoC.gridx = 0;
                reDoC.gridy = 1;
                reDoC.anchor = GridBagConstraints.LINE_START;
                this.add(reDo, reDoC);


                JPanel plot = new PlotArea();
                plot.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                GridBagConstraints plotC = new GridBagConstraints();
                plotC.gridx = 1;
                plotC.gridy = 1;
                plotC.fill = GridBagConstraints.BOTH;
                plotC.weightx = 1.0;
                plotC.weighty = 1.0;
                plotC.insets = new Insets(10,10,10,10);
                this.add(plot, plotC);
            }


            // Subclasses: -----------------------------------------------------

            public class PlotArea extends JPanel implements TriggerListener {

                // Constants: --------------------------------------------------

                private final int CIRCLE_RADIUS = 15;


                // Private Variables: ------------------------------------------

                private String currentFunc = null;
                private boolean funcChanged = false;
                private BufferedImage currentImage = null;

                // To check if we have resized
                private int lastWidth;
                private int lastHeight;

                // To store the values until the user hits "Plot"
                private double reUpT;
                private double reDoT;
                private double imUpT;
                private double imDoT;

                // Store the last complex number the user clicked on
                private Complex lastPointed = new Complex(0,0);


                // Constructors: -----------------------------------------------

                public PlotArea() {
                    // We want to know when the function changed
                    plotList.add(this);

                    lastWidth = this.getWidth();
                    lastHeight = this.getHeight();

                    this.addMouseMotionListener(new MouseMotionListener() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            // Update lastPointed to what the user clicked on
                            lastPointed = new Complex(
                                    ComplexMath.numAtC(
                                            e.getX(),
                                            lastWidth,
                                            reUpT,
                                            reDoT
                                    ),
                                    ComplexMath.numAtR(
                                            e.getY(),
                                            lastHeight,
                                            imUpT,
                                            imDoT
                                    )
                            );
                            repaint();
                        }

                        @Override
                        public void mouseMoved(MouseEvent e) { /* Do Nothing */ }
                    });
                }


                // Public Methods: ---------------------------------------------

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    super.paintComponent(g2d);

                    // If we have a function
                    if (currentFunc != null) {
                        // Only recompute the image if the function has changed,
                        //  or the window size has
                        if(funcChanged
                                || this.getWidth() != lastWidth
                                || this.getHeight() != lastHeight){
                            currentImage = ComplexMath.plot(
                                    currentFunc,
                                    reUpT,
                                    reDoT,
                                    imUpT,
                                    imDoT,
                                    this.getWidth(),
                                    this.getHeight()
                            );

                            // Update the width and height
                            lastWidth = this.getWidth();
                            lastHeight = this.getHeight();
                            funcChanged = false;
                        }

                        g2d.drawImage(
                                currentImage,
                                0,
                                0,
                                null
                        );

                        // If the user has pointed to a complex number, draw a
                        //  red circle there and a green circle at the
                        //  function's value at the number the user pointed to
                        if(lastPointed != null){
                            // Compute the r and c of `lastPointed`
                            int c = ComplexMath.colOf(lastPointed, this.getWidth(), reUpT, reDoT);
                            int r = ComplexMath.rowOf(lastPointed, this.getHeight(), imUpT, imDoT);

                            // Draw a red circle at `lastPointed`
                            g.setColor(Color.RED);
                            g.fillOval(
                                    c - (CIRCLE_RADIUS/2),
                                    r - (CIRCLE_RADIUS/2),
                                    CIRCLE_RADIUS,
                                    CIRCLE_RADIUS
                            );

                            // Compute f(`lastPointed`)
                            Complex res = ComplexMath.parsePostfix(currentFunc,lastPointed);

                            // Compute its r and c
                            int cf = ComplexMath.colOf(res, this.getWidth(), reUpT, reDoT);
                            int rf = ComplexMath.rowOf(res, this.getHeight(), imUpT, imDoT);

                            // Draw a green circle at f(`lastPointed`)
                            g.setColor(Color.GREEN);
                            g.fillOval(
                                    cf - (CIRCLE_RADIUS/2),
                                    rf - (CIRCLE_RADIUS/2),
                                    CIRCLE_RADIUS,
                                    CIRCLE_RADIUS
                            );
                        }
                    } else {
                        // Fill it with gray if there is no function
                        g2d.setColor(Color.GRAY);
                        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                }

                public void onTrigger() {
                    // Set the function to the one in the box
                    currentFunc = funcField.getText();
                    funcChanged = true;

                    // Update the bounds
                    reUpT = Double.parseDouble(reUp.getText());
                    reDoT = Double.parseDouble(reDo.getText());
                    imUpT = Double.parseDouble(imUp.getText());
                    imDoT = Double.parseDouble(imDo.getText());

                    this.repaint(); // with the new function
                }

            }

        }

    }

}
