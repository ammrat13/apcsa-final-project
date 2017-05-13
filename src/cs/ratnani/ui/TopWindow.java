package cs.ratnani.ui;

import javax.swing.*;
import java.awt.*;


/**
 * This class represents the top level window / main window. It contains all
 * the other components, and is the main ui for this application.
 *
 * @author Ammar Ratnani
 * @version 0.0
 */
public class TopWindow extends JFrame {

    // Constants: --------------------------------------------------------------

    //About text:
    private final String ABOUT_TEXT =
            "apcsa-final-project\n" +
                    "This is an interactive plotter for complex valued " +
                    "functions of complex numbers.";


    // Constructors: -----------------------------------------------------------

    public TopWindow(String n){
        super(n);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(700,700);
        this.setLayout(new BorderLayout());

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

        // Private Variables: --------------------------------------------------

        private JTextField funcField;


        // Constructors: -------------------------------------------------------

        public FuncBar(){
            this.setLayout(new GridBagLayout());

            // This consists of a JLabel and a JTextField
            // JLabel
            JLabel funcLabel = new JLabel("f(z) = ");
            GridBagConstraints funcLabelC = new GridBagConstraints();
            funcLabelC.gridx = 0;
            funcLabelC.gridy = 0;
            funcLabelC.insets = new Insets(10,10,10,10);
            funcLabelC.anchor = GridBagConstraints.LINE_START;
            this.add(funcLabel, funcLabelC);

            // JTextField
            funcField = new JTextField(9999);
            GridBagConstraints funcFieldC = new GridBagConstraints();
            funcFieldC.gridx = 1;
            funcFieldC.gridy = 0;
            funcFieldC.fill = GridBagConstraints.HORIZONTAL;
                // Necessary to tell it to fill due to its weight
            funcFieldC.insets = new Insets(10,0,10,10);
            funcFieldC.weightx = 1.0;
                // Necessary to make the bar span to the end of the window
            this.add(funcField, funcFieldC);

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
     * number being pointed to
     */
    public class MidPanel extends JPanel{

        // Constructors: -------------------------------------------------------

        public MidPanel(){
            this.setLayout(new GridBagLayout());

            // We add the plot and the info boxes to the layout
            GridBagConstraints plotC = new GridBagConstraints();
            plotC.gridx = 0;
            plotC.gridy = 0;
            plotC.gridwidth = 2;
            plotC.fill = GridBagConstraints.BOTH;
            plotC.weighty = .6;
            plotC.weightx = 1.0;
            plotC.insets = new Insets(10,10,10,10);
            this.add(new Plot(), plotC);
        }


        // Public classes: -----------------------------------------------------

        /**
         * This class contains the plot of the function inputted.
         */
        public class Plot extends JPanel{

            // Private Variables: ----------------------------------------------

            private JTextField imUp;
            private JTextField imDo;
            private JTextField reUp;
            private JTextField reDo;

            private JPanel plot;


            // Constructors: ---------------------------------------------------

            public Plot(){
                this.setLayout(new GridBagLayout());

                // We have four JTextFields surrounding a central plot
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


                plot = new JPanel();
                plot.setSize(new Dimension(300,300));
                plot.setBackground(Color.BLACK);
                GridBagConstraints plotC = new GridBagConstraints();
                plotC.gridx = 1;
                plotC.gridy = 1;
                plotC.fill = GridBagConstraints.BOTH;
                plotC.weightx = 1.0;
                plotC.weighty = 1.0;
                plotC.insets = new Insets(10,10,10,10);
                this.add(plot, plotC);
            }

        }

    }

}
