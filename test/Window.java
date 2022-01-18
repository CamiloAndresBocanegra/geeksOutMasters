package game;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private
        Panel windowPanel;

    public Window()
    {
        initWindow();
        pack();
    }

    public void initWindow()
    {
//SETTING UP WINDOW

        setTitle("Geeks Out Masters");
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        windowPanel = new Panel();
        windowPanel.setLayout(new GridLayout(2, 3));
        add(windowPanel);
//header test
//        JLabel header = new JLabel();
//        header.setText("Board");
//        header.setOpaque(true);
//        header.setBackground(colorBackground);
//        header.setForeground(new Color(255,255,255));
//        header.setFont(new Font(Font.DIALOG,Font.BOLD,20));
//        header.setHorizontalAlignment(JLabel.CENTER);
//        header.setVerticalAlignment(JLabel.CENTER);
//        windowPanel.add(header, BorderLayout.NORTH);

//SETTING UP MAIN 4 PANELS
        Panel activeDicesPanel = new Panel();
        activeDicesPanel.addImage("2");
        windowPanel.addPanel(activeDicesPanel);

        Panel usedDicesPanel = new Panel();
        usedDicesPanel.addImage("3");
        windowPanel.addPanel(usedDicesPanel);

        Panel pointsPanel = new Panel();
        pointsPanel.addImage("4");
        windowPanel.addPanel(pointsPanel);

        Panel inactiveDicesPanel = new Panel();
        inactiveDicesPanel.addImage("0");
        windowPanel.addPanel(inactiveDicesPanel);


//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.fill = GridBagConstraints.BOTH;

//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        windowPanel.add(activeDicesPanel, constraints);
//        constraints.gridx = 1;
//        windowPanel.add(inactiveDicesPanel, constraints);
//        constraints.gridx = 0;
//        constraints.gridy = 1;
//        windowPanel.add(pointsPanel, constraints);
//        constraints.gridx = 1;
//        windowPanel.add(usedDicesPanel, constraints);


//        window.setPreferredSize(new Dimension(250, 80));
//        window.setSize(800,600); //use this or pack


    }


}
