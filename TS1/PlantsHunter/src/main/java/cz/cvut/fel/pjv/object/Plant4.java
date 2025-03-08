package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Plant4 extends Entity {
    GamePanel gp;
    public Plant4(GamePanel gp) {
        super(gp);
        name = "Plant4";
        face = setup("/objects/plant4", gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nPlant4";
    }
}
