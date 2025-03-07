package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Shield extends Entity {
    GamePanel gp;
    public Shield(GamePanel gp) {
        super(gp);
        type = type_shield;
        name = "Shield";
        face = setup("/objects/shield", gp.tileSize, gp.tileSize);
        defenceValue = 1;
        description = "[" + name + "]\nMagic shield";
    }
}
