package cz.cvut.fel.pjv.monster;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.object.Drop;
import cz.cvut.fel.pjv.object.Soda;
import cz.cvut.fel.pjv.object.Strawberry;

import java.util.Random;

public class Monster  extends Entity {
    GamePanel gp;

    public Monster(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Frog";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 3;
        defence = 0;
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage(){

        faceLeft = setup("/monster/frog1",gp.tileSize,gp.tileSize);
        faceRight = setup("/monster/frog2",gp.tileSize,gp.tileSize);
        face = setup("/monster/frog1",gp.tileSize,gp.tileSize);
        back = setup("/monster/frog2",gp.tileSize,gp.tileSize);

    }

    public void setAction () {

        actionLockCounter++;

        if (actionLockCounter == 100) {

            Random random = new Random();
            int i = random.nextInt(80) + 1; // pick up a number from 1 to 100

            if (i <= 20) {
                direction = "up";
            }
            if (i > 20 && i <= 40) {
                direction = "down";
            }
            if (i > 40 && i <= 60) {
                direction = "left";
            }
            if (i > 60 && i <= 80) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    @Override
    public void damageReaction(){
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        //Set the drop
        if (i < 50) {
            dropItem(new Soda(gp));
        }
        if (i >= 50 && i < 75) {
            dropItem(new Strawberry(gp));
        }
        if (i >= 75 && i < 100) {
            dropItem(new Drop(gp));

        }
    }
}

