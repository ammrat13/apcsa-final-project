package cs.ratnani.ui;

import cs.ratnani.math.Complex;
import cs.ratnani.math.ComplexMath;
import cs.ratnani.util.TriggerList;
import cs.ratnani.util.TriggerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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

    // TODO: Remember to ship with the `res` directory
    private static final String ABOUT_TEXT_PATH = "res\\about.txt";
    private final String ABOUT_TEXT;

    // For `UIHelper`, make it package-private
    static final String ERROR_SOUND_PATH = "res\\error.wav";


    // Private Variables: ------------------------------------------------------

    // We put these here as their values are needed thorough-out the UI
    private JTextField funcField;
    private JTextField reUpField;
    private JTextField reDoField;
    private JTextField imUpField;
    private JTextField imDoField;

    // To store the values for each text box, to be set when the user hits
    //  "Plot"
    private String currentFunc;
    private boolean funcOrBoundsChanged;
    private double reUpT;
    private double reDoT;
    private double imUpT;
    private double imDoT;

    // Store the last complex number the user clicked on
    private Complex lastPointed = new Complex();

    // For when "repaint()" needs to be called on the plot area
    private final TriggerList plotList = new TriggerList();

    // For when the user points to a new number
    private final TriggerList pointList = new TriggerList();


    // Constructors: -----------------------------------------------------------

    public TopWindow(String n){
        // Set the window title to the name passed
        super(n);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1000,600));
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            // So it starts full-screen

        // Read the about page
        ABOUT_TEXT = UIHelper.readFile(ABOUT_TEXT_PATH);

        // Add all the components
        this.add(new FuncBar(), BorderLayout.PAGE_START);
        this.add(new ButtonBar(), BorderLayout.PAGE_END);
        this.add(new MidPanel(), BorderLayout.CENTER);
    }


    // Private Methods: --------------------------------------------------------

    /**
     * Updates the bounds and current function if it is valid.
     *
     * @throws IllegalArgumentException If the bounds or function is invalid
     */
    private void updateBoundsAndFunction() throws IllegalArgumentException{
        // Check if function is valid by testing a point
        ComplexMath.parsePostfix(funcField.getText(),new Complex());
        // Try to cause an exception
        reUpT = Double.parseDouble(reUpField.getText());
        reDoT = Double.parseDouble(reDoField.getText());
        imUpT = Double.parseDouble(imUpField.getText());
        imDoT = Double.parseDouble(imDoField.getText());

        // If none of those caused an error, we're good to
        //  go
        currentFunc = funcField.getText();
        funcOrBoundsChanged = true;
    }


    // Subclasses: -------------------------------------------------------------

    /**
     * This class is the function input bar that appears at the top of the
     * window.
     */
    private class FuncBar extends JPanel{


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
            // OnClick
            plotButton.addActionListener(
                    e -> {
                        // Error checking
                        try{
                            updateBoundsAndFunction();
                            // If it worked
                            plotList.trigger();
                        } catch(IllegalArgumentException f){
                            f.printStackTrace();
                            // Play a sound
                            UIHelper.playSoundNonBlocking(ERROR_SOUND_PATH);
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

    }

    /**
     * This class contains the buttons that appear at the bottom of the page.
     */
    private class ButtonBar extends JPanel{

        // Constructors: -------------------------------------------------------

        public ButtonBar(){
            // This bar consists of an About and Export button aligned to the
            //  right
            this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

            JButton export = new JButton("Export...");
            export.addActionListener(
                    e -> {
                        try {
                            updateBoundsAndFunction();

                            // Get file from user
                            JFileChooser fileChooser = new JFileChooser();
                            int res = fileChooser.showSaveDialog(null);
                            if(res == JFileChooser.APPROVE_OPTION){
                                File out = fileChooser.getSelectedFile();
                                UIHelper.writePlotTo(
                                        out,
                                        currentFunc,
                                        reUpT,
                                        reDoT,
                                        imUpT,
                                        imDoT
                                );
                                // It will re-plot anyway when we have to call
                                //  `repaint()`, may as well do it now
                                plotList.trigger();
                            } else {
                                throw new IOException();
                            }
                        } catch(IllegalArgumentException | IOException f){
                            f.printStackTrace();
                            // Error sound
                            UIHelper.playSoundNonBlocking(ERROR_SOUND_PATH);
                        }
                    }
            );
            this.add(export);


            JButton about = new JButton("About...");
            // OnClick
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
    private class MidPanel extends JPanel{

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

            // Input info box
            GridBagConstraints inpC = new GridBagConstraints();
            inpC.gridx = 1;
            inpC.gridy = 0;
            inpC.fill = GridBagConstraints.BOTH;
            inpC.weighty = .5;
            inpC.weightx = .4;
            inpC.insets = new Insets(10,10,10,10);
            this.add(new InputInfoBox(), inpC);

            // Output info box
            GridBagConstraints outC = new GridBagConstraints();
            outC.gridx = 1;
            outC.gridy = 1;
            outC.fill = GridBagConstraints.BOTH;
            outC.weighty = .5;
            outC.weightx = .4;
            outC.insets = new Insets(10,10,10,10);
            this.add(new OutputInfoBox(), outC);
        }


        // Subclasses: ---------------------------------------------------------

        /**
         * This class contains the plot of the function inputted.
         */
        private class Plot extends JPanel{

            // Constructors: ---------------------------------------------------

            public Plot(){
                this.setLayout(new GridBagLayout());

                // We have four JTextFields surrounding a central plot
                // We make all of them instance variables as we will have to
                //  access them in the `FuncBar` class.
                imUpField = new JTextField(5);
                imUpField.setText("15");
                imUpField.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints imUpC = new GridBagConstraints();
                imUpC.gridx = 1;
                imUpC.gridy = 0;
                imUpC.anchor = GridBagConstraints.PAGE_START;
                this.add(imUpField, imUpC);

                imDoField = new JTextField(5);
                imDoField.setText("-15");
                imDoField.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints imDoC = new GridBagConstraints();
                imDoC.gridx = 1;
                imDoC.gridy = 2;
                imDoC.anchor = GridBagConstraints.PAGE_END;
                this.add(imDoField, imDoC);

                reUpField = new JTextField(5);
                reUpField.setText("15");
                reUpField.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints reUpC = new GridBagConstraints();
                reUpC.gridx = 2;
                reUpC.gridy = 1;
                reUpC.anchor = GridBagConstraints.LINE_END;
                this.add(reUpField, reUpC);

                reDoField = new JTextField(5);
                reDoField.setText("-15");
                reDoField.setHorizontalAlignment(JTextField.CENTER);
                GridBagConstraints reDoC = new GridBagConstraints();
                reDoC.gridx = 0;
                reDoC.gridy = 1;
                reDoC.anchor = GridBagConstraints.LINE_START;
                this.add(reDoField, reDoC);


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

            /**
             * The actual area where the plot is displayed
             */
            private class PlotArea extends JPanel implements TriggerListener {

                // Constants: --------------------------------------------------

                private final int CIRCLE_RADIUS = 10;


                // Private Variables: ------------------------------------------

                private BufferedImage currentImage = null;
                private boolean imageReady = false;

                // To check if we have re-sized
                private int lastWidth;
                private int lastHeight;

                // Thread to plot in the background
                Thread backgroundPlot;


                // Constructors: -----------------------------------------------

                public PlotArea() {
                    // We want to know when the function changed
                    plotList.add(this);

                    lastWidth = this.getWidth();
                    lastHeight = this.getHeight();

                    this.addMouseMotionListener(new MouseMotionListener() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            // Update lastPointed to what the user was on
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
                            pointList.trigger();
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

                    // If its not our first time
                    if (currentFunc != null) {
                        // Only recompute the image if the function has changed,
                        //  or the window size has
                        if(funcOrBoundsChanged
                                || this.getWidth() != lastWidth
                                || this.getHeight() != lastHeight){
                            // Update the width and height
                            lastWidth = this.getWidth();
                            lastHeight = this.getHeight();

                            funcOrBoundsChanged = false;
                            imageReady = false;

                            // Draw loading text
                            g2d.drawString(
                                    "Loading...",
                                    this.getWidth() / 2,
                                    this.getHeight() / 2
                            );

                            // If we are already plotting something, stop
                            if(backgroundPlot != null && backgroundPlot.isAlive())
                                // Yes, `stop()` is deprecated, but I don't care
                                backgroundPlot.stop();

                            // Plot in the background
                            backgroundPlot = new Thread(() -> {
                                currentImage = ComplexMath.plot(
                                        currentFunc,
                                        reUpT,
                                        reDoT,
                                        imUpT,
                                        imDoT,
                                        lastWidth,
                                        lastHeight
                                );
                                // When we are done, the image is ready, and
                                //  call `repaint()`
                                imageReady = true;
                                plotList.trigger();
                            });
                            backgroundPlot.start();
                        } // ENDIF: funcOrBoundsChanged

                        if(imageReady) {
                            g2d.drawImage(
                                    currentImage,
                                    0,
                                    0,
                                    null
                            );

                            // If the user has pointed to a complex number, draw a
                            //  red circle there and a green circle at the
                            //  function's value at the number the user pointed to1
                            // Compute the r and c of `lastPointed`
                            int c = ComplexMath.colOf(lastPointed, this.getWidth(), reUpT, reDoT);
                            int r = ComplexMath.rowOf(lastPointed, this.getHeight(), imUpT, imDoT);

                            // Draw a red circle at `lastPointed`
                            g2d.setColor(Color.RED);
                            g2d.fillOval(
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
                            g2d.setColor(Color.GREEN);
                            g2d.fillOval(
                                    cf - (CIRCLE_RADIUS/2),
                                    rf - (CIRCLE_RADIUS/2),
                                    CIRCLE_RADIUS,
                                    CIRCLE_RADIUS
                            );
                        } // ENDIF: imageReady
                    } else {
                        // Fill it with gray if it is our first time
                        g2d.setColor(Color.GRAY);
                        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                    }
                }

                public void onTrigger() {
                    this.repaint();
                }

            }

        }

        /**
         * This class contains the two boxes that have information on the input
         * of the function the user is plotting.
         */
        private class InputInfoBox extends JPanel implements TriggerListener{

            // Private Variables: ----------------------------------------------

            // We will need these later
            private final JLabel re = new JLabel("Re(z) = ");
            private final JLabel im = new JLabel("Im(z) = ");
            private final JLabel abs = new JLabel("|z| = ");
            private final JLabel arg = new JLabel("arg(z) = ");


            // Constructors: ---------------------------------------------------

            public InputInfoBox(){
                this.setLayout(new GridLayout(3,2,10,10));
                // Use compound border to get line and margins
                this.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(10,10,10,10)
                ));

                // Title label and labels for re, im, r, and theta
                // Title
                this.add(new JLabel("Input:"));

                // Placeholder
                this.add(new JPanel());

                // Set the label size so they don't resize
                re.setMinimumSize(new Dimension(100,-1));
                re.setPreferredSize(new Dimension(100,-1));
                im.setMinimumSize(new Dimension(100,-1));
                im.setPreferredSize(new Dimension(100,-1));
                abs.setMinimumSize(new Dimension(100,-1));
                abs.setPreferredSize(new Dimension(100,-1));
                arg.setMinimumSize(new Dimension(100,-1));
                arg.setPreferredSize(new Dimension(100,-1));

                // Add info labels
                this.add(re);
                this.add(abs);
                this.add(im);
                this.add(arg);

                // We want to know when the user points to a new point
                pointList.add(this);
            }


            // Public Methods: -------------------------------------------------

            public void onTrigger(){
                // Update the text on each of the parts to at most ten digits
                re.setText(String.format("Re(z) = %10.3f", lastPointed.getRe()));
                im.setText(String.format("Im(z) = %10.3f", lastPointed.getIm()));
                abs.setText(String.format("|z| = %10.3f", lastPointed.getAbs()));
                arg.setText(String.format("arg(z) = %10.3f", lastPointed.getArg()));
            }

        }

        /**
         * This class contains the two boxes that have information on the output
         * of the function the user is plotting. Its just like `InputInfoBox`,
         * but it calculates the output.
         *
         * @see InputInfoBox
         */
        private class OutputInfoBox extends JPanel implements TriggerListener{

            // Private Variables: ----------------------------------------------

            // We will need these later
            private final JLabel re = new JLabel("Re(z) = ");
            private final JLabel im = new JLabel("Im(z) = ");
            private final JLabel abs = new JLabel("|z| = ");
            private final JLabel arg = new JLabel("arg(z) = ");


            // Constructors: ---------------------------------------------------

            public OutputInfoBox(){
                this.setLayout(new GridLayout(3,2,10,10));
                // Use compound border to get line and margins
                this.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(10,10,10,10)
                ));

                // Title label and labels for re, im, r, and theta
                // Title
                this.add(new JLabel("Output:"));

                // Placeholder
                this.add(new JPanel());

                // Set the label size so they don't resize
                re.setMinimumSize(new Dimension(100,-1));
                re.setPreferredSize(new Dimension(100,-1));
                im.setMinimumSize(new Dimension(100,-1));
                im.setPreferredSize(new Dimension(100,-1));
                abs.setMinimumSize(new Dimension(100,-1));
                abs.setPreferredSize(new Dimension(100,-1));
                arg.setMinimumSize(new Dimension(100,-1));
                arg.setPreferredSize(new Dimension(100,-1));

                // Add info labels
                this.add(re);
                this.add(abs);
                this.add(im);
                this.add(arg);

                // We want to know when the user points to a new point
                pointList.add(this);
            }


            // Public Methods: -------------------------------------------------

            public void onTrigger(){
                // Calculate the result
                Complex res = ComplexMath.parsePostfix(currentFunc,lastPointed);

                // Update the text on each of the parts to ten digits max
                re.setText(String.format("Re(z) = %10.3f", res.getRe()));
                im.setText(String.format("Im(z) = %10.3f", res.getIm()));
                abs.setText(String.format("|z| = %10.3f", res.getAbs()));
                arg.setText(String.format("arg(z) = %10.3f", res.getArg()));
            }

        }

    }

}
