package cz.cvut.fel.pjv.entity;

import cz.cvut.fel.pjv.GamePanel;

import java.util.Random;

public class NPC_Yuzo extends Entity {

    public NPC_Yuzo(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;
        getImage();
        setDialogue();
    }

    public void getImage(){
        face = setup("/npc/npcFace",gp.tileSize,gp.tileSize);
        faceLeft = setup("/npc/npcLeft",gp.tileSize,gp.tileSize);
        faceRight = setup("/npc/npcRight",gp.tileSize,gp.tileSize);
        back = setup("/npc/npcBack",gp.tileSize,gp.tileSize);
    }

    public void setDialogue(){

        dialogues[0] = "Hello, stranger";
        dialogues[1] = "I am Yuzo, guardian \nof Flora Prime";
        dialogues[2] = "Good luck in your\n mission";
    }

    /**
     * Method for letting NPC move on map
     */
    public void setAction(){
        actionLockCounter++;

        if(actionLockCounter == 120){

            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick up a number from 1 to 100

            if(i <= 25){
                direction = "up";
            }
            if(i > 25 && i <= 50){
                direction = "down";
            }
            if(i > 50 && i <= 75) {
                direction = "left";
            }
            if(i > 75 && i <= 100){
                direction = "right";
            }
            actionLockCounter = 0;
        }

    }

    public void speak(){
        super.speak();
    }
}
