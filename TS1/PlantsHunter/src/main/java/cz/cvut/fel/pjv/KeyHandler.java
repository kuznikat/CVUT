package cz.cvut.fel.pjv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed;

    //DEBUG
    boolean showDebugText = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) { //key buttons are pressed
        int code = e.getKeyCode();

        //TITLE State
        if(gp.gameState == gp.titleState){
            titleState(code);
        }

        //Play State
        if(gp.gameState == gp.playState) {
            playState(code);
        }

        //PAUSE STATE
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }

        //DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        }

        //CHARACTER STATE
        else if(gp.gameState == gp.characterState){
            characterState(code);
        }

        //OPTIONS STATE
        else if(gp.gameState == gp.optionsState){
            optionsState(code);
        }

        //GAME OVER STATE
        else if(gp.gameState == gp.gameOverState){
            gameOverState(code);
        }

        else if (gp.gameState == gp.winnerState) {
            winState(code);
        }
    }

    public void titleState(int code){
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum --;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 2;
            }
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum ++;
            if(gp.ui.commandNum > 2){
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum == 1){
                gp.saveGame.load();
                gp.playMusic(0);
            }
            if(gp.ui.commandNum == 2){
                System.exit(0);
            }
        }
    }

    public void playState(int code){
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if (code == KeyEvent.VK_O) {
            gp.gameState = gp.optionsState;
        }

        //DEBUG
        if(code == KeyEvent.VK_T){
            if(showDebugText == false){
                showDebugText = true;
            }
            else if(showDebugText == true){
                showDebugText = false;
            }
        }
    }

    public void pauseState(int code){
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code){
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0){
                gp.ui.slotRow--;
            }
        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.slotCol != 0){
                gp.ui.slotCol--;
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 3){
                gp.ui.slotRow++;
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 4){
                gp.ui.slotCol++;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }

    public void optionsState(int code){
        if(code == KeyEvent.VK_O){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState){
            case 0: maxCommandNum = 2; break;
            case 2: maxCommandNum = 1; break;

        }
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum --;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum ++;
            if(gp.ui.commandNum > maxCommandNum){
                gp.ui.commandNum = 0;
            }
        }
    }

    public void gameOverState(int code){
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }

        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1){
                gp.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.retry();
                gp.gameState = gp.playState;
            } else if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.stopMusic();
                gp.restart();
            }
        }
    }

    public void winState(int code){
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }

        if(code == KeyEvent.VK_S){
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1){
                gp.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.gameState = gp.playState;
                gp.restart();
                gp.playMusic(0);
            }
            else if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.stopMusic();
                gp.restart();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // key buttons are not pressed
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}
