package cz.cvut.fel.pjv.entity;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.KeyHandler;
import cz.cvut.fel.pjv.UI;
import cz.cvut.fel.pjv.object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    private int attackCounter = 0;
    public boolean moves = false;

    public ArrayList<Entity> inventory = new ArrayList<>();
    public int maxInventorySize = 20;



    public Player (GamePanel gp, KeyHandler keyH) {
        super(gp); // calling constructor of the superclass of this class(player class)
        this.gp = gp;
        this.keyH = keyH;


        screenX = gp.screenWidth/ 2 - (gp.tileSize/2);
        screenY = gp.screenHeight / 2 - (gp.tileSize/2);
        //SOLID AREA
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;


        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }


    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "";

        //PLAYER STATUS
        level = 1;
        maxLife = 6; // 2 lives == 1 full heart
        life = maxLife;
        strength = 1; //the more strength player has, the more damage he gives
        dexterity = 1;
        plants = 0;
        currentWeapon = new Laser(gp); //The total attack value is decided by strength and weapon
        currentShield = new Shield(gp);
        attack = getAttack();
        defence = getDefence();
    }

    public void setDefaultPositions(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "";
    }

    public void restoreLife(){
        life = maxLife;
        invincible = false;
        attacking = false;
    }

    public void setItems(){
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(new Soda(gp));
        inventory.add(new Strawberry(gp));
        inventory.add(new Drop(gp));
    }

    public int getAttack(){
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefence(){
        return defence = dexterity * currentShield.defenceValue;
    }

    public void getPlayerImage(){
        face = setup("/player/face_Left",gp.tileSize,gp.tileSize);
        faceLeft = setup("/player/face_Left",gp.tileSize,gp.tileSize);
        faceRight = setup("/player/face_Right",gp.tileSize,gp.tileSize);
        back = setup("/player/back",gp.tileSize,gp.tileSize);
    }

    public void getPlayerAttackImage(){
        attackUp = setup("/player/aUp",gp.tileSize,gp.tileSize * 2);
        attackDown = setup("/player/aDown",gp.tileSize,gp.tileSize * 2);
        attackLeft = setup("/player/aRight",gp.tileSize * 2,gp.tileSize);
        attackRight = setup("/player/aLeft", gp.tileSize * 2,gp.tileSize);
    }

    /**
     * Method handles the player's movement, interaction with the environment, and checks for collisions
     */
    public void update(){

        if (keyH.spacePressed && !attacking) {
            gp.playSE(1);
            attacking = true;
            attackCounter = 0; // start the attack duration
        }

        if (attacking) {
            attacking();
        }

        if((keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed )){
            moves = true;
            if(keyH.upPressed){
                direction = "up";
            }else if(keyH.downPressed){//when down button is pressed -players coordinates incises by 4 bcs players speed == 4 from the beginning
                direction = "down";
            }else if(keyH.leftPressed){
                direction = "left";
            }else if(keyH.rightPressed) {
                direction = "right";
            }
        }
        else{
            moves = false;
        }

        //CHECK TILE COLLISION
        collisionOn = false;
        gp.cChecker.checkTile(this);

        //CHECK OBJECT COLLISION
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        //CHECK NPC COLLISION
        int npcIndex = gp.cChecker.checkEntity(this,gp.npc);
        interactNPC(npcIndex);

        //CHECK MONSTER COLLISION
        int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
        touchMonster(monsterIndex);

        //CHECK EVENT
        gp.eHandler.checkEvent();


        //IF Collision is false, player can move
        if(collisionOn == false && gp.keyH.enterPressed == false && moves == true){
            switch (direction){
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY+= speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        gp.keyH.enterPressed = false;


        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(life > maxLife){
            life = maxLife;
        }
        if(life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.stopMusic();
        }

    }

    /**
     * Method for handling the logic for player attacks
     */
    public void attacking(){
        attackCounter++;

        if(attackCounter <= 25){

            attacking = true;

            //SAVE Current worldX,worldY,solidArea

            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjusts players worldX/Y for the attackArea
            switch (direction){
                case "up": worldY -= attackArea.height;break;
                case "down": worldY += attackArea.height;break;
                case "left": worldX -= attackArea.width;break;
                case "right": worldX += attackArea.width;break;
            }

            //Attack area becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //Check monster collision with the updated worldX/Y and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            damageMonster(monsterIndex);

            //Restoring the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if (attackCounter > 25) {// Assuming attack lasts 25 frames
            attackCounter = 0;
            attacking = false;

        }

    }

    public void plantCounter() {
        plants++;
        if (plants == 5) {
            gp.gameState = gp.winnerState;
            gp.playSE(5);
            gp.stopMusic();
        }
    }

    public void pickUpObject(int i){
        if(i != 999){


            if (gp.obj[i] != null) {
                String text = gp.obj[i].name;
                if (inventory.size() < maxInventorySize) {
                    inventory.add(gp.obj[i]);

                    if (text.equals("Plant1") || text.equals("Plant2") || text.equals("Plant3") || text.equals("Plant4") || text.equals("Plant5")) {
                        plantCounter();
                    }

                    gp.obj[i] = null;
                    gp.ui.displayMessage("Picked up " + text);
                    gp.playSE(3);
                } else {
                    gp.ui.displayMessage("Inventory is full");
                }
            }
        }
    }

    public void boostSpeed(int boostAmount, long durationMillis) {
        final int originalSpeed = speed;

        // Boost speed
        speed += boostAmount;

        // Schedule task to reset the speed back to normal after duration
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                speed = originalSpeed;
            }
        }, durationMillis);
    }

    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.enterPressed == true) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void touchMonster(int i){

        if(i != 999){
            if(invincible == false && gp.monster[i].dying == false){
                gp.playSE(4);

                int damage = gp.monster[i].attack - defence;
                if(damage < 0){
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i){
        if(i != 999) {
            if(gp.monster[i].invincible == false){

                gp.playSE(4);

                int damage = attack - gp.monster[i].defence;
                if(damage < 0){
                    damage = 0;
                }
                gp.ui.displayMessage(damage + "Damage!");

                gp.monster[i].life -= damage;
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if(gp.monster[i].life <= 0){
                    gp.monster[i].dying = true;
                    gp.ui.displayMessage("Killed the " + gp.monster[i].name);
                }
            }
        }

    }

    /**
     * Method allows the player to interact with items in their inventory through the game UI
     */
    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_laser){
                currentWeapon = selectedItem;
                attack = getAttack();
            }
            if(selectedItem.type == type_shield){
                currentShield = selectedItem;
                defence = getDefence();
            }
            if(selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    /**
     * Method draw player character and his movements depending on direction
     */
    public void draw(Graphics2D g2){

        int tempScreenX = screenX;
        int tempScreenY = screenY;
        BufferedImage image = null;
                switch (direction) {
            case "up" -> {
                if (attacking) {
                    tempScreenY = screenY - gp.tileSize;
                    image = attackUp;
                } else {
                    image = back;
                }
            }
            case "down" -> {
                if (attacking) {
                    image = attackDown;
                } else {
                    image = face;
                }
            }
            case "left" -> {
                if (attacking) {
                    tempScreenX = screenX - gp.tileSize;
                    image = attackRight;
                } else {
                    image = faceLeft;
                }
            }
            case "right" -> {
                if (attacking) {
                    image = attackLeft;
                } else {
                    image = faceRight;
                }
            }
            default -> image = faceLeft;
        };

        if(invincible == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //RESET Alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    }
}


