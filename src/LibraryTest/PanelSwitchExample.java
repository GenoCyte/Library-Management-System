package LibraryTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelSwitchExample extends JFrame {

    JPanel mainPanel; // The parent container
    JPanel panel1, panel2;

    public PanelSwitchExample() {
        setTitle("Panel Switcher");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Use null layout at the top level to allow stacking panels manually
        mainPanel = new JPanel(null);
        setContentPane(mainPanel);

        // Create panels
        panel1 = createPanel1();
        panel2 = createPanel2();

        // Set bounds so they sit on top of each other
        panel1.setBounds(0, 0, 400, 300);
        panel2.setBounds(0, 0, 400, 300);

        // Add to main panel
        mainPanel.add(panel1);
        mainPanel.add(panel2);

        // Initial visibility
        panel1.setVisible(true);
        panel2.setVisible(false);
    }

    private JPanel createPanel1() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.CYAN);

        JLabel label = new JLabel("This is Panel 1");
        label.setBounds(130, 80, 150, 30);
        panel.add(label);

        JButton switchButton = new JButton("Go to Panel 2");
        switchButton.setBounds(120, 130, 150, 30);
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel1.setVisible(false);
                panel2.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        panel.add(switchButton);

        return panel;
    }

    private JPanel createPanel2() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.PINK);

        JLabel label = new JLabel("This is Panel 2");
        label.setBounds(130, 80, 150, 30);
        panel.add(label);

        JButton backButton = new JButton("Back to Panel 1");
        backButton.setBounds(120, 130, 150, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel2.setVisible(false);
                panel1.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        panel.add(backButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PanelSwitchExample().setVisible(true);
        });
    }
}
