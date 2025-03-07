package cz.cvut.fel.pjv;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this lets the window properly close when user clicks the close("x") button
        window.setResizable(false); // we cannot resize the window
        window.setTitle("Plants Hunter"); //set the title of the game

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); //this window must be sized to preferred settings of subcomponent(gamePanel)


        window.setLocationRelativeTo(null); //not specify location of the window = window will be displayed at the center of the screen
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}