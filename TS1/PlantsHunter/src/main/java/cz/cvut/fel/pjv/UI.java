package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.object.Heart;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI {
    private static final Logger LOGGER = Logger.getLogger(UI.class.getName());

    GamePanel gp;
    Graphics2D g2;
    Font pixelMono, pixelBold;
    BufferedImage h_full, h_half, h_blank;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    int subState = 0;


    public UI(GamePanel gp){
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/PixeloidMono.ttf");
            pixelMono = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/PixeloidSans-Bold.ttf");
            pixelBold = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load font", e);
        }

        //CREATE HUD OBJECT
        Entity heart = new Heart(gp);
        h_full = heart.image;
        h_half = heart.image2;
        h_blank = heart.image3;
    }

    public void displayMessage(String text){
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(pixelMono);
        g2.setColor(Color.WHITE);

        //TITLE State
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        //PLAY State
        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        }

        //PAUSE State
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        //DIALOGUE State
        if(gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
        //CHARACTER State
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }
        //OPTIONS State
        if(gp.gameState == gp.optionsState){
            drawOptionsScreen();
        }

        //GAME OVER State
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }

        //WIN State
        if(gp.gameState == gp.winnerState){
            drawWinScreen();
        }
    }

    public void drawPlayerLife(){

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //DRAW MAX LIFE
        while(i < gp.player.maxLife/2){
            g2.drawImage(h_blank,x, y, null);
            i++;
            x+=gp.tileSize;
        }

        //RESET
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        //DRAW CURRENT LIFE
        while(i < gp.player.life){
            g2.drawImage(h_half,x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(h_full,x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));

        for (int i = 0; i < message.size(); i++) {
            if(message.get(i) != null){

                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);

                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // equal to messageCounter++, but because of message Counter is an Array List, I cant do increase as for int
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;

                //dispose the message
                if(messageCounter.get(i) > 120 ){ // 2 seconds
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
            
        }

    }

    public void drawTitleScreen(){


        g2.setColor(new Color(172, 133, 245));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        //TITLE Name
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
        String text = "Plants Hunter";
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;


        //SHADOW
        g2.setColor(new Color(25, 3, 71));
        g2.drawString(text, x + 5, y + 5);

        //MAIN COLOR
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        //ZUBASTIC Image

        x = gp.screenWidth / 2 - (gp.tileSize * 2)/2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.faceLeft, x, y, gp.tileSize *2, gp.tileSize *2,null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        //CONTINUE
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
        text = "CONTINUE";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }

        //QUIT
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,40F));
        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

    public void drawDialogueScreen(){

        //Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize * 3);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen(){
        //Create a frame
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize ;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 7;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(22F ));
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 40;

        //NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Plants ", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight + 15;

        //VALUES
        int tailX = (frameX + frameWidth) - 30;
        //Reset textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.plants);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.face, tailX - gp.tileSize, textY - 15,null);
        textY += lineHeight;

        g2.drawImage(gp.player.currentShield.face,tailX - gp.tileSize, textY - 15,null );


    }

    public void drawInventory(){

        //FRAME
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        //DRAW PLAYERS ITEMS
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            //EQUIP Cursor
            if(gp.player.inventory.get(i) == gp.player.currentWeapon ||
                    gp.player.inventory.get(i) == gp.player.currentShield){
                g2.setColor(new Color(248, 255, 36));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10,10);
            }
            g2.drawImage(gp.player.inventory.get(i).face, slotX, slotY,null);

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14){
                slotX = slotXStart;
                slotY += slotSize;
            }
            
        }

        //CURSOR for 5 slots horizontally and 4 slots vertically
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //DRAW Cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY,cursorWidth,cursorHeight,10,10);

        //DESCRIPTION Frame
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;


        //DESCRIPTION Text
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));
        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()){

            drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);

            for(String line : gp.player.inventory.get(itemIndex).description.split("\n")){
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public void drawGameOverScreen(){
        g2.setColor(new Color(36, 6, 74, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));

        text = "GAME OVER";
        //Shadow
        g2.setColor(Color.BLACK);
        x = getXForCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);
        //Text
        g2.setColor(Color.white);
        g2.drawString(text, x -4, y- 4);


        //Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "RESET";
        x = getXForCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - 40, y);
        }


        //Back to the title screen
        text = "MENU";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - 40, y);
        }
    }

    public void drawWinScreen(){
        g2.setColor(new Color(36, 6, 74, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,50f));

        text = "MISSION " +
                "COMPLETED";
        //Shadow
        g2.setColor(Color.BLACK);
        x = getXForCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);
        //Text
        g2.setColor(Color.white);
        g2.drawString(text, x -4, y- 4);

        //Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "RESTART";
        x = getXForCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - 40, y);
        }


        //Back to the title screen
        text = "MENU";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - 40, y);
        }
    }

    public void drawOptionsScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(30F));

        //SUB Window
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 9;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState){
            case 0: options_top(frameX, frameY); break;
            case 1: options_control(frameX,frameY);break;
            case 2: options_endGame(frameX, frameY);break;
        }
        gp.keyH.enterPressed = false;
    }

    public void options_top(int frameX, int frameY){
        int textX;
        int textY;
        //TITLE
        String text = "Option Menu";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);


        //CONTROL
        textX = frameX + gp.tileSize ;
        textY += gp.tileSize * 2;
        g2.drawString("Control", textX , textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 1;
                commandNum =  0;
            }
        }

        //END GAME
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if(commandNum == 1){
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 2;
                commandNum = 0;
            }
        }

        //BACK
        textY += gp.tileSize * 3;
        g2.drawString("Back", textX , textY);
        if(commandNum == 2){
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed){
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

    }

    public void options_control(int frameX, int frameY){
        int textX;
        int textY;
        //TITLE
        String text = "Control";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text,textX ,textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY); textY += gp.tileSize;
        g2.drawString("Attack", textX, textY); textY += gp.tileSize;
        g2.drawString("Confirm", textX, textY); textY += gp.tileSize;
        g2.drawString("Char.scr.", textX, textY); textY += gp.tileSize;
        g2.drawString("Pause", textX, textY); textY += gp.tileSize;
        g2.drawString("Options", textX, textY); textY += gp.tileSize;


        textX = frameX + gp.tileSize * 5;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("W/A/S/D", textX, textY); textY += gp.tileSize;
        g2.drawString("SPACE", textX, textY); textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY); textY += gp.tileSize;
        g2.drawString(" C", textX, textY); textY += gp.tileSize;
        g2.drawString("ESC", textX, textY); textY += gp.tileSize;
        g2.drawString("O", textX, textY); textY += gp.tileSize;

        //BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 2;
            }
        }
    }

    public void options_endGame(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Quit the Game?";
        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX,textY);
        }
        //YES or NO
        String text = " YES";
        textX = getXForCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text, textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                gp.gameState = gp.titleState;
                gp.stopMusic();
                gp.restart();
            }
        }

        text = "NO";
        textX = getXForCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1){
            g2.drawString(">", textX - 25, textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 3;
            }
        }
    }

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(10, 199, 223, 220);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color(248, 255, 36);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10,25,25);

    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public int getXForAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}
