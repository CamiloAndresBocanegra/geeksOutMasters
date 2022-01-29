package game;

import javax.swing.*;
import java.awt.*;

public class Dice extends JButton
{
    int currentFace;
    int id;

    public Dice(){
        setPreferredSize(new Dimension(90,90));
    }
}
