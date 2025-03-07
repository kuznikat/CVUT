package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.entity.Player;
import cz.cvut.fel.pjv.gameData.SaveGame;
import cz.cvut.fel.pjv.tile.TileControler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import static java.awt.Color.white;

public class GamePanel extends JPanel implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(UI.class.getName());


    // Screen Settings
    final int originalTileSize =  16; //16 x 16 px tile : плитка
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //16 x 3(scale) = 48 x 48 px: a size of one square of a game panel
    public final int maxScreenColumns = 16;
    public final int maxScreenRows = 12;
    public final int screenWidth = tileSize * maxScreenColumns; //960  pixels
    public final int screenHeight = tileSize * maxScreenRows; //576 pixels

    //WORLD settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;


    //FPS
    int FPS = 60;

    //SYSTEM
    TileControler tileContr = new TileControler(this);
    public final KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    SaveGame saveGame = new SaveGame(this);

    Thread gameThread; //when we start this method it automatically calls run function


    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);


    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);

    public Entity obj[] = new Entity[20];
    public Entity npc[] = new Entity[20];
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();


    //GameState
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int winnerState = 7;

    private int lastLoggedState = -1; // To track the last logged state


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight)); // sets the size of class JPanel
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH); //gamepanel can recognise the key input
        this.setFocusable(true); //game panel will be focused to receive key input
    }

    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        if(gameState != playState){
            gameState = titleState;
        }
        logStateChange();

    }

    public void retry(){
        player.setDefaultPositions();
        player.restoreLife();
        aSetter.setNPC();
        aSetter.setMonster();
        playMusic(0);

    }

    public void restart(){
        player.setDefaultValues();
        player.setDefaultPositions();
        player.restoreLife();
        player.setItems();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();

        // Clear inventory and objects to avoid duplicates
        player.inventory.clear();
        for (int i = 0; i < obj.length; i++) {
            obj[i] = null;
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS; //representation of one second in nanoseconds divided by 60 = 0.16666 seconds. it means we can draw the screen 60 times per second
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime =System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update(); //1. Update: update information such as characters position
                repaint();//2.Draw:draw the screen with the updated info
                delta --;
                drawCount ++;
            }
            if(timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Update method handle the main game loop for updating various entities and game state
     */
    public void update(){

        if(gameState == playState){
            //Player
            player.update();

            //NPC
            for (int i = 0; i < npc.length; i++) {
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            //Monster
            for (int i = 0; i < monster.length; i++) {
                if(monster[i] != null){
                    if(monster[i].alive == true && monster[i].dying == false){
                        monster[i].update();
                    }
                    if(monster[i].alive == false){
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }
       }
        logStateChange();
    }

    /**
     * Logging game state
     */
    private void logStateChange() {
        if (gameState != lastLoggedState) {
            lastLoggedState = gameState;
            switch (gameState) {
                case titleState:
                    LOGGER.info("Game Started");
                    break;
                case playState:
                    LOGGER.info("Game Playing");
                    break;
                case pauseState:
                    LOGGER.info("Game Paused");
                    break;
                case dialogueState:
                    LOGGER.info("In Dialogue");
                    break;
                case characterState:
                    LOGGER.info("In Character Menu");
                    break;
                case optionsState:
                    LOGGER.info("In Options Menu");
                    break;
                case gameOverState:
                    LOGGER.info("Game Over");
                    break;
            }
        }
    }

    /**
     *
     * Method is for :Game State Handling,Entity Drawing,UI Drawing
     * @param g the <code>Graphics</code> object to protect
     *
     */
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText == true){
            drawStart = System.nanoTime();
        }

        //TITLE Screen
        if(gameState == titleState){
            ui.draw(g2);
        }

        //OTHERS
        else {
            //TILE
            tileContr.draw(g2); //this must go first, because method will draw it as a layers

            //ADD ENTITIES TO THE LIST

            //PLAYER
            entityList.add(player);

            //NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            //OBJECT
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }
            //MONSTER
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            //DRAW Entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            //EMPTY Entity List
            entityList.clear();

            //UI
            ui.draw(g2);
        }

        //DEBUG
        if(keyH.showDebugText == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial",Font.PLAIN, 20));
            g2.setColor(white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;
            g2.drawString("WorldX" + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY" + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col" + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("Row" + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;


            g2.drawString("Draw time " + passed, x, y);
            System.out.println("Draw Time :"+passed);
        }
        g2.dispose();

    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        soundEffect.setFile(i);
        soundEffect.play();
    }

}
