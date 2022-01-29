package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Window extends JFrame
{
    int score = 0;
    int winDicesCount = 0;
    boolean roundEnded = false;
    boolean gameEnded = false;
    int currentRound = 1;


    int selectedDice = -1;
    Dice[] dices;
    Random rng;

//    DebugPanel DEBUGPanel;
    JLabel selectedDiceLabel;
    JTextArea diceExplanationText;
    JPanel selectedDicePanel;

    JPanel activeDicesPanel;
    JPanel inactiveDicesPanel;
    JPanel actionsPanel;
    JPanel pointsPanel;
    JPanel usedDicesPanel;

    JButton nextRoundButton;

    DiceListener diceListener;
    inactiveDiceListener inactiveDiceListener;

    final int   NONE = -1,
                MEEPLE = 0,
                DRAGON = 1,
                HEART = 2,
                ROCKET = 3,
                HERO = 4,
                N42= 5;

    public Window()
    {
        initWindow();

//        pack();
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(800,800);
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
    }

    public void initWindow()
    {
//window frame
        setTitle("Geeks Out Masters");
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//main window panel
        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new GridLayout(2, 3));
        add(windowPanel);

//creating and adding panels
        selectedDicePanel = new JPanel();
        selectedDicePanel.setLayout(new BoxLayout(selectedDicePanel, BoxLayout.Y_AXIS));

        activeDicesPanel = new JPanel();
        inactiveDicesPanel = new JPanel();
        actionsPanel = new JPanel();
        pointsPanel = new JPanel();
        usedDicesPanel = new JPanel();

    // Debug

//        DEBUGPanel = new DebugPanel();
//
//        JScrollPane scrollPane = new JScrollPane(DEBUGPanel);
//
//        windowPanel.add(scrollPane);
    //-
        windowPanel.add(selectedDicePanel);

        windowPanel.add(activeDicesPanel);
        windowPanel.add(inactiveDicesPanel);
        windowPanel.add(actionsPanel);
        windowPanel.add(pointsPanel);
        windowPanel.add(usedDicesPanel);
// Dices Creation
        rng = new Random();

        dices = new Dice[10];

        inactiveDiceListener = new inactiveDiceListener();
        diceListener = new DiceListener();

        for(int i = 0; i<10 ; i++)
        {
            dices[i] = new Dice();
            dices[i].id = i;
        }

//Selected Dice view
        {
            ImageIcon image = new ImageIcon(getClass().getResource("/resources/-1.png"));
            selectedDiceLabel = new JLabel();
            selectedDiceLabel.setIcon(image);
            selectedDiceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            selectedDicePanel.add(selectedDiceLabel);

            diceExplanationText = new JTextArea();
            selectedDicePanel.add(diceExplanationText);
        }

// Buttons
        resetDicesPositions();

        nextRoundButton = new JButton("next round");
        nextRoundButton.addActionListener(new nextRoundListener());
        actionsPanel.add(nextRoundButton);

// exit button
        JButton exitButton = new JButton("Exit");
        ExitButtonListener exitListener = new ExitButtonListener();
        exitButton.addActionListener(exitListener);
        actionsPanel.add(exitButton);
    }

    public void moveDice(int diceId, JPanel to)
    {
        to.add(dices[diceId]);
        // removing listeners from the dice
        MouseListener[] mouseListeners = dices[diceId].getMouseListeners();
        for (int i = 0; i < mouseListeners.length ; i++)
        {
            dices[diceId].removeMouseListener(mouseListeners[i]);
        }
        //
        if (to == activeDicesPanel)
        {
            throwDice(diceId);
            dices[diceId].addMouseListener(diceListener);
        }else if(to == inactiveDicesPanel){
            dices[diceId].currentFace = NONE;
            updateDice(diceId);
            dices[diceId].addMouseListener(inactiveDiceListener);
        }
        activeDicesPanel.revalidate();
        activeDicesPanel.repaint();
        inactiveDicesPanel.revalidate();
        inactiveDicesPanel.repaint();
    }

    public void throwDice(int diceId)
    {
        dices[diceId].currentFace = rng.nextInt(6);
        updateDice(diceId);

//        DEBUGPanel.write("throwing dice "+ diceId);
    }

    public void flipDice(int diceId)
    {
        dices[diceId].currentFace = (dices[diceId].currentFace + 3) % 6;
        updateDice(diceId);
//        DEBUGPanel.write("flipping dice "+ diceId);
    }

    public void updateDice(int diceId)
    {
        int imageName = dices[diceId].currentFace;
        ImageIcon image = new ImageIcon(getClass().getResource("/resources/" + imageName + ".png"));
        dices[diceId].setIcon(image);
        dices[diceId].repaint();
        dices[diceId].revalidate();
    }

    public void checkRoundEnd()
    {
        int roundPoints = 0;

        int dicesCount = activeDicesPanel.getComponentCount();
        if(dicesCount == 0)
        {
//            JOptionPane.showMessageDialog(null,"round over");
            roundEnded = true;
//            DEBUGPanel.write("no more dices, round ended");
            checkWin();
            return;
        }
        for(int i = 0; i<dicesCount ; i++)
        {
            Dice dice = (Dice) activeDicesPanel.getComponent(i);
            if(dicesCount > 1)
            {
                switch(dice.currentFace)
                {
                    case ROCKET:
                    case HERO:
                    case MEEPLE:
//                        DEBUGPanel.write("round continues");
                        return;
                    default:
                        break;
                }
            }
            if(dice.currentFace == HEART)
            {
                if(inactiveDicesPanel.getComponentCount() > 0)
                {
//                    DEBUGPanel.write("round continues");
                    return;
                }
            }
            if(dice.currentFace == N42)
            {
                roundPoints++;
            }else if(dice.currentFace == DRAGON)
            {
                roundPoints--;
            }
        }
        score = 0;
        if(roundPoints < 0)
        {
            winDicesCount = 0;
//            DEBUGPanel.write("there are dragons alive, you lost all your points");
        }else{
            winDicesCount += roundPoints;
            for(int i = 1; i <= winDicesCount ; i++)
            {

                 score += i;
            }
//            DEBUGPanel.write("you earned points ");
        }
        roundEnded = true;
        checkWin();
    }

    public void checkWin()
    {
        if(currentRound == 5)
        {
            // Game Ended
            if(score < 30)
            {
//                DEBUGPanel.write("you lose");
            }else{
//                DEBUGPanel.write("you won");
            }
            //TODO: end properly
            nextRoundButton.setText("Restart Game");
            gameEnded = true;
        }
    }

    public void resetDicesPositions()
    {
        for (int i=0 ; i<10 ; i++)
        {
            if (i < 3) // inactive dices
            {
                dices[i].currentFace = NONE;
                moveDice(i, inactiveDicesPanel);
            }
            else // active dices
            {
                moveDice(i, activeDicesPanel);
            }
            updateDice(i);
        }
        roundEnded = false;
    }

    public void setSelectedDice(int value)
    {
        selectedDice = value;
        int currentFace;
        if(value == NONE)
        {
            currentFace = -1;
        }else{
            currentFace = dices[value].currentFace;
        }
        ImageIcon image = new ImageIcon(getClass().getResource("/resources/"+ currentFace +".png"));
        selectedDiceLabel.setIcon(image);
        String outputText = "";
        switch (currentFace)
        {
            case NONE:
            {
                outputText = "Ningun Dado Seleccionado, \n";
            }break;
            case MEEPLE:
            {
                outputText = "Meeple seleccionado \n" +
                        "Al usar su habilidad en otro dado, lo vuelve a tirar \n";
            }break;
            case DRAGON:
            {
                outputText = "Dragon seleccionado \n" +
                        "Si solo quedan dragones al final de la ronda, pierdes todos los puntos \n";
            }break;
            case HEART:
            {
                outputText = "Corazon seleccionado \n" +
                        "Al usar su habilidad en uno de los dados inactivos, lo trae a los dados activos y lo tira \n";
            }break;
            case ROCKET:
            {
                outputText = "Cohete seleccionado \n" +
                        "Al usar su habilidad sobre otro dado, lo manda a los dados inactivos \n";
            }break;
            case HERO:
            {
                outputText = "Heroe seleccionado \n" +
                        "Al usar su habilidad sobre otro dado, le da la vuelta a} su cara contraria \n";
            }break;
            case N42:
            {
                outputText = "42 seleccionado \n" +
                        "Si solo quedan dados 42, se acaba la ronda y se añaden a los dados obtenidos \n";
            }break;
        }
        outputText += "\n\nclick izquierdo para seleccionar un dado \n" +
                "click derecho para usar habilidad del dado seleccionado \n";
        diceExplanationText.setText(outputText);
    }

    private class DiceListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {

            if (e.getButton() == MouseEvent.BUTTON1)
            {
                if (selectedDice == ((Dice) e.getSource()).id)
                {
                    setSelectedDice(NONE); //deselecting dice
//                    DEBUGPanel.write("Deselecting");
                    return;
                }
                setSelectedDice(((Dice) e.getSource()).id);
//                DEBUGPanel.write("Selecting "+ selectedDice);
            }
            if (e.getButton() == MouseEvent.BUTTON3)
            {
                if ( (((Dice) e.getSource()).id == selectedDice))
                {
//                    DEBUGPanel.write("same dice selected");
                    return;
                }
                if((selectedDice == NONE))
                {
//                    DEBUGPanel.write("no dice selected");
                    return;
                }
                switch (dices[selectedDice].currentFace)
                {
                    case MEEPLE:
                    {
//                        meeple: throw dice
                        throwDice(((Dice) e.getSource()).id);
                        moveDice(selectedDice, usedDicesPanel);
                        selectedDice = -1;
                    }break;
                    case ROCKET:
                    {
//                        rocket: move to inactive
                        moveDice(((Dice) e.getSource()).id, inactiveDicesPanel);
                        moveDice(selectedDice, usedDicesPanel);
                        selectedDice = -1;
                    }break;
                    case HERO:
                    {
//                        hero: flip
                        flipDice(((Dice) e.getSource()).id);
                        moveDice(selectedDice, usedDicesPanel);
                        selectedDice = -1;
                    }break;
                    default: // (2) heart, (1)dragon, (5)42
                    {
//                        DEBUGPanel.write("can't use ability");
                        break;
                    }
                }
                checkRoundEnd();
            }
        }
    }

    private class inactiveDiceListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (selectedDice != NONE)
            {
                if (dices[selectedDice].currentFace == HEART)
                {
                    moveDice(((Dice) e.getSource()).id, activeDicesPanel);
                    moveDice(selectedDice, usedDicesPanel);
                    selectedDice = NONE;

                    checkRoundEnd();
                }
            }
        }
    }

    private class nextRoundListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(roundEnded)
            {
                resetDicesPositions();
                if(gameEnded)
                {
                    gameEnded = false;
                    ((JButton) e.getSource()).setText("Next Round");
                    currentRound = 1;
                    score = 0;
                    winDicesCount = 0;
                }else{

                    currentRound += 1;
                }
//                DEBUGPanel.write("current round: "+ currentRound);
//                DEBUGPanel.write("current score: "+ score+", windicescount: "+ winDicesCount);

            }
        }
    }

    private class ExitButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Runtime.getRuntime().exit(0);
        }
    }
}
