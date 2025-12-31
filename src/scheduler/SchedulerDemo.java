package scheduler;

import javax.swing.*;

/**
 * CPU Task Scheduling Using Greedy Algorithms - Demo Application
 * DAA Assignment: Design and Analysis of Algorithms
 * 
 * This class launches the GUI application that demonstrates greedy-based
 * CPU scheduling algorithms in a real-world operating system context.
 * 
 * The application automatically loads sample tasks representing realistic
 * operating system processes (Browser, System, Media, Editor, Backup)
 * to demonstrate how greedy algorithms optimize CPU resource allocation.
 */
public class SchedulerDemo {
    
    public static void main(String[] args) {
        // Launch the GUI application
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new CPUSchedulerGUI().setVisible(true);
        });
    }
}

