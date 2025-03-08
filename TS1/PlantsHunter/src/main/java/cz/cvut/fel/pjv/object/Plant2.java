package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Plant2 extends Entity {
    GamePanel gp;
    public Plant2(GamePanel gp) {
        super(gp);
        name = "Plant2";
        face = setup("/objects/plant2",gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nPlant2";
    }
}
