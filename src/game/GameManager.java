package game;

import java.util.Random;

public class GameManager
{

    int score = 0;
    int winDicesCount = 0;
    boolean roundEnded = false;
    boolean gameEnded = false;
    int currentRound = 1;


    int selectedDice = -1;
    Dice[] dices;
    Random rng;

    public GameManager()
    {
        rng = new Random();
        dices = new Dice[10];

        for(int i = 0; i<10 ; i++)
        {
            dices[i] = new Dice();
            dices[i].id = i;
        }
    }
}
