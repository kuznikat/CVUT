package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Strawberry extends Entity {
    GamePanel gp;
    public Strawberry(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        value = 1;
        name = "Strawberry";
        face = setup("/objects/strawberry", gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nHealing\n strawberry";
    }

    public void use(Entity entity){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You eat " + name + "\n" + "Your life has been recovered\nby " + value;
        entity.life += value;
        if(gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(3);
    }

}
