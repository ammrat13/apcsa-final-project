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
            JTextField funcField = new JTextField(9999);
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
                    e -> JOptionPane.showMessageDialog(null, ABOUT_TEXT)
            );
            this.add(about);
        }

    }

}
