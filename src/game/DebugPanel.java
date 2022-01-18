package game;

import javax.swing.*;

public class DebugPanel extends JTextArea {

    public DebugPanel()
    {
        setLineWrap(true);
    }

    public void write(String newText)
    {
        append(newText);
        append("\n");
    }
}
