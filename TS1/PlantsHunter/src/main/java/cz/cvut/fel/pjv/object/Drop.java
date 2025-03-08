package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.entity.Player;

public class Drop extends Entity {
    GamePanel gp;

    public Drop(GamePanel gp) {
        super(gp);
        name = "Drop";
        type = type_consumable;
        value = 4;
        face = setup("/objects/gasoline_drop", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nSpeed booster";
    }

    public void use(Entity entity){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You gain " + name;

        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.boostSpeed(value, 5000); //  (5 seconds)
        }
    }
}
