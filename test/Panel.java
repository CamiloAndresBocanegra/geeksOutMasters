package game;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel{

    public void addPanel(JPanel panel)
    {
        setPreferredSize(new Dimension(200,200));
        add(panel);
    }

    public void addImage(String imageName)
    {
        ImageIcon image = new ImageIcon(getClass().getResource("/resources/"+ imageName + ".png"));
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(image);
        add(imageLabel);
    }
}
