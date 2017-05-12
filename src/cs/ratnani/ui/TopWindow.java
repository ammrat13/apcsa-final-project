package cs.ratnani.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * This class represents the top level window / main window. It contains all
 * the other components.
 *
 * @author Ammar Ratnani
 * @version 0.0
 */
public class TopWindow extends JFrame {

    // Constructors: -----------------------------------------------------------

    public TopWindow(String n){
        super(n);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setLayout(new BorderLayout());
    }

}
