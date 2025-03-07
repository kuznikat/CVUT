package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Soda extends Entity {
    GamePanel gp;
    public Soda(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        value = 4;
        name = "Soda";
        face = setup("/objects/soda_can",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nRestore life";
    }

    public void use(Entity entity){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + "\n" + "Your life has been recovered\nby " + value;
        entity.life += value;
        if(gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(3);
    }
}
