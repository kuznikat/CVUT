package cz.cvut.fel.pjv.entity;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.UI;
import cz.cvut.fel.pjv.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
     GamePanel gp;
     public BufferedImage face,faceLeft,faceRight,back;
     public BufferedImage attackUp, attackDown, attackLeft, attackRight;
     public BufferedImage image, image2, image3;
     public Rectangle solidArea = new Rectangle(0,0,48,48);
     public Rectangle attackArea = new Rectangle(0,0,0,0);
     public int solidAreaDefaultX, solidAreaDefaultY;
     public boolean collision = false;
     String dialogues[] = new String[30];


     //STATE
     public int worldX, worldY;
     public String direction = "";
     int dialogueIndex = 0;
     public boolean collisionOn = false;
     public boolean invincible = false;
     boolean attacking = false;
     public boolean alive = true;
     public boolean dying = false;
     boolean hpBarOn = false;

     //COUNTER
     public int actionLockCounter = 0;
     public int invincibleCounter = 0;
     int dyingCounter = 0;
     int hpBarCounter = 0;

     //CHARACTER ATTRIBUTE
     public String name;
     public int speed;
     public int maxLife;
     public int life;
     public int level;
     public int strength;
     public int dexterity;
     public int attack;
     public int defence;
     public int plants;
     public Entity currentWeapon;
     public Entity currentShield;

     //ITEM ATTRIBUTES
     public int attackValue;
     public int defenceValue;
     public String description = "";
     public int value;

     //TYPE
     public int type; // 0=player/1=npc/2=monster/
     public final int type_monster = 2;
     public final int type_laser = 3;
     public final int type_shield = 4;
     public static final int type_consumable = 5;


     public Entity(GamePanel gp){this.gp = gp;}

     public void setAction(){}

     public void damageReaction(){}

     public void speak(){
          if(dialogues[dialogueIndex] == null){
               dialogueIndex = 0;
          }
          gp.ui.currentDialogue = dialogues[dialogueIndex];
          dialogueIndex ++;

          switch (gp.player.direction){
               case "up":
                    direction = "down";
                    break;
               case "down":
                    direction = "up";
                    break;
               case "left":
                    direction = "right";
                    break;
               case "right":
                    direction = "left";
                    break;
          }
     }

     public void use(Entity entity){}

     public void checkDrop(){}

     /**
      * Method that is used in Monster class for receiving which item will be dropped after monster death
      * @param droppedItem is for setting object
      */
     public void dropItem(Entity droppedItem){
          for(int i = 0; i < gp.obj.length; i++){
               if(gp.obj[i] == null){
                    gp.obj[i] = droppedItem;
                    gp.obj[i].worldX = worldX;
                    gp.obj[i].worldY = worldY;
                    break;
               }
          }
     }

     public void update(){
          setAction(); // even though the method is empty, it was also defined in Entity class, so it has priority before
          collisionOn = false;
          gp.cChecker.checkTile(this); // it is going to pass NPC_Yuzo class as an entity
          gp.cChecker.checkObject(this, false);
          gp.cChecker.checkEntity(this, gp.npc);
          gp.cChecker.checkEntity(this, gp.monster);
          boolean touchPlayer = gp.cChecker.checkPlayer(this);

          if(this.type == type_monster && touchPlayer == true){
               damagePlayer(attack);
          }

          if(!collisionOn && direction != null){
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

          if(invincible == true){
               invincibleCounter++;
               if(invincibleCounter > 40){
                    invincible = false;
                    invincibleCounter = 0;
               }
          }
     }

     /**
      * This method handles players damage
      * @param attack  is used for calculation damage that player will receive
      */
     public void damagePlayer(int attack){
          if(gp.player.invincible == false){

               gp.playSE(4);
               int damage = attack - gp.player.defence;
               if(damage < 0){
                    damage = 0;
               }
               gp.player.life -= damage;
               gp.player.invincible = true;
          }
     }

     /**
      * In this method handled painting monsters health bar and calculate screen coordinates
      */
     public void draw(Graphics2D g2){
          int screenX = worldX - gp.player.worldX + gp.player.screenX;
          int screenY = worldY - gp.player.worldY + gp.player.screenY;

          if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                  worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                  worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                  worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

               BufferedImage image = switch (direction) {
                   case "left" -> faceLeft;
                   case "up" -> back;
                   case "right" -> faceRight;
                   default -> face;
               };

               //Monster Health
               if(type == 2 && hpBarOn){
                    double oneScale = (double)gp.tileSize/maxLife;
                    double hpBarValue = oneScale * life;

                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                    g2.setColor(new Color(193, 0, 23));
                    g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                    hpBarCounter++;

                    if(hpBarCounter > 600){
                         hpBarCounter = 0;
                         hpBarOn = false;
                    }
               }

               if(invincible == true){
                    hpBarOn = true;
                    hpBarCounter = 0;
                    changeAlpha(g2,0.4F);
               }
               if(dying == true){
                    dyingAnimation(g2);
               }

               g2.drawImage(image, screenX, screenY, null);

               changeAlpha(g2,1F);
          }
     }

     /**
      * Method handles dying animation
      */
     public void dyingAnimation(Graphics2D g2){

          dyingCounter++;
          int i = 5;

          if(dyingCounter <= 5){changeAlpha(g2,0f);}
          if(dyingCounter > i  && dyingCounter <= i *2 ){changeAlpha(g2,1f);}
          if(dyingCounter > i*2 && dyingCounter <= i*3){changeAlpha(g2,0f);}
          if(dyingCounter > i*3 && dyingCounter <= i*4){changeAlpha(g2,1f);}
          if(dyingCounter > i*4 && dyingCounter <= i*5){changeAlpha(g2,0f);}
          if(dyingCounter > i*5 && dyingCounter <= i*6){changeAlpha(g2,1f);}
          if(dyingCounter > i*6 && dyingCounter <= i*7){changeAlpha(g2,0f);}
          if(dyingCounter > i*7 && dyingCounter <= i*8){changeAlpha(g2,1f);}
          if(dyingCounter > i*8){
               alive = false;
          }
     }

     /**
      *Method change the transparency (alpha) level of the graphics context
      * @param alphaValue specifies the alpha (transparency) level.
      */
     public void changeAlpha(Graphics2D g2, float alphaValue){
          g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
     }

     /**
      * Method for setting and loading images in game
      * @param imagePath is for pathing the road to where the image stored
      * @param width for scaling image
      * @param height for scaling image
      * @return image
      */
     public BufferedImage setup(String imagePath, int width, int height){
          Utility ut = new Utility();
          BufferedImage image = null;

          try{
               image = ImageIO.read(getClass().getResourceAsStream( imagePath + ".png"));
               image = ut.scaleImg(image,width, height);
          }catch(IOException e){
               e.printStackTrace();
          }
          return image;
     }
}
